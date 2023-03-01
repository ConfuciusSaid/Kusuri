package com.blogmain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogmain.dto.Result;
import com.blogmain.pojo.Info;

public interface InfoService extends IService<Info> {
    Result getInfoByUserId(Long id);

    void deleteInfoCache(Long id);
}
