package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.Result;
import com.blog.pojo.BlogComment;

public interface BlogCmtService extends IService<BlogComment> {
    Result getBlogCommentsById(Long id, Long page, Long limit);
}
