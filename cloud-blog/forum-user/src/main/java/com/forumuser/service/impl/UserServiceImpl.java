package com.forumuser.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forumuser.dto.Result;
import com.forumuser.mapper.UserMapper;
import com.forumuser.pojo.User;
import com.forumuser.dto.UserDTO;
import com.forumuser.service.UserService;
import com.forumuser.settings.RedisSettings;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public String getCode() {
        String code = RandomUtil.randomString(16);
        redisTemplate.opsForValue().set(RedisSettings.CODE_PREFIX + code, "", RedisSettings.CODE_TTL, TimeUnit.SECONDS);
        return code;
    }

    @Override
    public void deleteCode(String code) {
        redisTemplate.delete(RedisSettings.CODE_PREFIX + code);
    }


    @Override
    public Result checkCode(String code) {
        String value = redisTemplate.opsForValue().get(RedisSettings.CODE_PREFIX + code);
        if (value == null) {
            return Result.fail("验证码已失效");
        }
        if (value.equals("")) {
            return Result.fail("验证码未发送");
        }
        UserDTO userDTO = JSONUtil.toBean(value, UserDTO.class);
        return Result.ok(userDTO);
    }

    @Override
    public boolean checkAccountAndPassword(String account, String password) {
        return account.matches("^[0-9a-zA-Z]{8,12}$") &&
                password.matches("^[0-9a-zA-Z@\\-_#~.]{8,16}$");
    }

    @Override
    public Result getUserById(Long id) {
        String key = RedisSettings.USER_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            User userOrigin = getById(id);
            UserDTO user = null;
            if (userOrigin == null) {
                redisTemplate.opsForValue().set(key, "", RedisSettings.EMPTY_TTL, TimeUnit.SECONDS);
                return Result.fail("用户不存在");
            } else {
                user = BeanUtil.copyProperties(userOrigin, UserDTO.class);
                redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(user), RedisSettings.USER_TTL,
                        TimeUnit.SECONDS);
            }
            return Result.ok(user);
        }
        if (value.equals("")) {
            return Result.fail("用户不存在");
        }
        return Result.ok(JSONUtil.toBean(value, UserDTO.class));
    }

    public Result getUsers(Long page, Long limit, Wrapper<User> wrapper) {
        Page<User> producePage = new Page<>(page, limit);
        getBaseMapper().selectPage(producePage, wrapper);
        IPage<UserDTO> convert = producePage.convert(
                user -> BeanUtil.copyProperties(user, UserDTO.class));
        return Result.ok(convert);
    }

    @Override
    public Result getAllUsers(Long page, Long limit) {
        return getUsers(page, limit, null);
    }

//    @Override
//    public Result login(String code, String account, String password, String token, HttpServletResponse response) {
//        String newToken;
//        UserDTO userDTO;
//        // 查询验证码
//        if (code != null) {
//            userDTO = checkCode(code);
//            if (userDTO == null) {
//                return Result.fail("验证码验证失败");
//            }
//            if (!checkAccountAndPassword(account, password)) {
//                return Result.fail("账号密码不合规");
//            }
//            User user = query().eq("uid", userDTO.getUid()).one();
//
//            if (user == null) {
//                // 注册
//                if (query().eq("account", account).count() != 0) {
//                    return Result.fail("账号已存在");
//                }
//                user = new User();
//                user.setUid(userDTO.getUid());
//                user.setLevel(userDTO.getLevel());
//                user.setAvatar("http://www.kusuri.world/photos/avatar/" + userDTO.getUid() + ".jpg");
//                user.setAccount(account);
//                user.setPassword(SecureUtil.md5(password));
//                user.setName(userDTO.getName());
//                save(user);
//            }
//            userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//            redisTemplate.delete(RedisSettings.CODE_PREFIX + code);
//        } else {
//            if (!checkAccountAndPassword(account, password)) return Result.fail("错误的账号密码");
//            User user = query().eq("account", account).eq("password", SecureUtil.md5(password)).one();
//            if (user == null) {
//                return Result.fail("账号密码不存在");
//            }
//            userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//        }
//
//        newToken = UUID.randomUUID().toString();
//        redisTemplate.opsForValue().set(RedisSettings.TOKEN_PREFIX + newToken,
//                JSONUtil.toJsonStr(userDTO), RedisSettings.TOKEN_TTL, TimeUnit.SECONDS);
//        response.addCookie(new Cookie("kusuri_token", newToken));
//
//        if (token != null) {
//            redisTemplate.delete(RedisSettings.TOKEN_PREFIX + token);
//        }
//
//        return Result.ok(userDTO);
//    }

    @Override
    public Result logout(String token) {
        if (BooleanUtil.isTrue(redisTemplate.delete(RedisSettings.TOKEN_PREFIX + token))) {
            return Result.ok();
        } else
            return Result.fail("登录信息无效");
    }

    @Override
    public UserDTO checkToken(String token) {
        String json = redisTemplate.opsForValue().get(RedisSettings.TOKEN_PREFIX + token);
        if (json == null) return null;
        return JSONUtil.toBean(json, UserDTO.class);
    }

    @Override
    public Result loginByPassword(String account, String password) {
        User user = query().eq("account", account).eq("password", SecureUtil.md5(password)).one();
        if (user == null) {
            return Result.fail("账号密码不存在");
        }
        return Result.ok(BeanUtil.copyProperties(user, UserDTO.class));
    }

    @Override
    public Result loginByCode(String code) {
        Result result = checkCode(code);
        if (!result.getSuccess()) return result;
        UserDTO userDTO = (UserDTO) result.getData();

        User user = query().eq("uid", userDTO.getUid()).one();
        if (user == null) {
            return Result.fail("用户未注册");
        }
        return Result.ok(BeanUtil.copyProperties(user, UserDTO.class));
    }

    @Override
    public Result register(String code, String account, String password) {
        Result result = checkCode(code);
        if (!result.getSuccess()) return result;
        UserDTO userDTO = (UserDTO) result.getData();

        if (query().eq("uid", userDTO.getUid()).count() != 0) {
            return Result.fail("b站账号已注册");
        }
        if (query().eq("account", account).count() != 0) {
            return Result.fail("账号" + account + "已存在");
        }
        if (userDTO.getLevel() == 0) {
            return Result.fail("未持有合格的粉丝牌等级");
        }
        User user = new User();
        user.setUid(userDTO.getUid());
        user.setLevel(userDTO.getLevel());
        user.setAvatar("http://www.kusuri.world/photos/avatar/" + userDTO.getUid() + ".jpg");
        user.setAccount(account);
        user.setPassword(SecureUtil.md5(password));
        user.setName(userDTO.getName());
        save(user);
        return Result.ok(BeanUtil.copyProperties(user, UserDTO.class));
    }

}
