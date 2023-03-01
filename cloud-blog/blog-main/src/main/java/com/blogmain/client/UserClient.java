package com.blogmain.client;

import com.blogmain.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("userservice")
public interface UserClient {
    @GetMapping("/user/token")
    UserDTO checkToken(@RequestParam("kusuri_token") String token);
}
