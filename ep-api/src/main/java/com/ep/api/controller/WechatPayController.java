package com.ep.api.controller;

import com.ep.common.tool.IpTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.wechat.WechatSessionBo;
import com.ep.domain.service.OrderService;
import com.ep.domain.service.WechatXcxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;

/**
 * @Description: 微信支付控制器
 * @Author J.W
 * @Date: 上午 11:17 2018/5/8 0008
 */
@Slf4j
@RequestMapping("auth/wechat/pay")
@RestController
@Api(value = "api-security-wechat-pay", description = "api微信支付")
public class WechatPayController extends ApiController {

    @Autowired
    private WechatXcxService wechatXcxService;
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "统一下单")
    @PostMapping("/unifiedorder")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo unifiedorder(HttpServletRequest request,
                                 @RequestParam("sessionToken") String sessionToken,
                                 @RequestParam("orderId") Long orderId) throws Exception {
        log.info("【小程序报名支付】开始, sessionToken={}, orderId={}", sessionToken, orderId);
        // 当前用户
        Long memberId = super.getCurrentUser().get().getId();
        // 微信session
        WechatSessionBo sessionBo;
        try {
            sessionBo = wechatXcxService.getSessionObject(sessionToken);
        } catch (GeneralSecurityException e) {
            log.error("【小程序报名支付】sessionToken失败, sessionToken={}", sessionToken, e);
            return ResultDo.build(MessageCode.ERROR_WECHAT_SESSION_TOKEN_CONTENT);
        }
        if (sessionBo == null || sessionBo.getOpenid() == null) {
            log.error("【小程序报名支付】sessionToken异常, sessionToken={}, sessionBo={}", sessionToken, sessionBo);
            return ResultDo.build(MessageCode.ERROR_WECHAT_SESSION_TOKEN_CONTENT);
        }
        // 微信支付生成预支付订单
        return orderService.prePayByWechatPay(memberId, sessionBo.getOpenid(), orderId, IpTools.getIpAddr(request));
    }

}
