package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.Result;
import com.blog.dto.UserDTO;
import com.blog.mapper.BlogCmtMapper;
import com.blog.pojo.BlogComment;
import com.blog.pojo.UserComment;
import com.blog.service.BlogCmtService;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogCmtServiceImpl extends ServiceImpl<BlogCmtMapper, BlogComment> implements BlogCmtService {

    @Autowired
    BlogCmtMapper blogCmtMapper;

    @Autowired
    UserService userService;

    public Result getBlogComments(Long page, Long limit, Wrapper<BlogComment> wrapper) {
        Page<BlogComment> producePage = new Page<>(page, limit);
        blogCmtMapper.selectPage(producePage, wrapper);
        for (BlogComment comment : producePage.getRecords()) {
            comment.setUser((UserDTO) userService.getUserById(comment.getUserId()).getData());
        }
        return Result.ok(producePage);
    }

    @Override
    public Result getBlogCommentsById(Long id, Long page, Long limit) {
        LambdaQueryWrapper<BlogComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogComment::getBlogId, id);
        wrapper.orderByDesc(BlogComment::getCreateTime);
        return getBlogComments(page, limit, wrapper);
    }
}
