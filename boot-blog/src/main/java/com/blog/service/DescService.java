package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.Result;
import com.blog.pojo.Desc;

public interface DescService extends IService<Desc> {
    Result getDescByUserId(Long id, Long page, Long limit);
}
