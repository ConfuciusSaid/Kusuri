package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.Result;
import com.blog.pojo.BlogContent;

public interface ContentService extends IService<BlogContent> {
    Result getContentById(Long id);
}
