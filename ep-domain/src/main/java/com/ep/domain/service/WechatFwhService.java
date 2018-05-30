package com.ep.domain.service;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.ValidCodeTools;
import com.ep.common.tool.wechat.WechatTools;
import com.ep.domain.component.DictComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.event.QcloudsmsEventBo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.pojo.po.EpMessageCaptchaPo;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.pojo.po.EpWechatOpenidPo;
import com.ep.domain.repository.MemberRepository;
import com.ep.domain.repository.MessageCaptchaRepository;
import com.ep.domain.repository.WechatOpenidRepository;
import com.ep.domain.repository.domain.enums.EpMemberStatus;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaScene;
import com.ep.domain.repository.domain.enums.EpMessageCaptchaCaptchaType;
import com.ep.domain.repository.domain.enums.EpWechatOpenidType;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @Description: 微信服务号服务接口
 * @Author: CC.F
 * @Date: 0:20 2018/4/23
 */
@Slf4j
@Service
public class WechatFwhService {

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
    private WechatOpenidRepository wechatOpenidRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 客服接口-发消息
     *
     * @param accessToken
     * @param openId
     * @param msg
     * @throws Exception
     */
    public ResultDo msgCustomSend(String accessToken, String openId, String msg) throws Exception {
        log.info("[微信服务号]指定openid发送消息开始，openid={},msg={}。", openId, msg);
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
                log.info("[微信服务号]指定openid发送消息成功，openid={},msg={}。", openId, msg);
                return ResultDo.build();
            } else {
                log.error("[微信服务号]指定openid发送消息失败，openid={},errcode={}。", openId, responseMap.get("errcode").toString());
                return ResultDo.build().setSuccess(false).setError(responseMap.get("errcode").toString());
            }
        }
        log.error("[微信服务号]指定openid发送消息失败，openid={},原因={}。", openId, MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
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
                log.error("[微信服务号]调用微信接口，获取access_token失败，errcode={}，errmsg={}。", responseMap.get(WechatTools.PARAM_ERRCODE), responseMap.get(WechatTools.PARAM_ERRMSG));
                return ResultDo.build().setSuccess(false).setError(responseMap.get(WechatTools.PARAM_ERRCODE).toString())
                        .setErrorDescription(responseMap.get(WechatTools.PARAM_ERRMSG).toString());
            }
        }
        log.error("[微信服务号]获取access_token失败，原因={}。", MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
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
        return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
    }

    /**
     * 自定义菜单创建
     *
     * @return
     */
    public ResultDo menuCreate(String menuJson) {
        log.info("[微信服务号]自定义菜单创建开始，菜单={}。", menuJson);
        ResultDo resultDoAccessToken = this.getAccessToken();
        if (!resultDoAccessToken.isSuccess()) {
            return ResultDo.build().setSuccess(false);
        }
        String accessToken = (String) resultDoAccessToken.getResult();
        String url = String.format(BizConstant.WECHAT_URL_MENU_CREATE, accessToken);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, menuJson, String.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            log.info("[微信服务号]自定义菜单创建，接口返回={}。", responseEntity.getBody());
            return ResultDo.build().setResult(responseEntity.getBody());
        }
        log.error("[微信服务号]自定义菜单创建失败，原因={}。", MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
    }

    /**
     * 自定义菜单删除
     *
     * @return
     */
    public ResultDo menuDelete() {
        log.info("[微信服务号]自定义菜单删除开始。");
        ResultDo resultDoAccessToken = this.getAccessToken();
        if (!resultDoAccessToken.isSuccess()) {
            return ResultDo.build().setSuccess(false);
        }
        String accessToken = (String) resultDoAccessToken.getResult();
        String url = String.format(BizConstant.WECHAT_URL_MENU_DELETE, accessToken);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            log.info("[微信服务号]自定义菜单删除，接口返回={}。", responseEntity.getBody());
            return ResultDo.build().setResult(responseEntity.getBody());
        }
        log.error("[微信服务号]自定义菜单删除失败，原因={}。", MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
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
                responseMap = this.receiveEventSubscribe(requestMap.get(WechatTools.PARAM_FROMUSERNAME));
            } else if (requestMap.get(WechatTools.PARAM_EVENT).equals(WechatTools.EVENT_CLICK)) {
                //事件类型为点击 click
                responseMap = this.receiveEventClick(requestMap.get("EventKey"));
            }
        } else if (requestMap.get(WechatTools.PARAM_MSGTYPE).equals(WechatTools.MSGTYPE_TEXT)) {
            //请求为文本类型
            responseMap = this.receiveText(requestMap.get(WechatTools.PARAM_CONTENT), requestMap.get(WechatTools.PARAM_FROMUSERNAME));
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
    public Map<String, String> receiveEventSubscribe(String openid) {
        log.info("[微信服务号]接收事件(类型为关注),openid={}。", openid);
        Map<String, String> responseMap = Maps.newHashMap();
        responseMap.put(WechatTools.PARAM_CONTENT, "谢谢您关注小竹马！");
        responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
        Optional<EpWechatOpenidPo> optional = wechatOpenidRepository.getByOpenidAndType(openid, EpWechatOpenidType.fwh);
        if (!optional.isPresent()) {
            EpWechatOpenidPo wechatOpenidPo = new EpWechatOpenidPo();
            wechatOpenidPo.setOpenid(openid);
            wechatOpenidPo.setType(EpWechatOpenidType.fwh);
            wechatOpenidRepository.insert(wechatOpenidPo);
            log.info("[微信服务号]接收事件(类型为关注),ep_wechat_openid表插入数据id={}", wechatOpenidPo.getId());
        }
        return responseMap;
    }


    /**
     * 接收事件(类型为点击)
     *
     * @param eventKey
     * @return
     */
    public Map<String, String> receiveEventClick(String eventKey) {
        log.info("[微信服务号]接收事件(类型为点击),eventKey={}。", eventKey);
        Map<String, String> responseMap = Maps.newHashMap();
        if (BizConstant.WECHAT_EVENTKEY_BIND_MOBILE.equals(eventKey)) {
            log.info("[微信服务号]接收事件(类型为点击)为点击绑定手机号,服务号回复绑定手机号提示语。");
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
    public Map<String, String> receiveText(String content, String openid) {
        log.info("[微信服务号]接收文本,openid={},content={}。", openid, content);
        Map<String, String> responseMap = Maps.newHashMap();
        //非法输入多个###
        if (Pattern.matches(BizConstant.PATTERN_ILLEGAL_SPLIT, content)) {
            log.error("[微信服务号]接收文本失败,openid={},content={}，原因=非法输入多个###。", openid, content);
            return this.defaultResponse();
        }
        String[] contentParams = content.split(BizConstant.WECHAT_TEXT_MSG_SPLIT);
        //微信用户绑定手机号请求
        if (contentParams[0].equals(BizConstant.WECHAT_TEXT_MSG_BIND_MOBILE)) {
            if (contentParams.length == BizConstant.DB_NUM_TWO) {
                String mobile = contentParams[1];
                return this.receiveTextMsgBindMobile(mobile, openid);
            } else {
                return this.defaultResponse();
            }
        }
        //微信用户通过验证码和手机号绑定请求
        if (Pattern.matches(BizConstant.WECHAT_PATTERN_CAPTCHA, contentParams[0])
                && Pattern.matches(BizConstant.PATTERN_MOBILE, contentParams[1])
                && contentParams.length == BizConstant.DB_NUM_TWO) {
            return this.receiveTextMsgCaptchaMobile(contentParams[0], contentParams[1], openid);

        }
        responseMap.put(WechatTools.PARAM_CONTENT, "么么哒！");
        responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
        return responseMap;
    }

    /**
     * 接收验证码#绑定号码
     *
     * @param mobile
     */
    @Transactional(rollbackFor = Exception.class)
    private Map<String, String> receiveTextMsgCaptchaMobile(String captcha, String mobile, String openid) {
        log.info("[微信服务号]接收文本验证码#绑定号码,captcha={},mobile={},openid={}。", captcha, mobile, openid);
        Map<String, String> responseMap = Maps.newHashMap();
        EpMemberPo oldMemberPo = memberRepository.getByMobile(Long.parseLong(mobile));
        //判断是否已绑定
        if (oldMemberPo != null) {
            if (oldMemberPo.getStatus().equals(EpMemberStatus.normal)) {
                responseMap.put(WechatTools.PARAM_CONTENT, "该手机号已绑定！");
            } else if (oldMemberPo.getStatus().equals(EpMemberStatus.freeze)) {
                responseMap.put(WechatTools.PARAM_CONTENT, "该手机号已绑定！但手机号已被冻结！");
            } else {
                responseMap.put(WechatTools.PARAM_CONTENT, "找不到该手机号！");
                if (oldMemberPo.getStatus().equals(EpMemberStatus.cancel)) {
                    log.info("[微信服务号]，该手机号被平台注销，mobile={}。", mobile);
                }
            }
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            return responseMap;
        }
        //ep_wechat_openid表是否已有数据
        Optional<EpWechatOpenidPo> wechatOpenidOptional = wechatOpenidRepository.getByMobileAndOpenidAndType(Long.parseLong(mobile), openid, EpWechatOpenidType.fwh);
        if (wechatOpenidOptional.isPresent()) {
            responseMap.put(WechatTools.PARAM_CONTENT, "该手机号已绑定小竹马服务号！");
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            return responseMap;
        }
        EpMessageCaptchaPo messageCaptchaPo = messageCaptchaRepository.getBySourceIdAndCaptchaContent(Long.parseLong(mobile), EpMessageCaptchaCaptchaType.short_msg
                , EpMessageCaptchaCaptchaScene.wx_bind_mobile, captcha);
        if (messageCaptchaPo != null) {
            if (!DateTools.getTimeIsAfter(messageCaptchaPo.getExpireTime(), DateTools.getCurrentDateTime())) {
                responseMap.put(WechatTools.PARAM_CONTENT, "验证码已失效！");
            } else {
                //会员表插入数据
                EpMemberPo epMemberPo = new EpMemberPo();
                epMemberPo.setMobile(Long.parseLong(mobile));
                epMemberPo.setStatus(EpMemberStatus.normal);
                memberRepository.insert(epMemberPo);
                log.info("[微信服务号]ep_member插入数据，id={}。", epMemberPo.getId());
                responseMap.put(WechatTools.PARAM_CONTENT, "您已绑定成功" + mobile + "的手机号！");
                //公众号关联表更新手机号
                EpWechatOpenidPo wechatOpenidPo = new EpWechatOpenidPo();
                wechatOpenidPo.setOpenid(openid);
                wechatOpenidPo.setType(EpWechatOpenidType.fwh);
                wechatOpenidPo.setMobile(Long.parseLong(mobile));
                Optional<EpWechatOpenidPo> optionalWechatOpenid = wechatOpenidRepository.getByOpenidAndType(openid, EpWechatOpenidType.fwh);
                if (optionalWechatOpenid.isPresent()) {
                    //该微信绑定过，则公众号关联表更新手机号
                    if (wechatOpenidRepository.updateMobileByOpenidAndType(Long.parseLong(mobile), openid, EpWechatOpenidType.fwh) == BizConstant.DB_NUM_ONE) {
                        log.info("[微信服务号]微信服务号更新绑定手机号成功，openid={},mobile={}。", openid, mobile);
                    }
                } else {
                    //该微信未绑定过，则公众号关联表插入数据
                    EpWechatOpenidPo insertWechatOpenidPo = new EpWechatOpenidPo();
                    insertWechatOpenidPo.setOpenid(openid);
                    insertWechatOpenidPo.setType(EpWechatOpenidType.fwh);
                    insertWechatOpenidPo.setMobile(Long.parseLong(mobile));
                    wechatOpenidRepository.insert(insertWechatOpenidPo);
                    log.info("[微信服务号]ep_wechat_openid插入数据，id={}。", insertWechatOpenidPo.getId());
                    log.info("[微信服务号]微信服务号绑定手机号成功，openid={},mobile={}。", openid, mobile);
                }
            }
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            return responseMap;
        }
        log.error("[微信服务号]微信服务号绑定手机号失败，openid={},mobile={}。", openid, mobile);
        responseMap.put(WechatTools.PARAM_CONTENT, "绑定失败！");
        responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
        return responseMap;
    }

    /**
     * 接收文本绑定#号码
     *
     * @param mobile
     */
    private Map<String, String> receiveTextMsgBindMobile(String mobile, String openid) {
        log.info("[微信服务号]接收文本绑定#号码,mobile={}。", mobile);
        Map<String, String> responseMap = Maps.newHashMap();
        //判断是否为手机号
        if (!Pattern.matches(BizConstant.PATTERN_MOBILE, mobile)) {
            responseMap.put(WechatTools.PARAM_CONTENT, "找不到该手机号！");
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            log.error("[微信服务号]接收文本绑定#号码,mobile={},原因:不是标准的手机号。", mobile);
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
                if (epMemberPo.getStatus().equals(EpMemberStatus.cancel)) {
                    log.info("[微信服务号]，该手机号被平台注销，mobile={}。", mobile);
                }
            }
            responseMap.put(WechatTools.PARAM_MSGTYPE, WechatTools.MSGTYPE_TEXT);
            return responseMap;
        }
        //ep_wechat_openid表是否已有数据
        Optional<EpWechatOpenidPo> wechatOpenidOptional = wechatOpenidRepository.getByMobileAndOpenidAndType(Long.parseLong(mobile), openid, EpWechatOpenidType.fwh);
        if (wechatOpenidOptional.isPresent()) {
            responseMap.put(WechatTools.PARAM_CONTENT, "该手机号已绑定小竹马服务号！");
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
        log.info("[微信服务号]ep_message_captcha表插入数据，id={}。", epMessageCaptchaPo.getId());
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
