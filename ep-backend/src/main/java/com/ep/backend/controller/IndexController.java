package com.ep.backend.controller;

import com.ep.domain.service.SystemMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by fcc on 2018/1/17.
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private SystemMenuService systemMenuService;

    /**
     * 后台首页，左侧菜单
     *
     * @return
     */
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }

    @ApiOperation(value = "首页")
    @GetMapping("/index1")
    public String index1(Model model) {
        return "index";
    }
}
