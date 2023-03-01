package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.BlogDTO;
import com.blog.mapper.BlogDTOMapper;
import com.blog.service.BlogDTOService;
import org.springframework.stereotype.Service;

@Service
public class BlogDTOServiceImpl extends ServiceImpl<BlogDTOMapper, BlogDTO>implements BlogDTOService {
}
