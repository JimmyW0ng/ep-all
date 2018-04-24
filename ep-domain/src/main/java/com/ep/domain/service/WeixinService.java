package com.ep.domain.service;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.HttpClientTools;
import com.ep.common.tool.WeixinTools;
import com.ep.domain.constant.BizConstant;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 0:20 2018/4/23
 */
@Slf4j
@Service
public class WeixinService {
    //    @Autowired
//    private WechatAuthCodeRepository wechatAuthCodeRepository;
    @Value("${weixin4j.oauth.appid}")
    private String weixin4jOauthAppid;
    @Value("${weixin4j.oauth.secret}")
    private String weixin4jOauthSecret;

    /**
     * 发送客服消息
     *
     * @param accessToken
     * @param openIds
     * @param msg
     * @throws Exception
     */
    public void msgCustomSend(String accessToken, List<String> openIds, String msg) throws Exception {
        String url = String.format(BizConstant.WECHAT_URL_MSG_CUSTOM_SEND, accessToken);
        for (String openId : openIds) {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("touser", openId);
            jsonParam.put("msgtype", "text");
            JSONObject jsonText = new JSONObject();
            jsonText.put("content", msg);
            jsonParam.put("text", jsonText);
            HttpClientTools.doPost(url, jsonParam.toString());
        }
    }

    /**
     * 获取access_token
     * @return
     * @throws Exception
     */
    public String getAccessToken() throws Exception {
        String url = String.format(BizConstant.WECHAT_URL_GET_ACCESS_TOKEN, weixin4jOauthAppid, weixin4jOauthSecret);
        Map<String, String> resultMap = HttpClientTools.doGet(url);
        String accessToken = resultMap.get("access_token");
        return accessToken;
    }

    /**
     * 用户向公众号post请求
     *
     * @param requestMap
     * @return
     */
    public Map<String, String> postReq(Map<String, String> requestMap) {
        Map<String, String> responseMap = Maps.newHashMap();
        //判断请求是否事件类型 event
        if (requestMap.get("MsgType").equals(WeixinTools.MESSAGE_EVENT)) {
            if (requestMap.get("Event").equals(WeixinTools.EVENT_SUB)) {
                responseMap.put("Content", "谢谢您关注小竹马！");
                responseMap.put("MsgType", "text");
            } else if (requestMap.get("Event").equals(WeixinTools.EVENT_CLICK)
                    && requestMap.get("EventKey").equals("bind_mobile")) {
                String content = SpringComponent.messageSource("WECHAT_BIND_MOBILE_TIP");
                responseMap.put("Content", content);
                responseMap.put("MsgType", "text");

            }
        } else {
            receiveTextMsg(requestMap.get("Content"), requestMap.get("FromUserName"));
            responseMap.put("Content", "么么哒！");
            responseMap.put("MsgType", "text");
        }
        return responseMap;
    }


    public Map<String, String> receiveTextMsg(String content, String openId) {
        Map<String, String> responseMap = Maps.newHashMap();
        if (content.startsWith(BizConstant.WECHAT_TEXT_MSG_BIND_MOBILE)) {
            String mobile = content.substring(BizConstant.WECHAT_TEXT_MSG_BIND_MOBILE.length() + BizConstant.DB_NUM_ONE, content.length());
//            EpWechatAuthCodePo wechatAuthCodePo = new EpWechatAuthCodePo();
//            wechatAuthCodePo.setOpenId(openId);
//            wechatAuthCodePo.setMobile(Long.parseLong(mobile));
//            wechatAuthCodePo.setAuthCode(ValidCodeTools.generateDigitValidCode(BizConstant.DB_NUM_ZERO));
//            wechatAuthCodePo.setType(EpWechatAuthCodeType.bind);
//            wechatAuthCodeRepository.insert(wechatAuthCodePo);
            responseMap.put("Content", "验证码已发送至" + mobile + ",两分钟内有效");
            responseMap.put("MsgType", "text");
            return responseMap;
        }
        responseMap.put("Content", "");
        responseMap.put("MsgType", "text");
        return responseMap;
    }
}
