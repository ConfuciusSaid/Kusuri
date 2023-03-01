package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.Result;
import com.blog.pojo.Info;

public interface InfoService extends IService<Info> {
    Result getInfoByUserId(Long id);

    void deleteInfoCache(Long id);
}
