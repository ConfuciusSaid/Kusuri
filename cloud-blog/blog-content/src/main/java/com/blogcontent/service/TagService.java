package com.blogcontent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogcontent.dto.Result;
import com.blogcontent.pojo.BlogTag;

public interface TagService extends IService<BlogTag> {
    Result getTagById(Long id);
}
