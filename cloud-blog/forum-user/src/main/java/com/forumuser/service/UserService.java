package com.forumuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forumuser.dto.Result;
import com.forumuser.pojo.User;
import com.forumuser.dto.UserDTO;

import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {
    String getCode();

    Result checkCode(String code);

    boolean checkAccountAndPassword(String account, String password);

    Result getUserById(Long id);

    //Result login(String code, String account, String password, String token, HttpServletResponse response);

    Result logout(String token);

    UserDTO checkToken(String token);

    Result loginByPassword(String account, String password);

    Result loginByCode(String code);

    Result register(String code, String account, String password);

    void deleteCode(String code);

    Result getAllUsers(Long page, Long limit);
}
