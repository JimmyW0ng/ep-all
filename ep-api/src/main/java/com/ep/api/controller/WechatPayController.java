package com.ep.api.controller;

import com.ep.domain.component.WechatPayComponent;
import com.ep.domain.pojo.ResultDo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author J.W
 * @Date: 上午 11:17 2018/5/8 0008
 */
@Slf4j
@RequestMapping("security/wechat/pay")
@RestController
@Api(value = "api-security-wechat-pay", description = "api微信支付接口")
public class WechatPayController {

    @Autowired
    private WechatPayComponent wechatPayComponent;

    @ApiOperation(value = "微信支付获取沙箱环境秘钥")
    @PostMapping("/pay/sandbox/key")
    public ResultDo getCaptcha() {
        return wechatPayComponent.getSandboxSignkey();
    }

}
