package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.BlogComment;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogCmtMapper extends BaseMapper<BlogComment> {
}
