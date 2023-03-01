package com.forumuser.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.forumuser.dto.Result;
import com.forumuser.dto.UserDTO;
import com.forumuser.pojo.User;
import com.forumuser.service.UserService;
import com.forumuser.settings.RedisSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/code")
    public Result getCode() {
        return Result.ok(userService.getCode());
    }

    @GetMapping("/code/{code}")
    public Result checkCode(@PathVariable String code) {
        return userService.checkCode(code);
    }

    @DeleteMapping("/code/{code}")
    public Result deleteCode(@PathVariable String code) {
        userService.deleteCode(code);
        return Result.ok();
    }

    @GetMapping("/")
    public Result getAllUsers(@RequestParam(name = "page", defaultValue = "1") Long page,
                              @RequestParam(name = "limit", defaultValue = "10") Long limit) {
        if (limit < 0 || limit > 10) limit = 10L;
        return userService.getAllUsers(page, limit);
    }

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/token")
    public UserDTO checkToken(@RequestParam("kusuri_token") String token) {
        return userService.checkToken(token);
    }

    @GetMapping("/admin")
    public Boolean checkAdmin(@RequestParam("id") Long id) {
        return redisTemplate.hasKey(RedisSettings.ADMIN_PREFIX + id);
    }

    @PostMapping("/login")
    public Result login(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "kusuri_token", required = false) String token
    ) {
        Result result = null;
        if (code != null) {
            if (account != null && password != null) {
                if (!userService.checkAccountAndPassword(account, password)) {
                    return Result.fail("账号密码格式错误");
                }
                result = userService.register(code, account, password);
            } else {
                result = userService.loginByCode(code);
            }
        } else {
            if (account != null && password != null) {
                if (!userService.checkAccountAndPassword(account, password)) {
                    return Result.fail("账号密码格式错误");
                }
                result = userService.loginByPassword(account, password);
            }
        }
        if (result == null) {
            return Result.fail("传参错误");
        }
        if (!result.getSuccess()) return result;

        String newToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(RedisSettings.TOKEN_PREFIX + newToken,
                JSONUtil.toJsonStr(result.getData()), RedisSettings.TOKEN_TTL, TimeUnit.SECONDS);
        //response.addCookie(new Cookie("kusuri_token", newToken));

        if (token != null) {
            redisTemplate.delete(RedisSettings.TOKEN_PREFIX + token);
        }

        redisTemplate.delete(RedisSettings.CODE_PREFIX + code);
        result.setErrorMsg(newToken);
        return result;
    }

    @PostMapping("/logout")
    public Result logout(@RequestParam("kusuri_token") String token) {
        return userService.logout(token);
    }
}
