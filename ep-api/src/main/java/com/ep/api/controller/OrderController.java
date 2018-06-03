package com.ep.api.controller;

import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.OrderDto;
import com.ep.domain.pojo.dto.OrderInitDto;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.pojo.wechat.WechatSessionBo;
import com.ep.domain.service.OrderService;
import com.ep.domain.service.WechatXcxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.GeneralSecurityException;
import java.util.Optional;

/**
 * @Description: 订单控制类
 * @Author: J.W
 * @Date: 下午2:06 2018/1/28
 */
@Slf4j
@RequestMapping("auth/order")
@RestController
@Api(value = "api-auth-order", description = "订单接口")
public class OrderController extends ApiController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private WechatXcxService wechatXcxService;
    @Value("${wechat.xcx.member.appid}")
    private String xcxMemberAppId;
    @Value("${wechat.xcx.member.secret}")
    private String xcxMemberSecret;

    @ApiOperation(value = "立即订单初始化信息")
    @PostMapping("/init")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrderInitDto> init(@RequestParam("courseId") Long courseId) {
        Optional<EpMemberPo> optional = super.getCurrentUser();
        return orderService.initInfo(optional.get().getId(), courseId);
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrderDto> order(@RequestParam("childId") Long childId,
                                    @RequestParam("classId") Long classId) {
        Optional<EpMemberPo> optional = super.getCurrentUser();
        return orderService.order(optional.get().getId(), childId, classId);
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("/newOrder")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrderDto> order(@RequestParam("childId") Long childId,
                                    @RequestParam("classId") Long classId,
                                    @RequestParam("formId") String formId,
                                    @RequestParam("sessionToken") String sessionToken
    ) throws GeneralSecurityException {
        Optional<EpMemberPo> optional = super.getCurrentUser();
        // 微信session
        WechatSessionBo sessionBo;
        try {
            sessionBo = wechatXcxService.getSessionObject(sessionToken);
        } catch (GeneralSecurityException e) {
            log.error("【小程序创建订单】sessionToken失败, sessionToken={}", sessionToken, e);
            return ResultDo.build(MessageCode.ERROR_WECHAT_SESSION_TOKEN_CONTENT);
        }
        if (sessionBo == null || sessionBo.getOpenid() == null) {
            log.error("【小程序创建订单】sessionToken异常, sessionToken={}, sessionBo={}", sessionToken, sessionBo);
            return ResultDo.build(MessageCode.ERROR_WECHAT_SESSION_TOKEN_CONTENT);
        }
        return orderService.order(optional.get().getId(), childId, classId, formId, sessionBo.getOpenid());
    }

}
