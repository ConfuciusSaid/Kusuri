package com.blog.controller;

import com.blog.dto.Result;
import com.blog.dto.UserDTO;
import com.blog.pojo.Blog;
import com.blog.pojo.BlogComment;
import com.blog.pojo.UserComment;
import com.blog.service.BlogCmtService;
import com.blog.service.BlogService;
import com.blog.service.UserCmtService;
import com.blog.service.UserService;
import com.blog.settings.DataSettings;
import com.blog.settings.RedisSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    UserCmtService userCmtService;

    @Autowired
    BlogCmtService blogCmtService;

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/user/{id}")
    public Result getUserCommentsById(@PathVariable Long id,
                                      @RequestParam(name = "page", defaultValue = "1") Long page,
                                      @RequestParam(name = "limit", defaultValue = "20") Long limit
    ) {
        if (limit < 0 || limit > 20) limit = 20L;
        return userCmtService.getUserCommentsById(id, page, limit);
    }

    @PostMapping("/user/{id}")
    public Result insertUserComment(@PathVariable Long id,
                                    @RequestBody UserComment body,
                                    @RequestParam("kusuri_token") String token) {
        String markdown = body.getMarkdown();
        if (markdown == null) {
            return Result.fail("无效参数");
        }
        if (markdown.length() > DataSettings.USER_CMT_MARKDOWN) {
            return Result.fail("评论过长");
        }
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        String key = RedisSettings.USER_CMT_PREFIX + user.getId();
        String value = redisTemplate.opsForValue().get(key);
        if (value != null && Integer.parseInt(value) > DataSettings.USER_CMT_COUNT) {
            return Result.fail("您发评论过于频繁");
        }

        UserComment comment = new UserComment();
        comment.setUserId(user.getId());
        comment.setMarkdown(markdown);
        comment.setTargetId(id);
        userCmtService.save(comment);
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, RedisSettings.USER_CMT_TTL, TimeUnit.SECONDS);

        return Result.ok();
    }

    @DeleteMapping("/user/{id}")
    public Result deleteUserCommentById(@PathVariable Long id,
                                        @RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (!userCmtService.update()
                .eq("id", id)
                .and(wrapper -> wrapper.eq("user_id", user.getId())
                        .or().eq("target_id", user.getId())).remove()) {
            return Result.fail("目标不存在");
        }
        return Result.ok();
    }

    @GetMapping("/blog/{id}")
    public Result getBlogCommentsById(@PathVariable Long id,
                                      @RequestParam(name = "page", defaultValue = "1") Long page,
                                      @RequestParam(name = "limit", defaultValue = "20") Long limit
    ) {
        if (limit < 0 || limit > 20) limit = 20L;
        return blogCmtService.getBlogCommentsById(id, page, limit);
    }

    @PostMapping("/blog/{id}")
    public Result insertBlogComment(@PathVariable Long id,
                                    @RequestBody UserComment body,
                                    @RequestParam("kusuri_token") String token) {
        String markdown = body.getMarkdown();
        if (markdown == null) {
            return Result.fail("无效参数");
        }
        if (markdown.length() > DataSettings.USER_CMT_MARKDOWN) {
            return Result.fail("评论过长");
        }
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        String key = RedisSettings.USER_CMT_PREFIX + user.getId();
        String value = redisTemplate.opsForValue().get(key);
        if (value != null && Integer.parseInt(value) > DataSettings.USER_CMT_COUNT) {
            return Result.fail("您发评论过于频繁");
        }

        BlogComment comment = new BlogComment();
        comment.setUserId(user.getId());
        comment.setMarkdown(markdown);
        comment.setBlogId(id);
        blogCmtService.save(comment);
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, RedisSettings.USER_CMT_TTL, TimeUnit.SECONDS);

        return Result.ok();
    }

    @DeleteMapping("/blog/{id}")
    public Result deleteBlogCommentById(@PathVariable Long id,
                                        @RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        BlogComment comment = blogCmtService.getById(id);
        Blog blog = blogService.getBlogById(comment.getBlogId());
        if (blog == null) {
            return Result.fail("目标不存在");
        }
        if (user.getId().equals(blog.getUserId()) || user.getId().equals(comment.getUserId())) {
            if (!blogCmtService.update()
                    .eq("id", id).remove()) {
                return Result.fail("目标不存在");
            }
        }
        return Result.ok();
    }
}
