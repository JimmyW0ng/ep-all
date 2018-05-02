package com.ep.domain.service;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.ValidCodeTools;
import com.ep.common.tool.WechatTools;
import com.ep.domain.component.DictComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.event.QcloudsmsEventBo;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.GeneralSecurityException;
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

    @Value("${wechat.fwh.appid}")
    private String wechatFwhAppid;
    @Value("${wechat.fwh.secret}")
    private String wechatFwhSecret;
    @Autowired
    private MessageCaptchaRepository messageCaptchaRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DictComponent dictComponent;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 微信登录凭证校验
     *
     * @param code
     * @return
     * @throws GeneralSecurityException
     */
    public ResultDo<String> getSessionToken(String code, String appid, String secret) throws GeneralSecurityException {
        ResultDo<String> resultDo = ResultDo.build();
        String url = String.format(BizConstant.WECHAT_URL_SESSION, appid, secret, code);
        return resultDo;
    }

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
        jsonParam.put(WechatTools.PARAM_TOUSER, openId);
        jsonParam.put("msgtype", WechatTools.MSGTYPE_TEXT);
        JSONObject jsonText = new JSONObject();
        jsonText.put("content", msg);
        jsonParam.put("text", jsonText);
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, jsonParam.toString(), HashMap.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            Map<String, Object> responseMap = responseEntity.getBody();
            if (responseMap.get("errcode").toString().equals(BizConstant.WECHAT_SUCCESS_CODE)) {
                return ResultDo.build();
            } else {
                return ResultDo.build().setSuccess(false).setError(responseMap.get("errcode").toString());
            }
        }
        return ResultDo.build(MessageCode.ERROR_WECHAT_API_CONNECTION);
    }

    /**
     * 获取access_token,先从从数据库获取再调用微信服务号接口
     *
     * @return
     */
    public ResultDo getAccessToken() {
        EpSystemDictPo dictPo = dictComponent.getByGroupNameAndKey(BizConstant.DICT_GROUP_WECHAT, BizConstant.DICT_KEY_WECHAT_FWH_ACCESS_TOKEN);
        //本地access_token是否过期
        if (dictPo != null && DateTools.addMinute(DateTools.getCurrentDate(), BizConstant.WECHAT_SESSION_TIME_OUT_M).before(dictPo.getUpdateAt())) {
            return ResultDo.build().setResult(dictPo.getValue());
        }
        //本地access_token过期调用微信接口
        String url = String.format(BizConstant.WECHAT_URL_ACCESS_TOKEN, wechatFwhAppid, wechatFwhSecret);
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            Map<String, Object> responseMap = responseEntity.getBody();
            if (null != responseMap.get(WechatTools.PARAM_ACCESSTOKEN)) {
                String accessToken = (String) responseMap.get(WechatTools.PARAM_ACCESSTOKEN);
                return ResultDo.build().setResult(accessToken);
            } else {
                log.error("[微信]调用微信接口，获取access_token失败，errcode={}，errmsg={}。", responseMap.get(WechatTools.PARAM_ERRCODE), responseMap.get(WechatTools.PARAM_ERRMSG));
                return ResultDo.build().setSuccess(false).setError(responseMap.get(WechatTools.PARAM_ERRCODE).toString())
                        .setErrorDescription(responseMap.get(WechatTools.PARAM_ERRMSG).toString());
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
        if (requestMap.get(WechatTools.PARAM_MSGTYPE).equals(WechatTools.MSGTYPE_EVENT)) {
            //请求为事件类型 event
            if (requestMap.get(WechatTools.PARAM_EVENT).equals(WechatTools.EVENT_SUB)) {
                //事件类型为关注 subscribe
                responseMap = this.receiveEventSubscribe();
            } else if (requestMap.get(WechatTools.PARAM_EVENT).equals(WechatTools.EVENT_CLICK)) {
                //事件类型为点击 click
                responseMap = this.receiveEventClick(requestMap.get("EventKey"));
            }
        } else if (requestMap.get(WechatTools.PARAM_MSGTYPE).equals(WechatTools.MSGTYPE_TEXT)) {
            //请求为文本类型
            responseMap = this.receiveText(requestMap.get(WechatTools.PARAM_CONTENT));
        } else {
            //请求为其他类型
            responseMap.put(WechatTools.PARAM_CONTENT, "么么哒！");
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
        }
        return responseMap;
    }

    /**
     * 接收事件(类型为关注)
     *
     * @return
     */
    public Map<String, String> receiveEventSubscribe() {
        Map<String, String> responseMap = Maps.newHashMap();
        responseMap.put(WechatTools.PARAM_CONTENT, "谢谢您关注小竹马！");
        responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
        return responseMap;
    }

    /**
     * 接收事件(类型为点击)
     *
     * @param eventKey
     * @return
     */
    public Map<String, String> receiveEventClick(String eventKey) {
        Map<String, String> responseMap = Maps.newHashMap();
        if (BizConstant.WECHAT_EVENTKEY_BIND_MOBILE.equals(eventKey)) {
            String content = SpringComponent.messageSource(BizConstant.WECHAT_BIND_MOBILE_TIP);
            responseMap.put(WechatTools.PARAM_CONTENT, content);
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            return responseMap;
        }
        return this.defaultResponse();
    }

    /**
     * 接收文本
     *
     * @param content
     * @return
     */
    public Map<String, String> receiveText(String content) {
        Map<String, String> responseMap = Maps.newHashMap();
        //非法输入多个###
        if (Pattern.matches(BizConstant.PATTERN_ILLEGAL_SPLIT, content)) {
            return this.defaultResponse();
        }
        String[] contentParams = content.split(BizConstant.WECHAT_TEXT_MSG_SPLIT);
        //微信用户绑定手机号请求
        if (contentParams[0].equals(BizConstant.WECHAT_TEXT_MSG_BIND_MOBILE)) {
            if (contentParams.length == BizConstant.DB_NUM_TWO) {
                String mobile = contentParams[1];
                return this.receiveTextMsgBindMobile(mobile);
            } else {
                return this.defaultResponse();
            }
        }
        //微信用户通过验证码和手机号绑定请求
        if (Pattern.matches(BizConstant.WECHAT_PATTERN_CAPTCHA, contentParams[0])
                && Pattern.matches(BizConstant.PATTERN_MOBILE, contentParams[1])
                && contentParams.length == BizConstant.DB_NUM_TWO) {
            return this.receiveTextMsgCaptchaMobile(contentParams[0], contentParams[1]);

        }
        responseMap.put(WechatTools.PARAM_CONTENT, "么么哒！");
        responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
        return responseMap;
    }

    /**
     * 接收验证码#绑定号码
     *
     * @param moblie
     */
    private Map<String, String> receiveTextMsgCaptchaMobile(String captcha, String moblie) {
        Map<String, String> responseMap = Maps.newHashMap();
        EpMemberPo oldMemberPo = memberRepository.getByMobile(Long.parseLong(moblie));
        //判断是否已绑定
        if (oldMemberPo != null) {
            if (oldMemberPo.getStatus().equals(EpMemberStatus.normal)) {
                responseMap.put(WechatTools.PARAM_CONTENT, "该手机号已绑定！");
            } else if (oldMemberPo.getStatus().equals(EpMemberStatus.freeze)) {
                responseMap.put(WechatTools.PARAM_CONTENT, "该手机号已绑定！但手机号已被冻结！");
            } else {
                responseMap.put(WechatTools.PARAM_CONTENT, "找不到该手机号！");
            }
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            return responseMap;
        }
        EpMessageCaptchaPo messageCaptchaPo = messageCaptchaRepository.getBySourceIdAndCaptchaContent(Long.parseLong(moblie), EpMessageCaptchaCaptchaType.short_msg
                , EpMessageCaptchaCaptchaScene.wx_bind_mobile, captcha);
        if (messageCaptchaPo != null) {
            if (!DateTools.getTimeIsAfter(messageCaptchaPo.getExpireTime(), DateTools.getCurrentDateTime())) {
                responseMap.put(WechatTools.PARAM_CONTENT, "验证码已失效！");
            } else {
                //会员表插入数据
                EpMemberPo epMemberPo = new EpMemberPo();
                epMemberPo.setMobile(Long.parseLong(moblie));
                epMemberPo.setStatus(EpMemberStatus.normal);
                memberRepository.insert(epMemberPo);
                responseMap.put(WechatTools.PARAM_CONTENT, "您已绑定成功！");
            }
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            return responseMap;
        }
        responseMap.put(WechatTools.PARAM_CONTENT, "绑定失败！");
        responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
        return responseMap;
    }

    /**
     * 接收绑定#号码
     *
     * @param mobile
     */
    private Map<String, String> receiveTextMsgBindMobile(String mobile) {
        Map<String, String> responseMap = Maps.newHashMap();
        //判断是否为手机号
        if (!Pattern.matches(BizConstant.PATTERN_MOBILE, mobile)) {
            responseMap.put(WechatTools.PARAM_CONTENT, "找不到该手机号！");
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            return responseMap;
        }
        EpMemberPo epMemberPo = memberRepository.getByMobile(Long.parseLong(mobile));
        //判断是否已绑定
        if (epMemberPo != null) {
            if (epMemberPo.getStatus().equals(EpMemberStatus.normal)) {
                responseMap.put(WechatTools.PARAM_CONTENT, "该手机号已绑定！");
            } else if (epMemberPo.getStatus().equals(EpMemberStatus.freeze)) {
                responseMap.put(WechatTools.PARAM_CONTENT, "该手机号已绑定！但手机号已被冻结！");
            } else {
                responseMap.put(WechatTools.PARAM_CONTENT, "找不到该手机号！");
            }
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            return responseMap;
        }
        EpSystemDictPo dictPo = dictComponent.getByGroupNameAndKey(BizConstant.DICT_GROUP_QCLOUDSMS, BizConstant.DICT_KEY_WECHAT_BIND_MOBILE_CAPTCHA);
        //短信模板id
        int templateId = Integer.parseInt(dictPo.getValue());
        String[] templateParams = new String[2];
        //验证码
        String captcha = ValidCodeTools.generateDigitValidCode(BizConstant.DB_NUM_ZERO);
        templateParams[0] = captcha;
        templateParams[1] = String.valueOf(BizConstant.CAPTCHA_SHORT_MSG_EXPIRE_MINUTE);
        //发送短信（事件）
        QcloudsmsEventBo eventBo = new QcloudsmsEventBo(templateId, mobile, templateParams);
        publisher.publishEvent(eventBo);
        EpMessageCaptchaPo epMessageCaptchaPo = new EpMessageCaptchaPo();
        epMessageCaptchaPo.setCaptchaType(EpMessageCaptchaCaptchaType.short_msg);
        epMessageCaptchaPo.setSourceId(Long.parseLong(mobile));
        epMessageCaptchaPo.setCaptchaContent(captcha);
        epMessageCaptchaPo.setCaptchaScene(EpMessageCaptchaCaptchaScene.wx_bind_mobile);
        epMessageCaptchaPo.setExpireTime(DateTools.addMinuteTimestamp(DateTools.getCurrentDate(), BizConstant.CAPTCHA_SHORT_MSG_EXPIRE_MINUTE));
        //验证码表插入数据
        messageCaptchaRepository.insert(epMessageCaptchaPo);
        //微信回复
        String responseContent = String.format(SpringComponent.messageSource(BizConstant.WECHAT_CAPTCHA_BIND_MOBILE_TIP), mobile);
        responseMap.put(WechatTools.PARAM_CONTENT, responseContent);
        responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
        return responseMap;
    }


    private Map<String, String> defaultResponse() {
        Map<String, String> responseMap = Maps.newHashMap();
        responseMap.put(WechatTools.PARAM_CONTENT, "么么哒！");
        responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
        return responseMap;
    }
}
