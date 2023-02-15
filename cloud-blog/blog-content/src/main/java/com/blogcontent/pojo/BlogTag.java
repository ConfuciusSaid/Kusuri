package com.blogcontent.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@TableName("tb_blog_tag")
public class BlogTag {
    Long id;
    String name;
}
