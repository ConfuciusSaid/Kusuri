package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
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
    @JsonFormat(timezone = "GMT+8")
    Date createTime;
    @JsonFormat(timezone = "GMT+8")
    Date updateTime;
    @TableField(exist = false)
    List<String> tags;
}
