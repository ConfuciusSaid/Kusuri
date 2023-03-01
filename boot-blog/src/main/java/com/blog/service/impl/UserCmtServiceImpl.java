package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.Result;
import com.blog.dto.UserDTO;
import com.blog.mapper.UserCmtMapper;
import com.blog.pojo.Blog;
import com.blog.pojo.UserComment;
import com.blog.service.UserCmtService;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserCmtServiceImpl extends ServiceImpl<UserCmtMapper, UserComment> implements UserCmtService {

    @Autowired
    UserCmtMapper userCmtMapper;

    @Autowired
    UserService userService;

    public Result getUserComments(Long page, Long limit, Wrapper<UserComment> wrapper) {
        Page<UserComment> producePage = new Page<>(page, limit);
        userCmtMapper.selectPage(producePage, wrapper);
        for (UserComment comment : producePage.getRecords()) {
            comment.setUser((UserDTO) userService.getUserById(comment.getUserId()).getData());
        }
        return Result.ok(producePage);
    }

    @Override
    public Result getUserCommentsById(Long id, Long page, Long limit) {
        LambdaQueryWrapper<UserComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserComment::getTargetId, id);
        wrapper.orderByDesc(UserComment::getCreateTime);
        return getUserComments(page, limit, wrapper);
    }
}
