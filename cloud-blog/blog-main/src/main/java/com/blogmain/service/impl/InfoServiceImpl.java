package com.blogmain.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blogmain.dto.Result;
import com.blogmain.mapper.InfoMapper;
import com.blogmain.pojo.Info;
import com.blogmain.service.InfoService;
import com.blogmain.settings.RedisSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info> implements InfoService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Result getInfoByUserId(Long id) {
        String key = RedisSettings.INFO_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            List<Info> list = query().eq("user_id", id).list();
            if (list.isEmpty()) {
                redisTemplate.opsForValue().set(key, "", RedisSettings.EMPTY_TTL, TimeUnit.SECONDS);
                return Result.ok(list);
            } else {
                redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(list), RedisSettings.INFO_TTL,
                        TimeUnit.SECONDS);
            }
            return Result.ok(list);
        }
        if (value.equals("")) {
            return Result.ok(Collections.emptyList());
        }
        return Result.ok(JSONUtil.toList(value, Info.class));
    }

    @Override
    public void deleteInfoCache(Long id) {
        String key = RedisSettings.INFO_PREFIX + id;
        redisTemplate.delete(key);
    }
}
