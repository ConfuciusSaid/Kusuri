package com.blog.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.BlogDTO;
import com.blog.dto.Result;
import com.blog.dto.ReviewStatus;
import com.blog.dto.UserDTO;
import com.blog.service.*;
import com.blog.settings.DataSettings;
import com.blog.util.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;
    @Autowired
    ContentService contentService;
    @Autowired
    BlogInfoService infoService;
    @Autowired
    TagService tagService;
    //    @Autowired
//    StringRedisTemplate redisTemplate;
    @Autowired
    BlogDTOService blogdtoService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    Result getAllBlogs(@RequestParam(name = "page", defaultValue = "1") Long page,
                       @RequestParam(name = "limit", defaultValue = "10") Long limit) {
        if (limit < 0 || limit > 10) limit = 10L;
        return blogService.getAllBlogs(page, limit);
    }

    @GetMapping("/{id}")
    Result getBlogByUserId(@PathVariable Long id,
                           @RequestParam(name = "page", defaultValue = "1") Long page,
                           @RequestParam(name = "limit", defaultValue = "10") Long limit
    ) {
        if (limit < 0 || limit > 10) limit = 10L;
        return blogService.getBlogByUserId(id, page, limit);
    }

    @DeleteMapping("/{id}")
    Result deleteBlog(@PathVariable Long id,
                      @RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (blogService.query().eq("id", id).eq("user_id", user.getId()).count() == 0) {
            return Result.fail("目标不存在");
        }
        return blogService.deleteBlog(id);
    }

    @GetMapping("/review")
    Result getReview(@RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        return Result.ok(blogdtoService.query().eq("user_id", user.getId()).list());
    }

    @GetMapping("/review/all")
    Result getAllReview(@RequestParam(value = "page", defaultValue = "1") Long page,
                        @RequestParam(value = "limit", defaultValue = "20") Long limit,
                        @RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (!userService.checkAdmin(user.getId())) {
            return Result.fail("权限不足");
        }
        Page<BlogDTO> producePage = new Page<>(page, limit);
        blogdtoService.getBaseMapper().selectPage(producePage, null);
        return Result.ok(producePage);
    }

    @PostMapping("/review")
    Result insertReview(@RequestBody BlogDTO body,
                        @RequestParam("kusuri_token") String token
    ) {
        String title = body.getTitle();
        String content = body.getContent();
        String tags = body.getTags();
        if (title == null || content == null || tags == null) {
            return Result.fail("无效参数");
        }
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (title.length() > DataSettings.BLOG_TITLE) {
            return Result.fail("标题请勿传输超过" + DataSettings.BLOG_TITLE / 1024 + "kb的数据");
        }
        title = MarkdownUtils.defend(title);
        if (content.length() > DataSettings.BLOG_CONTENT) {
            return Result.fail("内容请勿传输超过" + DataSettings.BLOG_CONTENT / 1024 + "kb的数据");
        }
        if (blogdtoService.query().eq("user_id", user.getId()).count() >= DataSettings.REVIEW_COUNT) {
            return Result.fail("您已有" + DataSettings.REVIEW_COUNT + "篇博客正在审核，请耐心等待");
        }
        content = MarkdownUtils.defend(content);
        List<String> tagList = Arrays.asList(tags.split(" "));
        HashSet<String> hashSet = new HashSet<>(tagList);
        if (hashSet.size() != tagList.size()) {
            return Result.fail("存在重复标签");
        }
        if (tagList.size() == 0) {
            return Result.fail("请至少添加一个标签");
        }
        if (tagList.size() > DataSettings.TAG_COUNT) {
            return Result.fail("请勿添加超过" + DataSettings.TAG_COUNT + "个标签");
        }
        for (String tag : tagList) {
            if (tag.length() > DataSettings.TAG_NAME) {
                return Result.fail("标签长度请勿超过" + DataSettings.TAG_NAME + "字符");
            }
        }
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setContent(content);
        blogDTO.setUserId(user.getId());
        blogDTO.setTitle(title);
        blogDTO.setStatus(ReviewStatus.Reviewing);
        blogDTO.setTags(JSONUtil.toJsonStr(tagList));

        blogdtoService.save(blogDTO);
        return Result.ok("上传成功，请等待审核");
    }

    @DeleteMapping("/review")
    Result cancelReview(@RequestParam("id") Long id,
                        @RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (!blogdtoService.update().eq("id", id).eq("user_id", user.getId()).remove()) {
            return Result.fail("目标不存在");
        }
        return Result.ok();
    }

    @PutMapping("/review/manage")
    Result acceptReview(@RequestParam("id") Long id,
                        @RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (!userService.checkAdmin(user.getId())) {
            return Result.fail("权限不足");
        }
        BlogDTO blogDTO = blogdtoService.getById(id);
        Result result = blogService.acceptReview(blogDTO);
        if (result.getSuccess()) {
            blogdtoService.removeById(id);
        }
        return result;
    }

    @DeleteMapping("/review/manage")
    Result refuseReview(@RequestParam("id") Long id,
                        @RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (!userService.checkAdmin(user.getId())) {
            return Result.fail("权限不足");
        }
        blogdtoService.update().eq("id", id).set("status", ReviewStatus.Failed).update();
        return Result.ok();
    }

    @GetMapping("/content/{id}")
    Result getContentById(@PathVariable Long id) {
        return contentService.getContentById(id);
    }

    @GetMapping("/info/{id}")
    Result getInfoById(@PathVariable Long id) {
        return infoService.getInfoByBlogId(id);
    }

    @GetMapping("/tag/{id}")
    Result getTagName(@PathVariable Long id) {
        return tagService.getTagById(id);
    }
}
