package com.blogcontent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blogcontent.pojo.BlogContent;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentMapper extends BaseMapper<BlogContent> {
}
