package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.Result;
import com.blog.mapper.TagMapper;
import com.blog.pojo.BlogTag;
import com.blog.service.TagService;
import com.blog.settings.RedisSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, BlogTag> implements TagService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Result getTagById(Long id) {
        String key = RedisSettings.TAG_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            BlogTag content = getById(id);
            if (content == null) {
                redisTemplate.opsForValue().set(key, "", 1L, TimeUnit.SECONDS);
                return Result.fail("标签不存在");
            } else {
                redisTemplate.opsForValue().set(key, content.getName());
            }
            return Result.ok(content.getName());
        }
        if (value.equals("")) {
            return Result.fail("标签不存在");
        }
        return Result.ok(value);
    }
}
