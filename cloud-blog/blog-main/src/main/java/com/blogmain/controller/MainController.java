package com.blogmain.controller;

import com.blogmain.pojo.Desc;
import com.blogmain.pojo.Info;
import com.blogmain.service.DescService;
import com.blogmain.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    DescService descService;

    @Autowired
    InfoService infoService;

    @GetMapping("/desc")
    List<Desc> getAllDesc() {
        return descService.list();
    }

    @GetMapping("/info")
    List<Info> getAllInfo() {
        return infoService.list();
    }
}
