package com.blogcontent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogcontent.dto.Result;
import com.blogcontent.pojo.BlogContent;

public interface ContentService extends IService<BlogContent> {
    Result getContentById(Long id);
}
