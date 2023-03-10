package com.blogcontent.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blogcontent.client.UserClient;
import com.blogcontent.dto.BlogDTO;
import com.blogcontent.dto.Result;
import com.blogcontent.dto.ReviewStatus;
import com.blogcontent.dto.UserDTO;
import com.blogcontent.service.*;
import com.blogcontent.settings.DataSettings;
import com.blogcontent.util.MarkdownUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;
    @Autowired
    ContentService contentService;
    @Autowired
    InfoService infoService;
    @Autowired
    TagService tagService;
    @Autowired
    UserClient userClient;
    //    @Autowired
//    StringRedisTemplate redisTemplate;
    @Autowired
    BlogdtoService blogdtoService;

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
        UserDTO user = userClient.checkToken(token);
        if (user == null) {
            return Result.fail("???????????????");
        }
        if (blogService.query().eq("id", id).eq("user_id", user.getId()).count() == 0) {
            return Result.fail("???????????????");
        }
        return blogService.deleteBlog(id);
    }

    @GetMapping("/review")
    Result getReview(@RequestParam("kusuri_token") String token) {
        UserDTO user = userClient.checkToken(token);
        if (user == null) {
            return Result.fail("???????????????");
        }
        return Result.ok(blogdtoService.query().eq("user_id", user.getId()).list());
    }

    @GetMapping("/review/all")
    Result getAllReview(@RequestParam(value = "page", defaultValue = "1") Long page,
                        @RequestParam(value = "limit", defaultValue = "20") Long limit,
                        @RequestParam("kusuri_token") String token) {
        UserDTO user = userClient.checkToken(token);
        if (user == null) {
            return Result.fail("???????????????");
        }
        if (!userClient.checkAdmin(user.getId())) {
            return Result.fail("????????????");
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
            return Result.fail("????????????");
        }
        UserDTO user = userClient.checkToken(token);
        if (user == null) {
            return Result.fail("???????????????");
        }
        if (title.length() > DataSettings.BLOG_TITLE) {
            return Result.fail("????????????????????????" + DataSettings.BLOG_TITLE / 1024 + "kb?????????");
        }
        title = MarkdownUtil.defend(title);
        if (content.length() > DataSettings.BLOG_CONTENT) {
            return Result.fail("????????????????????????" + DataSettings.BLOG_CONTENT / 1024 + "kb?????????");
        }
        if (blogdtoService.query().eq("user_id", user.getId()).count() >= DataSettings.REVIEW_COUNT) {
            return Result.fail("?????????" + DataSettings.REVIEW_COUNT + "???????????????????????????????????????");
        }
        content = MarkdownUtil.defend(content);
        List<String> tagList = new ArrayList<>(new HashSet<>(Arrays.asList(tags.split(" "))));
        if (tagList.size() == 0) {
            return Result.fail("???????????????????????????");
        }
        for (String tag : tagList) {
            if (tag.length() > DataSettings.TAG_NAME) {
                return Result.fail("????????????????????????" + DataSettings.TAG_NAME + "??????");
            }
        }
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setContent(content);
        blogDTO.setUserId(user.getId());
        blogDTO.setTitle(title);
        blogDTO.setStatus(ReviewStatus.Reviewing);
        blogDTO.setTags(JSONUtil.toJsonStr(tagList));

        blogdtoService.save(blogDTO);
        return Result.ok("??????????????????????????????");
    }

    @DeleteMapping("/review")
    Result cancelReview(@RequestParam("id") Long id,
                        @RequestParam("kusuri_token") String token) {
        UserDTO user = userClient.checkToken(token);
        if (user == null) {
            return Result.fail("???????????????");
        }
        BlogDTO blogDTO = blogdtoService.query().eq("id", id).eq("user_id", user.getId()).one();
        if (blogDTO == null) {
            return Result.fail("???????????????");
        }
        return Result.ok(blogDTO);
    }

    @PutMapping("/review/manage")
    Result acceptReview(@RequestParam("id") Long id,
                        @RequestParam("kusuri_token") String token) {
        UserDTO user = userClient.checkToken(token);
        if (user == null) {
            return Result.fail("???????????????");
        }
        if (!userClient.checkAdmin(user.getId())) {
            return Result.fail("????????????");
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
        UserDTO user = userClient.checkToken(token);
        if (user == null) {
            return Result.fail("???????????????");
        }
        if (!userClient.checkAdmin(user.getId())) {
            return Result.fail("????????????");
        }
       blogdtoService.update().eq("id", id).set("status", ReviewStatus.Failed);
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
