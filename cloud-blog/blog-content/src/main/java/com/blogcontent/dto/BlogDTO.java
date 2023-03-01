package com.blogcontent.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Data
@ToString
@TableName("tb_blog_review")
public class BlogDTO {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    Long userId;
    String content;
    String title;
    String tags;
    ReviewStatus status;
    Date createTime;
    Date updateTime;
}
