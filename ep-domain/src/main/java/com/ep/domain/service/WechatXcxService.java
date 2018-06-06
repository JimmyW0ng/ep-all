package com.ep.domain.service;

import com.ep.common.tool.CryptTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.JsonTools;
import com.ep.common.tool.StringTools;
import com.ep.common.tool.wechat.WechatTools;
import com.ep.domain.component.DictComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.pojo.wechat.WechatSessionBo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 微信小程序服务接口
 * @Author J.W
 * @Date: 上午 11:55 2018/5/8 0008
 */
@Slf4j
@Service
public class WechatXcxService {
    @Value("${wechat.xcx.member.appid}")
    private String wechatXcxMemberAppid;
    @Value("${wechat.xcx.member.secret}")
    private String wechatXcxMemberSecret;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DictComponent dictComponent;

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
        ResponseEntity<WechatSessionBo> entity = restTemplate.getForEntity(url, WechatSessionBo.class);
        log.info("微信获取session返回：{}", entity);
        if (!HttpStatus.OK.equals(entity.getStatusCode())) {
            return resultDo.setError(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        }
        WechatSessionBo sessionPojo = entity.getBody();
        if (!entity.getBody().isSuccess()) {
            return resultDo.setError(sessionPojo.getErrcode()).setErrorDescription(sessionPojo.getErrmsg());
        }
        String jsonStr = JsonTools.encode(sessionPojo);
        return resultDo.setResult(CryptTools.aesEncrypt(jsonStr, BizConstant.WECHAT_TOKEN_SECRET));
    }

    /**
     * 获取小程序(客户端)access_token,先从从数据库获取再调用微信小程序接口
     *
     * @return
     */
    public ResultDo getMemberAccessToken() {
        EpSystemDictPo dictPo = dictComponent.getByGroupNameAndKey(BizConstant.DICT_GROUP_WECHAT, BizConstant.DICT_KEY_WECHAT_XCX_MEMBER_ACCESS_TOKEN);
        //本地access_token是否过期
        if (dictPo != null && DateTools.addMinute(DateTools.getCurrentDate(), BizConstant.WECHAT_SESSION_TIME_OUT_M).before(dictPo.getUpdateAt())) {
            return ResultDo.build().setResult(dictPo.getValue());
        }
        //本地access_token过期调用微信接口
        String url = String.format(BizConstant.WECHAT_XCX_API_ACCESS_TOKEN, wechatXcxMemberAppid, wechatXcxMemberSecret);
        ResponseEntity<HashMap> responseEntity = restTemplate.getForEntity(url, HashMap.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            Map<String, Object> responseMap = responseEntity.getBody();
            if (null != responseMap.get(WechatTools.PARAM_ACCESSTOKEN)) {
                String accessToken = (String) responseMap.get(WechatTools.PARAM_ACCESSTOKEN);
                return ResultDo.build().setResult(accessToken);
            } else {
                log.error("[微信小程序]调用微信接口，获取access_token失败，errcode={}，errmsg={}。", responseMap.get(WechatTools.PARAM_ERRCODE), responseMap.get(WechatTools.PARAM_ERRMSG));
                return ResultDo.build().setSuccess(false).setError(responseMap.get(WechatTools.PARAM_ERRCODE).toString())
                        .setErrorDescription(responseMap.get(WechatTools.PARAM_ERRMSG).toString());
            }
        }
        log.error("[微信小程序]获取access_token失败，原因={}。", MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
    }

    /**
     * 获取模板库某个模板标题下关键词库
     *
     * @param id
     * @return
     */
    public ResultDo templateLibraryGet(String id) {
        log.info("[微信小程序]获取模板库某个模板标题下关键词库开始。");
        ResultDo resultDoAccessToken = this.getMemberAccessToken();
        if (!resultDoAccessToken.isSuccess()) {
            return ResultDo.build().setSuccess(false);
        }
        String accessToken = (String) resultDoAccessToken.getResult();
        String url = String.format(BizConstant.WECHAT_XCX_API_MESSAGE_TEMPLATE_LIBRARY_GET, accessToken);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("id", id);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, jsonParam.toString(), String.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            log.info("[微信小程序]获取模板库某个模板标题下关键词库，接口返回={}。", responseEntity.getBody());
            return ResultDo.build().setResult(responseEntity.getBody());
        }
        log.error("[微信小程序]获取模板库某个模板标题下关键词库失败，原因={}。", MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
    }

    /**
     * 获取帐号下已存在的模板列表
     *
     * @param offset
     * @return
     */
    public ResultDo templateList(Integer offset, Integer count) {
        log.info("[微信小程序]获取帐号下已存在的模板列表开始。");
        ResultDo resultDoAccessToken = this.getMemberAccessToken();
        if (!resultDoAccessToken.isSuccess()) {
            return ResultDo.build().setSuccess(false);
        }
        String accessToken = (String) resultDoAccessToken.getResult();
        String url = String.format(BizConstant.WECHAT_XCX_API_MESSAGE_TEMPLATE_LIST, accessToken);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("offset", offset);
        jsonParam.put("count", count);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, jsonParam.toString(), String.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            log.info("[微信小程序]获取帐号下已存在的模板列表，接口返回={}。", responseEntity.getBody());
            return ResultDo.build().setResult(responseEntity.getBody());
        }
        log.error("[微信小程序]获取帐号下已存在的模板列表失败，原因={}。", MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
    }

    /**
     * 发送模板消息
     *
     * @param openid
     * @param templateId
     * @param page
     * @param formId
     * @param data
     * @param color
     * @param emphasisKeyword
     * @return
     */
    public ResultDo messageTemplateSend(String openid, String templateId, String page, String formId, String data, String color, String emphasisKeyword) {
        log.info("[微信小程序]发送模板消息开始");
        ResultDo resultDoAccessToken = this.getMemberAccessToken();
        if (!resultDoAccessToken.isSuccess()) {
            return ResultDo.build().setSuccess(false);
        }
        String accessToken = (String) resultDoAccessToken.getResult();
        String url = String.format(BizConstant.WECHAT_XCX_API_MESSAGE_TEMPLATE_SEND, accessToken);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(WechatTools.PARAM_TOUSER, openid);
        jsonParam.put("template_id", templateId);
        if (StringTools.isNotBlank(page)) {
            jsonParam.put("page", page);
        }
        jsonParam.put("form_id", formId);
        jsonParam.put("data", data);
        if (StringTools.isNotBlank(color)) {
            jsonParam.put("color", color);
        }
        if (StringTools.isNotBlank(emphasisKeyword)) {
            jsonParam.put("emphasis_keyword", emphasisKeyword);
        }
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, jsonParam.toString(), HashMap.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            Map<String, Object> responseMap = responseEntity.getBody();
            if (responseMap.get("errcode").toString().equals(BizConstant.WECHAT_SUCCESS_CODE)) {
                log.info("[微信小程序]发送模版消息成功，openid={}。", openid);
                return ResultDo.build();
            } else {
                log.error("[微信小程序]发送模版消息失败，openid={},errcode={}。", openid, responseMap.get("errcode").toString());
                return ResultDo.build().setSuccess(false).setError(responseMap.get("errcode").toString());
            }
        }
        return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
    }


    /**
     * 根据sessionToken获取微信登录对象
     *
     * @param sessionToken
     * @return
     */
    public WechatSessionBo getSessionObject(String sessionToken) throws GeneralSecurityException {
        String decode = CryptTools.aesDecrypt(sessionToken, BizConstant.WECHAT_TOKEN_SECRET);
        return JsonTools.decode(decode, WechatSessionBo.class);
    }

}
