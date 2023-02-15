package com.blogcontent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogcontent.mapper.TagMapper;
import com.blogcontent.pojo.BlogTag;
import com.blogcontent.service.TagService;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, BlogTag> implements TagService {
}
