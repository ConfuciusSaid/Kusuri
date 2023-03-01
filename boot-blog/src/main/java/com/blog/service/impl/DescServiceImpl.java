package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.Result;
import com.blog.mapper.DescMapper;
import com.blog.pojo.Desc;
import com.blog.service.DescService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@MapperScan("com.blogmain.mapper")
public class DescServiceImpl extends ServiceImpl<DescMapper, Desc> implements DescService {
    @Autowired
    DescMapper descMapper;

    @Override
    public Result getDescByUserId(Long id, Long page, Long limit) {
        Page<Desc> producePage = new Page<>(page, limit);
        LambdaQueryWrapper<Desc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Desc::getUserId, id);
        descMapper.selectPage(producePage, wrapper);
        return Result.ok(producePage);
    }
}
