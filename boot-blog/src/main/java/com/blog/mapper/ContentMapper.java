package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.BlogContent;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentMapper extends BaseMapper<BlogContent> {
}
