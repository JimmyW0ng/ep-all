package com.ep.api.controller;

import com.ep.api.security.ApiSecurityAuthComponent;
import com.ep.common.tool.IpTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.ApiLoginDto;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaScene;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaType;
import com.ep.domain.service.MessageCaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: api开放控制类
 * @Author: J.W
 * @Date: 下午4:41 2018/1/9
 */
@RequestMapping("security/api")
@RestController
@Api(value = "api-security", description = "api开放接口")
public class SecurityController extends ApiController {

    @Autowired
    private MessageCaptchaService messageCaptchaService;

    @Autowired
    private ApiSecurityAuthComponent securityAuthComponent;

    @ApiOperation(value = "获取登录短信验证码")
    @PostMapping("/captcha")
    public ResultDo getCaptcha(@RequestParam("mobile") Long mobile,
                               @RequestParam("clientId") String clientId,
                               @RequestParam("clientSecret") String clientSecret,
                               HttpServletRequest request
    ) {
        ResultDo resultDo = securityAuthComponent.checkPrincipal(clientId, clientSecret);
        if (resultDo.isError()) {
            return resultDo;
        }
        return messageCaptchaService.getCaptcha(mobile,
                EpMessageCaptchaCaptchaType.short_msg,
                EpMessageCaptchaCaptchaScene.login,
                IpTools.getIpAddr(request));
    }

    @ApiOperation(value = "获取前台token")
    @PostMapping("/token")
    public ResultDo<ApiLoginDto> login(@RequestParam(value = "mobile") Long mobile,
                                       @RequestParam(value = "code") String code,
                                       @RequestParam(value = "captcha") String captcha,
                                       @RequestParam(value = "clientId") String clientId,
                                       @RequestParam(value = "clientSecret") String clientSecret
    ) {
        return securityAuthComponent.loginFromApi(mobile.toString(), code, captcha, clientId, clientSecret);
    }

}
