package com.blog.service.impl;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.Result;
import com.blog.mapper.ContentMapper;
import com.blog.pojo.BlogContent;
import com.blog.service.ContentService;
import com.blog.settings.RedisSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, BlogContent> implements ContentService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Result getContentById(Long id) {
        String key = RedisSettings.CONTENT_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            BlogContent content = getById(id);
            if (content == null) {
                redisTemplate.opsForValue().set(key, "", RedisSettings.EMPTY_TTL, TimeUnit.SECONDS);
                return Result.fail("文章不存在");
            } else {
                redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(content), RedisSettings.CONTENT_TTL,
                        TimeUnit.SECONDS);
            }
            return Result.ok(content);
        }
        if (value.equals("")) {
            return Result.fail("文章不存在");
        }
        return Result.ok(JSONUtil.toBean(value, BlogContent.class));
    }
}
