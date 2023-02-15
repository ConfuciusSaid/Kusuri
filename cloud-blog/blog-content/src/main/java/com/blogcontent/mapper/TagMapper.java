package com.blogcontent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blogcontent.pojo.BlogTag;
import org.springframework.stereotype.Repository;

@Repository
public interface TagMapper extends BaseMapper<BlogTag> {
}
