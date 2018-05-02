package com.ep.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 443根节点路由重定向到域名首页
 * @Author: J.W
 * @Date: 下午4:41 2018/1/9
 */
@Slf4j
@RequestMapping
@Controller
public class IndexController extends ApiController {

    @GetMapping("/")
    public String getCaptcha() {
        return "redirect:http://www.qwwlkj.com";
    }

}
