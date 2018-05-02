package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.GeneralSecurityException;

/**
 * @Description: 微信接口
 * @Author: J.W
 * @Date: 下午4:41 2018/1/9
 */
@Slf4j
@RequestMapping("security/wechat")
@RestController
@Api(value = "api-security", description = "api开放接口")
public class WechatController extends ApiController {

    @Value("${wechat.xcx.member.appid}")
    private String xcxMemberAppId;
    @Value("${wechat.xcx.member.secret}")
    private String xcxMemberSecret;

    @Autowired
    private WechatService wechatService;

    @ApiOperation(value = "登录凭证校验")
    @PostMapping("/xcx/member/auth")
    public ResultDo<String> getCaptcha(@RequestParam("code") String code) throws GeneralSecurityException {
        return wechatService.getSessionToken(code, xcxMemberAppId, xcxMemberSecret);
    }

}
