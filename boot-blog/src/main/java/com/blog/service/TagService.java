package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.Result;
import com.blog.pojo.BlogTag;

public interface TagService extends IService<BlogTag> {
    Result getTagById(Long id);
}
