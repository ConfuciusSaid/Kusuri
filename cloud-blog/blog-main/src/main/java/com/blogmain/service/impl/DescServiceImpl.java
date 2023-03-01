package com.blogmain.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogmain.dto.Result;
import com.blogmain.mapper.DescMapper;
import com.blogmain.pojo.Desc;
import com.blogmain.service.DescService;
import com.blogmain.settings.RedisSettings;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
