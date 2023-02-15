package com.blogcontent.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@ToString
@NoArgsConstructor
@TableName("tb_blog")
public class Blog {
    Long id;
    Long userId;
    String title;
    Date createTime;
    Date updateTime;
}
