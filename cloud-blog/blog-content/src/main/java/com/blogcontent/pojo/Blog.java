package com.blogcontent.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@TableName("tb_blog")
public class Blog {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    Long userId;
    String title;
    Date createTime;
    Date updateTime;
    @TableField(exist = false)
    List<String> tags;
}
