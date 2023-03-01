package com.blogmain.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@ToString
@NoArgsConstructor
@TableName("tb_main_info")
public class Info {
    @TableId(value = "id", type = IdType.AUTO)
    Long id;
    Long userId;
    String markdown;
    Date createTime;
    Date updateTime;
}
