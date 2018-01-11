package com.ep.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by fcc on 2018/1/10.
 */
@RequestMapping("/sysUser")
@Controller
@Api(value = "后管用户")
public class SysUserController {
    /**
     * 列表
     *
     * @return
     */
    @ApiOperation(value = "列表")
    @GetMapping("/index")
    public String getCaptcha() {
        System.out.println("ccccccccc");
        return "sysUser/index";
    }

}
