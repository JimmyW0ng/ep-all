package com.ep.api.controller;

import com.ep.domain.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 微信接入控制器
 * @Author: CC.F
 * @Date: 12:20 2018/4/22
 */
@Controller
@RequestMapping("security/wechat")
public class WechatFwhController {
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

//    @GetMapping("access")
//    public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // 微信加密签名
//        String signature = request.getParameter("signature");
//        String timestamp = request.getParameter("timestamp");
//        // 随机数
//        String nonce = request.getParameter("nonce");
//        String token = wechatFwhToken;
//        //成为开发者验证
//        String echostr = request.getParameter("echostr");
//        //确认此次请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
//        if (TokenTools.checkSignature(token, signature, timestamp, nonce)) {
//            response.getWriter().write(echostr);
//        }
//    }
//
//    @PostMapping("access")
//    public void post(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        // 微信加密签名
//        String signature = request.getParameter("signature");
//        // 时间戳
//        String timestamp = request.getParameter("timestamp");
//        // 随机数
//        String nonce = request.getParameter("nonce");
//        String token = wechatFwhToken;
//        //确认此次请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
//        if (!TokenTools.checkSignature(token, signature, timestamp, nonce)) {
//            //消息不可靠，直接返回
//            response.getWriter().write("");
//            return;
//        }
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/xml");
//        Map<String, String> requestMap = WechatTools.xmlToMap(request);
//        Map<String, String> responseMap = wechatService.postReq(requestMap);
//        responseMap.put(WechatTools.PARAM_CREATETIME, String.valueOf(DateTools.getCurrentDate().getTime()));
//        responseMap.put(WechatTools.PARAM_TOUSERNAME, requestMap.get(WechatTools.PARAM_FROMUSERNAME));
//        responseMap.put(WechatTools.PARAM_FROMUSERNAME, wechatFwhId);
//        String xml = WechatTools.mapToXmlString(responseMap);
//        try {
//            response.getWriter().write(xml);
//        } catch (IOException e) {
//            response.getWriter().write("");
//        }
//    }
}
