package com.ep.backend.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.service.WechatFwhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 10:37 2018/4/23/023
 */
@Controller
@RequestMapping("auth/wechat")
public class PlatformWechatController extends BackendController {
    @Autowired
    private WechatFwhService wechatFwhService;
    @Value("${wechat.fwh.token}")
    private String wechatFwhToken;
    @Value("${wechat.fwh.id}")
    private String wechatFwhId;

    /**
     * 自定义菜单页面
     *
     * @return
     */
    @GetMapping("menu")
    public String menu() {
        return "wechat/menu";
    }

    /**
     * 自定义菜单查询
     *
     * @return
     */
    @GetMapping("menuGet")
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

    /**
     * 微信发送客服消息
     *
     * @return
     */
    @GetMapping("msgCustomSend")
    public void msgCustomSend() throws Exception {
        ResultDo resultDoAccessToken = wechatFwhService.getAccessToken();
        if (resultDoAccessToken.isSuccess()) {
            String accessToken = (String) resultDoAccessToken.getResult();
            String openId = "oNn9k0vtlBRPyCN7dF1l_MuDkUvY";
            wechatFwhService.msgCustomSend(accessToken, openId, "hello world");
        }
    }


}
