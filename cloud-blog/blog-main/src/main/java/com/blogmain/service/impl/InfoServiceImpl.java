package com.blogmain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogmain.mapper.InfoMapper;
import com.blogmain.pojo.Info;
import com.blogmain.service.InfoService;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info> implements InfoService {
}
