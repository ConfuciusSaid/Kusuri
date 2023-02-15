package com.blogcontent.controller;

import com.blogcontent.pojo.Blog;
import com.blogcontent.pojo.BlogContent;
import com.blogcontent.pojo.BlogInfo;
import com.blogcontent.pojo.BlogTag;
import com.blogcontent.service.BlogService;
import com.blogcontent.service.ContentService;
import com.blogcontent.service.InfoService;
import com.blogcontent.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/blog")
    List<Blog> getAllBlog() {
        return blogService.list();
    }

    @GetMapping("/content/{id}")
    BlogContent getContentById(@PathVariable Long id) {
        return contentService.getById(id);
    }

    @GetMapping("/info/{id}")
    List<BlogInfo> getInfoById(@PathVariable Long id) {
        return infoService.query().eq("blog_id", id).list();
    }

    @GetMapping("/tag/{id}")
    String getTagName(@PathVariable Long id) {
        BlogTag tag = tagService.getById(id);
        return tag != null ? tag.getName() : null;
    }
}
