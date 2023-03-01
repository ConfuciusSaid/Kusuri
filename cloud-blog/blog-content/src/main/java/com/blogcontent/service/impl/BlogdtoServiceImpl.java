package com.blogcontent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogcontent.dto.BlogDTO;
import com.blogcontent.mapper.BlogdtoMapper;
import com.blogcontent.service.BlogdtoService;
import org.springframework.stereotype.Service;

@Service
public class BlogdtoServiceImpl extends ServiceImpl<BlogdtoMapper, BlogDTO>implements BlogdtoService {
}
