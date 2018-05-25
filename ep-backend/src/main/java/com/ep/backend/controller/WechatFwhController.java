package com.ep.backend.controller;

import com.ep.domain.component.WechatPayComponent;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.service.WechatFwhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 10:37 2018/4/23/023
 */
@Controller
@RequestMapping("auth/wechatFwh")
public class WechatFwhController extends BackendController {
    @Autowired
    private WechatFwhService wechatFwhService;

    @Autowired
    private WechatPayComponent wechatPayComponent;

    @Value("${wechat.fwh.token}")
    private String wechatFwhToken;
    @Value("${wechat.fwh.id}")
    private String wechatFwhId;

    /**
     * 自定义菜单页面
     *
     * @return
     */
    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('platform:wechatFwh:index')")
    public String menu() {
        return "wechatFwh/menu";
    }

    /**
     * 自定义菜单查询
     *
     * @return
     */
    @GetMapping("menuGet")
    @PreAuthorize("hasAnyAuthority('platform:wechatFwh:index')")
    @ResponseBody
    public ResultDo menuGet() {
        return wechatFwhService.menuGet();
    }

    /**
     * 自定义菜单创建
     *
     * @return
     */
    @PostMapping("menuCreate")
    @PreAuthorize("hasAnyAuthority('platform:wechatFwh:index')")
    @ResponseBody
    public ResultDo menuCreate(@RequestParam(value = "menuJson") String menuJson) {
        return wechatFwhService.menuCreate(menuJson);
    }

    /**
     * 自定义菜单删除
     *
     * @return
     */
    @GetMapping("menuDelete")
    @ResponseBody
    public ResultDo menuDelete() {
        return wechatFwhService.menuDelete();
    }

}
