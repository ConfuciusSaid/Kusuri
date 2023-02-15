package com.blogcontent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogcontent.mapper.ContentMapper;
import com.blogcontent.pojo.BlogContent;
import com.blogcontent.service.ContentService;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, BlogContent> implements ContentService {
}
