package com.ep.backend.controller;

import com.ep.common.tool.HttpClientTools;
import com.ep.common.tool.WeixinTools;
import com.ep.domain.service.WeixinService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.weixin4j.spi.HandlerFactory;
import org.weixin4j.spi.IMessageHandler;
import org.weixin4j.util.TokenUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 12:20 2018/4/22
 */
@Controller
@RequestMapping("security/weixin/jieru")
public class WeixinJieruController {
    @Autowired
    private WeixinService weixinService;


    //开发者接入验证
    @RequestMapping(method = RequestMethod.GET)
    public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //消息来源可靠性验证
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");       // 随机数
        //Token为weixin4j.properties中配置的Token
        String token = TokenUtil.get();
        //1.验证消息真实性
        //http://mp.weixin.qq.com/wiki/index.php?title=验证消息真实性
        //成为开发者验证
        String echostr = request.getParameter("echostr");
        //确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        if (TokenUtil.checkSignature(token, signature, timestamp, nonce)) {
            response.getWriter().write(echostr);
        }
    }

    //接收微信消息
    @RequestMapping(method = RequestMethod.POST)
    public void post(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //消息来源可靠性验证
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");       // 随机数

        //Token为weixin4j.properties中配置的Token
        String token = TokenUtil.get();
        //确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        if (!TokenUtil.checkSignature(token, signature, timestamp, nonce)) {
            //消息不可靠，直接返回
            response.getWriter().write("");
            return;
        }
        //用户每次向公众号发送消息、或者产生自定义菜单点击事件时，响应URL将得到推送


        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        PrintWriter out = response.getWriter();
//        try {
//            Map<String,String> requestMap=WeixinTools.xmlToMap(request);
//
//            //判断请求是否事件类型 event
//            if(requestMap.get("MsgType").equals(WeixinTools.MESSAGE_EVENT)){
//                if(requestMap.get("Event").equals(WeixinTools.EVENT_SUB)){
//                    out.print("谢谢您关注小竹马！");
//                    //todo插入数据库
//               }
//            }else{
//                out.print("么么哒！");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            out.close();
//        }


        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
            //获取POST流
            ServletInputStream in = request.getInputStream();
            //非注解方式，依然采用消息处理工厂模式调用
            IMessageHandler messageHandler = HandlerFactory.getMessageHandler();
            //处理输入消息，返回结果
//                String xml = messageHandler.invoke(in);
            String xml = "<xml><ToUserName><![CDATA[oNn9k0vtlBRPyCN7dF1l_MuDkUvY]]></ToUserName><FromUserName><![CDATA[gh_e4575a7e36bc]]></FromUserName><CreateTime>1524411115233</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[你的消息已经收到！]]></Content></xml>";
//
            System.out.println(xml);

            Map<String, String> requestMap = WeixinTools.xmlToMap(request);
//                requestMap.remove("MsgId");
//                String xml1=WeixinTools.mapToXmlString(requestMap);
//                System.out.println(xml1);
//                JSONObject jsonParam = new JSONObject();
//                jsonParam.put("touser", requestMap.get(""));
//                jsonParam.put("msgtype", "text");
//
//                JSONObject jsonText = new JSONObject();
//                jsonText.put("content",str);
//                jsonParam.put("text", jsonText);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("ToUserName", requestMap.get("ToUserName"));
            jsonParam.put("FromUserName", requestMap.get("FromUserName"));
            jsonParam.put("MsgType", requestMap.get("MsgType"));
            jsonParam.put("CreateTime", requestMap.get("CreateTime"));
            jsonParam.put("Content", requestMap.get("Content"));
            xml = "<xml><Content><![CDATA[q]]></Content><CreateTime><![CDATA[1524412124]]></CreateTime><ToUserName><![CDATA[oNn9k0vtlBRPyCN7dF1l_MuDkUvY]]></ToUserName>\n" +
                    "<FromUserName><![CDATA[gh_e4575a7e36bc]]></FromUserName>\n" +
                    "<MsgType><![CDATA[text]]></MsgType>\n" +
                    "</xml>";
            response.getWriter().write(xml);
//                }


        } catch (IOException ex) {
            response.getWriter().write("");
        }
//        }


    }

    @GetMapping("send")
    @ResponseBody
    public String send(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String accessToken = weixinService.getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;
        String openId = "oNn9k0vtlBRPyCN7dF1l_MuDkUvY";
        String str = "hello world";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("touser", openId);
        jsonParam.put("msgtype", "text");
        JSONObject jsonText = new JSONObject();
        jsonText.put("content", str);
        jsonParam.put("text", jsonText);
        return HttpClientTools.doPost(url, jsonParam.toString());

    }
}
