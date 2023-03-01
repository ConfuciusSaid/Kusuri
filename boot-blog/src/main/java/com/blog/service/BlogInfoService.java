package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.Result;
import com.blog.pojo.BlogInfo;

public interface BlogInfoService extends IService<BlogInfo> {
    Result getInfoByBlogId(Long id);
}
