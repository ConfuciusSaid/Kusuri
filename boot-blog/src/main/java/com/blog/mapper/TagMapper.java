package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.pojo.BlogTag;
import org.springframework.stereotype.Repository;

@Repository
public interface TagMapper extends BaseMapper<BlogTag> {
}
