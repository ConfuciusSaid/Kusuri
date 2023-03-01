package com.blogcontent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogcontent.dto.Result;
import com.blogcontent.pojo.BlogInfo;

import java.util.List;

public interface InfoService extends IService<BlogInfo> {
    Result getInfoByBlogId(Long id);
}
