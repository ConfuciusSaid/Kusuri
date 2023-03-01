package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.dto.BlogDTO;
import com.blog.dto.Result;
import com.blog.pojo.Blog;

public interface BlogService extends IService<Blog> {
    Result getBlogByUserId(Long id, Long page, Long limit);

    Result acceptReview(BlogDTO blogDTO);

    Result deleteBlog(Long id);

    Result getAllBlogs(Long page, Long limit);

    Blog getBlogById(Long id);
}
