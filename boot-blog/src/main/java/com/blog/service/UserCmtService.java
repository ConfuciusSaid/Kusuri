package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.Result;
import com.blog.pojo.UserComment;

public interface UserCmtService extends IService<UserComment> {
    Result getUserCommentsById(Long id, Long page, Long limit);
}
