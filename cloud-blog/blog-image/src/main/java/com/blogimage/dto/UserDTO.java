package com.blogimage.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class UserDTO {
    Long id;
    Long uid;
    Integer level;
    String name;
    String avatar;
    String motto;
}
