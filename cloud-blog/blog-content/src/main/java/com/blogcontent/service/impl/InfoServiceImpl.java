package com.blogcontent.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogcontent.dto.Result;
import com.blogcontent.mapper.InfoMapper;
import com.blogcontent.pojo.Blog;
import com.blogcontent.pojo.BlogInfo;
import com.blogcontent.service.InfoService;
import com.blogcontent.settings.RedisSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, BlogInfo> implements InfoService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Result getInfoByBlogId(Long id) {
        String key = RedisSettings.INFO_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            List<Long> list = query().eq("blog_id", id).list()
                    .stream().map(BlogInfo::getTagId).collect(Collectors.toList());
            if (list.isEmpty()) {
                redisTemplate.opsForValue().set(key, "", RedisSettings.EMPTY_TTL, TimeUnit.SECONDS);
                return Result.fail("文章不存在");
            } else {
                redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(list), RedisSettings.INFO_TTL,
                        TimeUnit.SECONDS);
            }
            return Result.ok(list);
        }
        if (value.equals("")) {
            return Result.fail("文章不存在");
        }
        return Result.ok(JSONUtil.toList(value, Long.class));
    }
}
