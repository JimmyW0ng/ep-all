package com.ep.domain.service;

import com.ep.common.tool.HttpClientTools;
import com.ep.common.tool.WeixinTools;
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
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;
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
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid=" + weixin4jOauthAppid + "&secret=" + weixin4jOauthSecret;
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
    public String postReq(Map<String, String> requestMap) {
        //判断请求是否事件类型 event
        if (requestMap.get("MsgType").equals(WeixinTools.MESSAGE_EVENT)) {
            if (requestMap.get("Event").equals(WeixinTools.EVENT_SUB)) {

                return "谢谢您关注小竹马！";
                //todo插入数据库
            } else {
                return null;
            }
        } else {
            return "么么哒！";
        }
    }
}
