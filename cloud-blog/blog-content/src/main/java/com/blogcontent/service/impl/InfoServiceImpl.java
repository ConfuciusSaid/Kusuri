package com.blogcontent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogcontent.mapper.InfoMapper;
import com.blogcontent.pojo.BlogInfo;
import com.blogcontent.service.InfoService;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, BlogInfo> implements InfoService {
}
