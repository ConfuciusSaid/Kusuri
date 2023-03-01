package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.UserComment;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCmtMapper extends BaseMapper<UserComment> {
}
