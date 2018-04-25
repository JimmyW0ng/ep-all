package com.ep.domain.service;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.HttpClientTools;
import com.ep.common.tool.ValidCodeTools;
import com.ep.common.tool.WechatTools;
import com.ep.domain.component.DictComponent;
import com.ep.domain.component.QcloudsmsComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.pojo.po.EpMessageCaptchaPo;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.repository.MemberRepository;
import com.ep.domain.repository.MessageCaptchaRepository;
import com.ep.domain.repository.domain.enums.EpMemberStatus;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaScene;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaType;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 0:20 2018/4/23
 */
@Slf4j
@Service
public class WechatService {
    @Autowired
    private QcloudsmsComponent qcloudsmsComponent;
    @Autowired
    private MessageCaptchaRepository messageCaptchaRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DictComponent dictComponent;
    @Value("${wechat.fwh.appid}")
    private String wechatFwhAppid;
    @Value("${wechat.fwh.secret}")
    private String wechatFwhSecret;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 发送客服消息
     *
     * @param accessToken
     * @param openId
     * @param msg
     * @throws Exception
     */
    public ResultDo msgCustomSend(String accessToken, String openId, String msg) throws Exception {
        String url = String.format(BizConstant.WECHAT_URL_MSG_CUSTOM_SEND, accessToken);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("touser", openId);
        jsonParam.put("msgtype", "text");
        JSONObject jsonText = new JSONObject();
        jsonText.put("content", msg);
        jsonParam.put("text", jsonText);
        Map<String, Object> responseMap = HttpClientTools.doPost(url, jsonParam.toString());
        if (responseMap.get("errcode").toString().equals(BizConstant.WECHAT_SUCCESS_CODE)) {
            return ResultDo.build();
        } else {
            return ResultDo.build().setSuccess(false).setError(responseMap.get("errcode").toString());
        }
    }

