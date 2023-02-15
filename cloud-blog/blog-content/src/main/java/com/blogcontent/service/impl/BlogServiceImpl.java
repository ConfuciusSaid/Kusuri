package com.blogcontent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogcontent.mapper.BlogMapper;
import com.blogcontent.pojo.Blog;
import com.blogcontent.service.BlogService;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
}
