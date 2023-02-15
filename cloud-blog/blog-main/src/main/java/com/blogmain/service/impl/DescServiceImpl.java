package com.blogmain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogmain.mapper.DescMapper;
import com.blogmain.pojo.Desc;
import com.blogmain.service.DescService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;

@Service
@MapperScan("com.blogmain.mapper")
public class DescServiceImpl extends ServiceImpl<DescMapper, Desc> implements DescService {
}
