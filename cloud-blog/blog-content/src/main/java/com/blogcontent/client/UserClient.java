package com.blogcontent.client;

import cn.hutool.core.util.BooleanUtil;
import com.blogcontent.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("userservice")
public interface UserClient {
    @GetMapping("/user/token")
    UserDTO checkToken(@RequestParam("kusuri_token") String token);
    @GetMapping("/user/admin")
    Boolean checkAdmin(@RequestParam("id") Long id);
}
