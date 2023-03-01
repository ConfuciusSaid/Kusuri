package com.blog.service.impl;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dto.BlogDTO;
import com.blog.dto.Result;
import com.blog.mapper.BlogMapper;
import com.blog.pojo.Blog;
import com.blog.pojo.BlogContent;
import com.blog.pojo.BlogInfo;
import com.blog.pojo.BlogTag;
import com.blog.service.*;
import com.blog.settings.RedisSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    BlogMapper blogMapper;

    @Autowired
    ContentService contentService;

    @Autowired
    BlogInfoService infoService;

    @Autowired
    TagService tagService;

    @Autowired
    BlogCmtService blogCmtService;

    @Autowired
    StringRedisTemplate redisTemplate;

    public Result getBlogs(Long page, Long limit, Wrapper<Blog> wrapper) {
        Page<Blog> producePage = new Page<>(page, limit);
        blogMapper.selectPage(producePage, wrapper);

        for (Blog blog : producePage.getRecords()) {
            blog.setTags(new ArrayList<>());
            Result result = infoService.getInfoByBlogId(blog.getId());
            if (!result.getSuccess()) return Result.fail("获取标签失败");

            List<Long> listTagId = (List<Long>) result.getData();
            for (Long tagId : listTagId) {
                result = tagService.getTagById(tagId);
                if (!result.getSuccess()) return Result.fail("获取标签失败");
                blog.getTags().add((String) result.getData());
            }
        }

        return Result.ok(producePage);
    }

    @Override
    public Result getAllBlogs(Long page, Long limit) {
        return getBlogs(page, limit, null);
    }

    @Override
    public Blog getBlogById(Long id) {
        String key = RedisSettings.BLOG_PREFIX + id;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            Blog blog = getById(id);
            if (blog == null) {
                redisTemplate.opsForValue().set(key, "", RedisSettings.EMPTY_TTL, TimeUnit.SECONDS);
                return null;
            } else {
                redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(blog), RedisSettings.BLOG_TTL,
                        TimeUnit.SECONDS);
            }
            return blog;
        }
        if (value.equals("")) {
            return null;
        }
        return JSONUtil.toBean(value, Blog.class);
    }

    @Override
    public Result getBlogByUserId(Long id, Long page, Long limit) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getUserId, id);
        wrapper.orderByDesc(Blog::getCreateTime);
        return getBlogs(page, limit, wrapper);
    }

    @Override
    public Result acceptReview(BlogDTO blogDTO) {
        Blog blog = new Blog();
        BlogContent content = new BlogContent();
        List<BlogInfo> infoList = new ArrayList<>();

        // 添加博客
        blog.setUserId(blogDTO.getUserId());
        blog.setTitle(blogDTO.getTitle());
        save(blog);

        // 添加博客内容
        content.setId(blog.getId());
        content.setMarkdown(blogDTO.getContent());
        contentService.save(content);

        // 添加博客标签
        for (String tag : JSONUtil.toList(blogDTO.getTags(), String.class)) {
            // 查询标签
            BlogTag blogTag = tagService.query().eq("name", tag).one();
            // 标签不存在则创建
            if (blogTag == null) {
                blogTag = new BlogTag();
                blogTag.setName(tag);
                tagService.save(blogTag);
            }
            BlogInfo blogInfo = new BlogInfo();
            blogInfo.setBlogId(blog.getId());
            blogInfo.setTagId(blogTag.getId());
            infoList.add(blogInfo);
        }
        // 添加标签与博客的关系
        infoService.saveBatch(infoList);

        return Result.ok();
    }

    @Override
    public Result deleteBlog(Long id) {
        infoService.update().eq("blog_id", id).remove();
        blogCmtService.update().eq("blog_id", id).remove();
        contentService.removeById(id);
        removeById(id);
        return Result.ok();
    }

}
