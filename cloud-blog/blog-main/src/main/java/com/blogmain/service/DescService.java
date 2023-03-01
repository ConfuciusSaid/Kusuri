package com.blogmain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogmain.dto.Result;
import com.blogmain.pojo.Desc;

public interface DescService extends IService<Desc> {
    Result getDescByUserId(Long id, Long page, Long limit);
}
