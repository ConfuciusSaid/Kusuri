package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.Result;
import com.blog.dto.UserDTO;
import com.blog.pojo.User;

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

    Boolean checkAdmin(Long id);
}
