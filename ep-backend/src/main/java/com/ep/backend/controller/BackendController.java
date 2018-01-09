package com.ep.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: backend开放控制类
 * @Author: J.W
 * @Date: 下午4:41 2018/1/9
 */
@RequestMapping("security/backend")
@Controller
@Api(value = "api-security", description = "backend开放接口")
public class BackendController {

    /**
     * 获取登录短信验证码
     *
     * @return
     */
    @ApiOperation(value = "后台登录页")
    @GetMapping("/login")
    public String getCaptcha() {
        return "login";
    }

}
