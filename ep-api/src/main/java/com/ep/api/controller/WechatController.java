package com.ep.api.controller;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.WechatTools;
import com.ep.common.tool.wechat.TokenTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @Autowired
    private WechatService wechatService;
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


    @ApiOperation(value = "登录凭证校验")
    @PostMapping("/xcx/member/auth")
    public ResultDo<String> getCaptcha(@RequestParam("code") String code) throws GeneralSecurityException {
        return wechatService.getSessionToken(code, xcxMemberAppId, xcxMemberSecret);
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
        Map<String, String> responseMap = wechatService.postReq(requestMap);
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
