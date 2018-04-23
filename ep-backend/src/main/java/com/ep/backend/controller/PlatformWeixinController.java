package com.ep.backend.controller;

import com.ep.domain.service.WeixinService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 10:37 2018/4/23/023
 */
@Controller
@RequestMapping("auth/weixin")
public class PlatformWeixinController {
    @Autowired
    private WeixinService weixinService;
    @Value("${weixin4j.token}")
    private String weixinToken;
    @Value("${weixin.id}")
    private String weixinId;

    /**
     * 微信发送客服消息
     *
     * @return
     */
    @GetMapping("msgCustomSend")
    public void msgCustomSend() throws Exception {
        String accessToken = weixinService.getAccessToken();
        List<String> openIds = Lists.newArrayList();
        openIds.add("oNn9k0vtlBRPyCN7dF1l_MuDkUvY");
        weixinService.msgCustomSend(accessToken, openIds, "hello world");
    }
}
