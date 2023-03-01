package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@TableName("tb_blog_info")
public class BlogInfo {
    Long blogId;
    Long tagId;
}
