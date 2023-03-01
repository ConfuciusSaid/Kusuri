package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.Blog;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogMapper extends BaseMapper<Blog> {
}
