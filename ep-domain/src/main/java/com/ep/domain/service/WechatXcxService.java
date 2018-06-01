package com.ep.domain.service;

import com.ep.common.tool.CryptTools;
import com.ep.common.tool.JsonTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.wechat.WechatSessionBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.GeneralSecurityException;

/**
 * @Description: 微信小程序服务接口
 * @Author J.W
 * @Date: 上午 11:55 2018/5/8 0008
 */
@Slf4j
@Service
public class WechatXcxService {

    @Autowired
    private RestTemplate restTemplate;

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

//    public

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