    /**
     * 获取access_token
     * @return
     * @throws Exception
     */
    public ResultDo getAccessToken() {
        String url = String.format(BizConstant.WECHAT_URL_GET_ACCESS_TOKEN, wechatFwhAppid, wechatFwhSecret);
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            Map<String, Object> responseMap = responseEntity.getBody();
            if (null != responseMap.get("access_token")) {
                String accessToken = (String) responseMap.get("access_token");
                return ResultDo.build().setResult(accessToken);
            } else {
                log.error("[微信]获取access_token失败，errcode={}，errmsg={}。", responseMap.get("errcode"), responseMap.get("errmsg"));
                return ResultDo.build().setSuccess(false).setError(responseMap.get("errcode").toString())
                        .setErrorDescription(responseMap.get("errmsg").toString());
            }
        }
        return ResultDo.build(MessageCode.ERROR_WECHAT_API_CONNECTION);
    }

    /**
     * 自定义菜单查询
     *
     * @return
     */
    public ResultDo menuGet() {
        ResultDo resultDoAccessToken = this.getAccessToken();
        if (!resultDoAccessToken.isSuccess()) {
            return ResultDo.build().setSuccess(false);
        }
        String accessToken = (String) resultDoAccessToken.getResult();
        String url = String.format(BizConstant.WECHAT_URL_MENU_GET, accessToken);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return ResultDo.build().setResult(responseEntity.getBody());
        }
        return ResultDo.build(MessageCode.ERROR_WECHAT_API_CONNECTION);
    }

    /**
     * 自定义菜单创建
     *
     * @return
     */
    public ResultDo menuCreate(String menuJson) {
        ResultDo resultDoAccessToken = this.getAccessToken();
        if (!resultDoAccessToken.isSuccess()) {
            return ResultDo.build().setSuccess(false);
        }
        String accessToken = (String) resultDoAccessToken.getResult();
        String url = String.format(BizConstant.WECHAT_URL_MENU_CREATE, accessToken);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, menuJson, String.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return ResultDo.build().setResult(responseEntity.getBody());
        }
        return ResultDo.build(MessageCode.ERROR_WECHAT_API_CONNECTION);
    }

    /**
     * 自定义菜单删除
     *
     * @return
     */
    public ResultDo menuDelete() {
        ResultDo resultDoAccessToken = this.getAccessToken();
        if (!resultDoAccessToken.isSuccess()) {
            return ResultDo.build().setSuccess(false);
        }
        String accessToken = (String) resultDoAccessToken.getResult();
        String url = String.format(BizConstant.WECHAT_URL_MENU_DELETE, accessToken);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return ResultDo.build().setResult(responseEntity.getBody());
        }
        return ResultDo.build(MessageCode.ERROR_WECHAT_API_CONNECTION);
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
        if (requestMap.get("MsgType").equals(WechatTools.MESSAGE_EVENT)) {
            if (requestMap.get("Event").equals(WechatTools.EVENT_SUB)) {
                responseMap.put("Content", "谢谢您关注小竹马！");
                responseMap.put("MsgType", "text");
            } else if (requestMap.get("Event").equals(WechatTools.EVENT_CLICK)
                    && requestMap.get("EventKey").equals("bind_mobile")) {
                String content = SpringComponent.messageSource(BizConstant.WECHAT_BIND_MOBILE_TIP);
                responseMap.put("Content", content);
                responseMap.put("MsgType", "text");

            }
        } else if (requestMap.get("MsgType").equals(WechatTools.MESSAGE_TEXT)) {
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
        //微信用户绑定手机号请求
        if (contentParams[0].equals(BizConstant.WECHAT_TEXT_MSG_BIND_MOBILE)) {
            String mobile = contentParams[1];
            return this.receiveTextMsgBindMobile(mobile);
        }
        //微信用户通过验证码和手机号绑定请求
        if (Pattern.matches(BizConstant.WECHAT_PATTERN_CAPTCHA, contentParams[0])
                && Pattern.matches(BizConstant.PATTERN_MOBILE, contentParams[1])) {
            return this.receiveTextMsgCaptchaMobile(contentParams[0], contentParams[1]);

        }
        responseMap.put("Content", "么么哒");
        responseMap.put("MsgType", "text");
        return responseMap;
    }

    /**
     * 接收验证码#绑定号码
     *
     * @param moblie
     */
    private Map<String, String> receiveTextMsgCaptchaMobile(String captcha, String moblie) {
        Map<String, String> responseMap = Maps.newHashMap();
        EpMessageCaptchaPo messageCaptchaPo = messageCaptchaRepository.getBySourceIdAndCaptchaCode(Long.parseLong(moblie), EpMessageCaptchaCaptchaType.short_msg
                , EpMessageCaptchaCaptchaScene.wx_bind_mobile, captcha);
        if (messageCaptchaPo != null && DateTools.getTimeIsAfter(messageCaptchaPo.getExpireTime(), DateTools.getCurrentDateTime())) {
            EpMemberPo epMemberPo = new EpMemberPo();
            epMemberPo.setMobile(Long.parseLong(moblie));
            epMemberPo.setStatus(EpMemberStatus.normal);
            memberRepository.insert(epMemberPo);
            responseMap.put("Content", "您已绑定成功！");
            responseMap.put("MsgType", "text");
            return responseMap;
        }
        responseMap.put("Content", "绑定失败！");
        responseMap.put("MsgType", "text");
        return responseMap;
    }

    /**
     * 接收绑定号码
     *
     * @param mobile
     */
    private Map<String, String> receiveTextMsgBindMobile(String mobile) {
        Map<String, String> responseMap = Maps.newHashMap();
        EpMemberPo epMemberPo = memberRepository.getByMobile(Long.parseLong(mobile));
        //判断是否已绑定
        if (epMemberPo != null && !epMemberPo.getDelFlag()) {
            responseMap.put("Content", "该手机号已绑定！");
            responseMap.put("MsgType", "text");
            return responseMap;
        }
        EpSystemDictPo dictPo = dictComponent.getByGroupNameAndKey(BizConstant.DICT_GROUP_QCLOUDSMS, BizConstant.WECHAT_BIND_MOBILE_CAPTCHA);
        //短信模板id
        int templateId = Integer.parseInt(dictPo.getValue());
        String[] templateParams = new String[1];
        //验证码
        String captcha = ValidCodeTools.generateDigitValidCode(BizConstant.DB_NUM_ZERO);
        templateParams[0] = captcha;
        //发送短信
        qcloudsmsComponent.singleSend(templateId, mobile, templateParams);
        EpMessageCaptchaPo epMessageCaptchaPo = new EpMessageCaptchaPo();
        epMessageCaptchaPo.setCaptchaType(EpMessageCaptchaCaptchaType.short_msg);
        epMessageCaptchaPo.setSourceId(Long.parseLong(mobile));
        epMessageCaptchaPo.setCaptchaContent(captcha);
        epMessageCaptchaPo.setCaptchaScene(EpMessageCaptchaCaptchaScene.wx_bind_mobile);
        epMessageCaptchaPo.setExpireTime(DateTools.addMinuteTimestamp(DateTools.getCurrentDate(), BizConstant.DB_NUM_TWO));
        messageCaptchaRepository.insert(epMessageCaptchaPo);
        //微信回复
        String responseContent = String.format(SpringComponent.messageSource(BizConstant.WECHAT_CAPTCHA_BIND_MOBILE_TIP), mobile);
        responseMap.put("Content", responseContent);
        responseMap.put("MsgType", "text");
        return responseMap;
    }

}
