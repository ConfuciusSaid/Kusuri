package com.blogcontent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blogcontent.dto.BlogDTO;
import com.blogcontent.dto.Result;
import com.blogcontent.pojo.Blog;

public interface BlogService extends IService<Blog> {
    Result getBlogByUserId(Long id, Long page, Long limit);

    Result acceptReview(BlogDTO blogDTO);

    Result deleteBlog(Long id);

    Result getAllBlogs(Long page, Long limit);
}
