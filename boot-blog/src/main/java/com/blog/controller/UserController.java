package com.blog.controller;

import cn.hutool.json.JSONUtil;
import com.blog.dto.Result;
import com.blog.dto.UserDTO;
import com.blog.service.UserService;
import com.blog.settings.RedisSettings;
import com.blog.util.MottoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

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
        return userService.checkAdmin(id);
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

    @PostMapping("/motto")
    public Result setMotto(@RequestBody UserDTO body,
                           @RequestParam("kusuri_token") String token
    ) {
        String motto = body.getMotto();
        if (motto == null) {
            return Result.fail("无效参数");
        }
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (!MottoUtils.check(motto)) {
            return Result.fail("签名不合规");
        }
        userService.update()
                .eq("id", user.getId())
                .set("motto", motto)
                .update();
        redisTemplate.delete(RedisSettings.USER_PREFIX + user.getId());
        user.setMotto(motto);
        redisTemplate.opsForValue().set(RedisSettings.TOKEN_PREFIX + token, JSONUtil.toJsonStr(user));
        return Result.ok();
    }
}
