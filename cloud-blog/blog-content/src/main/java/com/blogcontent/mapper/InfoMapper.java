package com.blogcontent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blogcontent.pojo.BlogInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoMapper extends BaseMapper<BlogInfo> {
}
