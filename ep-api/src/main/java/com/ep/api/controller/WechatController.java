package com.ep.api.controller;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.wechat.TokenTools;
import com.ep.common.tool.wechat.WechatTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.service.WechatFwhService;
import com.ep.domain.service.WechatPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

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
    /**
     * 微信服务号token
     */
    @Value("${wechat.fwh.token}")
    private String wechatFwhToken;
    /**
     * 微信服务号的微信号
     */
    @Value("${wechat.fwh.id}")
    private String wechatFwhId;
    /**
     * 微信服务号appid
     */
    @Value("${wechat.fwh.appid}")
    private String wechatFwhAppid;
    /**
     * 微信服务号secret
     */
    @Value("${wechat.fwh.secret}")
    private String wechatFwhSecret;
    @Autowired
    private WechatFwhService wechatFwhService;
    @Autowired
    private WechatPayService wechatPayService;

    @ApiOperation(value = "登录凭证校验")
    @PostMapping("/xcx/member/auth")
    public ResultDo<String> getCaptcha(@RequestParam("code") String code) throws GeneralSecurityException {
        return wechatFwhService.getSessionToken(code, xcxMemberAppId, xcxMemberSecret);
    }

    @ApiOperation(value = "小程序统一下单")
    @PostMapping("/xcx/pay/unifiedorder")
    public ResultDo<String> xcxPayUnifiedorder(
            @ApiParam(value = "商品描述(128)", required = true) @RequestParam("body") String body,
            @ApiParam(value = "商品详情(6000)", required = false) @RequestParam("detail") String detail,
            @ApiParam(value = "附加数据(127)", required = false) @RequestParam("attach") String attach,
            @ApiParam(value = "商户订单号(32)", required = true) @RequestParam("out_trade_no") String out_trade_no,
            @ApiParam(value = "标价金额(单位为分)", required = true) @RequestParam("total_fee") int total_fee,
            @ApiParam(value = "终端IP(16),APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP", required = true)
            @RequestParam("spbill_create_ip（16）") String spbill_create_ip,
            @ApiParam(value = "交易起始时间（14）格式为yyyyMMddHHmmss", required = false) @RequestParam("time_start") String time_start,
            @ApiParam(value = "交易结束时间（14）格式为yyyyMMddHHmmss", required = false) @RequestParam("time_expire") String time_expire,
            @ApiParam(value = "用户标识（128）", required = true) @RequestParam("openid") String openid
    ) throws GeneralSecurityException {
        return ResultDo.build();
//        return wechatPayService.xcxUnifiedorder(body,detail,attach,out_trade_no,total_fee,spbill_create_ip,time_start,time_expire,openid);
    }

    @GetMapping("fwh/access")
    public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 微信加密签名
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        String token = wechatFwhToken;
        //成为开发者验证
        String echostr = request.getParameter("echostr");
        //确认此次请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        if (TokenTools.checkSignature(token, signature, timestamp, nonce)) {
            response.getWriter().write(echostr);
        }
    }

    @PostMapping("fwh/access")
    public void post(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        String token = wechatFwhToken;
        //确认此次请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        if (!TokenTools.checkSignature(token, signature, timestamp, nonce)) {
            //消息不可靠，直接返回
            response.getWriter().write("");
            return;
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        Map<String, String> requestMap = WechatTools.xmlToMap(request);
        Map<String, String> responseMap = wechatFwhService.postReq(requestMap);
        responseMap.put(WechatTools.PARAM_CREATETIME, String.valueOf(DateTools.getCurrentDate().getTime()));
        responseMap.put(WechatTools.PARAM_TOUSERNAME, requestMap.get(WechatTools.PARAM_FROMUSERNAME));
        responseMap.put(WechatTools.PARAM_FROMUSERNAME, wechatFwhId);
        String xml = WechatTools.mapToXmlString(responseMap);
        try {
            response.getWriter().write(xml);
        } catch (IOException e) {
            response.getWriter().write("");
        }
    }

}
