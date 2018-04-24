package com.ep.domain.service;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.HttpClientTools;
import com.ep.common.tool.ValidCodeTools;
import com.ep.common.tool.WeixinTools;
import com.ep.domain.component.DictComponent;
import com.ep.domain.component.QcloudsmsComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpMessageCaptchaPo;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.repository.MessageCaptchaRepository;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaScene;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaType;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 0:20 2018/4/23
 */
@Slf4j
@Service
public class WeixinService {
    @Autowired
    private QcloudsmsComponent qcloudsmsComponent;
    @Autowired
    private MessageCaptchaRepository messageCaptchaRepository;
    @Autowired
    private DictComponent dictComponent;
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
                String content = SpringComponent.messageSource(BizConstant.WECHAT_BIND_MOBILE_TIP);
                responseMap.put("Content", content);
                responseMap.put("MsgType", "text");

            }
        } else if (requestMap.get("MsgType").equals(WeixinTools.MESSAGE_TEXT)) {
            //接收文本信息
            responseMap = this.receiveTextMsg(requestMap.get("Content"), requestMap.get("FromUserName"));
        } else {
            //其他
            responseMap.put("Content", "么么哒！");
            responseMap.put("MsgType", "text");
        }
        return responseMap;
    }

    /**
     * 接收文本信息
     *
     * @param content
     * @param openId
     * @return
     */
    public Map<String, String> receiveTextMsg(String content, String openId) {
        Map<String, String> responseMap = Maps.newHashMap();
        String[] contentParams = content.split(BizConstant.WECHAT_TEXT_MSG_SPLIT);
        //微信用户绑定手机号请求，向手机发送验证码
        if (contentParams[0].equals(BizConstant.WECHAT_TEXT_MSG_BIND_MOBILE)) {
            String mobile = contentParams[1];
            this.receiveTextMsgBindMobile(mobile);
            String responseContent = String.format(SpringComponent.messageSource(BizConstant.WECHAT_CAPTCHA_BIND_MOBILE_TIP), mobile);
            responseMap.put("Content", responseContent);
            responseMap.put("MsgType", "text");
            return responseMap;
        }
        if (Pattern.matches(BizConstant.WECHAT_PATTERN_CAPTCHA, content)) {

        }
        responseMap.put("Content", "");
        responseMap.put("MsgType", "text");
        return responseMap;
    }

    /**
     * 接收绑定号码信息,向手机发送验证码
     *
     * @param moblie
     */
    private void receiveTextMsgBindMobile(String moblie) {
        EpSystemDictPo dictPo = dictComponent.getByGroupNameAndKey(BizConstant.DICT_GROUP_QCLOUDSMS, BizConstant.WECHAT_BIND_MOBILE_CAPTCHA);
        //短信模板id
        int templateId = Integer.parseInt(dictPo.getValue());
        String[] templateParams = new String[1];
        //验证码
        templateParams[0] = ValidCodeTools.generateDigitValidCode(BizConstant.DB_NUM_ZERO);
        //发送短信
        qcloudsmsComponent.singleSend(templateId, moblie, templateParams);
    }

    public ResultDo wechatBindMobile(String code, Long mobile) {
        EpMessageCaptchaPo messageCaptchaPo = messageCaptchaRepository
                .getBySourceIdAndCaptchaCode(mobile, EpMessageCaptchaCaptchaType.short_msg, EpMessageCaptchaCaptchaScene.wx_bind_mobile, code);
        if (messageCaptchaPo != null && DateTools.getTimeIsAfter(messageCaptchaPo.getExpireTime(), DateTools.getCurrentDateTime())) {
            System.out.println("member插入数据");
        }
        return ResultDo.build();
    }
}
