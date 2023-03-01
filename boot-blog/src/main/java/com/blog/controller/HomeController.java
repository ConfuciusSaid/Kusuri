package com.blog.controller;

import com.blog.dto.Result;
import com.blog.dto.UserDTO;
import com.blog.pojo.Desc;
import com.blog.pojo.Info;
import com.blog.service.DescService;
import com.blog.service.InfoService;
import com.blog.service.UserService;
import com.blog.settings.DataSettings;
import com.blog.settings.RedisSettings;
import com.blog.util.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    DescService descService;

    @Autowired
    InfoService infoService;

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/desc/{id}")
    Result getDescByUserId(@PathVariable Long id,
                           @RequestParam(name = "page", defaultValue = "1") Long page,
                           @RequestParam(name = "limit", defaultValue = "10") Long limit) {
        if (limit < 0 || limit > 10) limit = 10L;
        return descService.getDescByUserId(id, page, limit);
    }

    @PostMapping("/desc")
    Result insertDesc(@RequestBody Desc body,
                      @RequestParam("kusuri_token") String token
    ) {
        String markdown = body.getMarkdown();
        if (markdown == null) return Result.fail("无效参数");

        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (descService.query().eq("user_id", user.getId()).count() >= DataSettings.DESC_COUNT) {
            return Result.fail("请勿添加超过" + DataSettings.DESC_COUNT + "条个人介绍");
        }
        if (markdown.length() > DataSettings.DESC_MARKDOWN) {
            return Result.fail("请勿传输超过" + DataSettings.DESC_MARKDOWN / 1024 + "kb的数据");
        }
        markdown = MarkdownUtils.defend(markdown);
        Desc desc = new Desc();
        desc.setUserId(user.getId());
        desc.setMarkdown(markdown);
        descService.save(desc);
        return Result.ok();
    }

    @PutMapping("/desc")
    Result updateDesc(@RequestBody Desc body,
                      @RequestParam("kusuri_token") String token) {
        Long id = body.getId();
        String markdown = body.getMarkdown();
        if (id == null || markdown == null) return Result.fail("无效参数");

        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (markdown.length() > DataSettings.DESC_MARKDOWN) {
            return Result.fail("请勿传输超过" + DataSettings.DESC_MARKDOWN / 1024 + "kb的数据");
        }
        markdown = MarkdownUtils.defend(markdown);

        if (!descService.update().eq("id", id)
                .eq("user_id", user.getId())
                .set("markdown", markdown).update()) {
            return Result.fail("目标不存在");
        }
        return Result.ok();
    }

    @DeleteMapping("/desc/{id}")
    Result deleteDesc(@PathVariable Long id,
                      @RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (!descService.update().eq("id", id)
                .eq("user_id", user.getId())
                .remove()) {
            return Result.fail("目标不存在");
        }
        return Result.ok();
    }

    @GetMapping("/info/{id}")
    Result getInfoByUserId(@PathVariable Long id) {
        return infoService.getInfoByUserId(id);
    }


    @PostMapping("/info")
    Result insertInfo(@RequestBody Info body,
                      @RequestParam("kusuri_token") String token
    ) {
        String markdown = body.getMarkdown();
        if (markdown == null) return Result.fail("无效参数");

        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (infoService.query().eq("user_id", user.getId()).count() >= DataSettings.INFO_COUNT) {
            return Result.fail("请勿添加超过" + DataSettings.INFO_COUNT + "条基本信息");
        }
        if (markdown.length() > DataSettings.INFO_MARKDOWN) {
            return Result.fail("请勿传输超过" + DataSettings.INFO_MARKDOWN / 1024 + "kb的数据");
        }
        markdown = MarkdownUtils.defend(markdown);
        Info info = new Info();
        info.setUserId(user.getId());
        info.setMarkdown(markdown);
        infoService.save(info);
        redisTemplate.delete(RedisSettings.INFO_PREFIX + user.getId());
        return Result.ok();
    }

    @PutMapping("/info")
    Result updateInfo(@RequestBody Info body,
                      @RequestParam("kusuri_token") String token) {
        Long id = body.getId();
        String markdown = body.getMarkdown();
        if (id == null || markdown == null) return Result.fail("无效参数");

        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }
        if (markdown.length() > DataSettings.INFO_MARKDOWN) {
            return Result.fail("请勿传输超过" + DataSettings.INFO_MARKDOWN / 1024 + "kb的数据");
        }
        markdown = MarkdownUtils.defend(markdown);

        if (!infoService.update().eq("id", id)
                .eq("user_id", user.getId())
                .set("markdown", markdown).update()) {
            return Result.fail("目标不存在");
        }
        redisTemplate.delete(RedisSettings.INFO_PREFIX + user.getId());
        return Result.ok();
    }

    @DeleteMapping("/info/{id}")
    Result deleteInfo(@PathVariable Long id,
                      @RequestParam("kusuri_token") String token) {
        UserDTO user = userService.checkToken(token);
        if (user == null) {
            return Result.fail("用户未登录");
        }

        if (!infoService.update().eq("id", id)
                .eq("user_id", user.getId())
                .remove()) {
            return Result.fail("目标不存在");
        }
        infoService.deleteInfoCache(user.getId());
        redisTemplate.delete(RedisSettings.INFO_PREFIX + user.getId());
        return Result.ok();
    }

}
