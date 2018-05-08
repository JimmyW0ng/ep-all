package com.ep.domain.component;

import com.ep.common.tool.wechat.WechatTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Description: 微信支付服务
 * @Author: CC.F
 * @Date: 10:35 2018/5/3/003
 */
@Slf4j
@Service
public class WechatPayComponent {

    /**
     * 接口地址
     */
    private static final String URL_PAY_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String URL_PAY_SANDBOX_UNIFIEDORDER = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
    private static final String URL_SANDBOX_GET_KEY = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";

    // @Value("${wechat.xcx.member.appid}")
    private String xcxMemberAppid = "wx2cb67c8e35a0bf2a";
    @Value("${wechat.pay.mchid}")
    private String wechatPayMchid;
    // @Value("${wechat.pay.key}")
    private String wechatPayKey = "3b4cfcbafaa069675e0b076d6dd8a759";
    @Value("${wechat.pay.notfy}")
    private String notifyUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 小程序客户端统一下单
     *
     * @param openid
     * @param body
     * @param outTradeNo
     * @param totalFee
     * @return
     * @throws Exception
     */
    public ResultDo xcxUnifiedorder(String openid, String body, String outTradeNo, String totalFee, String ip) throws Exception {
        log.info("【微信支付】统一下单入参: body={}, outTradeNo={}, totalFee={}, ip={}", body, outTradeNo, totalFee, ip);
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("appid", xcxMemberAppid);
        requestMap.put("mch_id", wechatPayMchid);
        requestMap.put("nonce_str", WechatTools.generateUUID());
        requestMap.put("openid", openid);
        requestMap.put("body", body);
        requestMap.put("out_trade_no", outTradeNo);
        requestMap.put("total_fee", "101");
        requestMap.put("spbill_create_ip", ip);
        requestMap.put("notify_url", notifyUrl);
        requestMap.put("trade_type", "JSAPI");
        requestMap.put("sign", WechatTools.generateSignature(requestMap, wechatPayKey));
        String mapToXml = WechatTools.mapToXmlString(requestMap);
        log.debug("【微信支付】统一下单提交参数: xml={}", mapToXml);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_PAY_SANDBOX_UNIFIEDORDER, mapToXml, String.class);
        log.info("【微信支付】统一下单返回: {}", responseEntity);
        return ResultDo.build();
    }


    /**
     * 获取沙箱秘钥
     *
     * @return
     */
    public ResultDo<Map<String, String>> getSandboxSignkey() {
        ResultDo<Map<String, String>> resultDo = ResultDo.build();
        try {
            Map<String, String> requestMap = Maps.newHashMap();
            requestMap.put("mch_id", this.wechatPayMchid);
            requestMap.put("nonce_str", WechatTools.generateUUID());
            requestMap.put("sign", WechatTools.generateSignature(requestMap, wechatPayKey));
            String xml = WechatTools.mapToXmlString(requestMap);
            log.info("【微信支付】获取沙箱秘钥入参: {}", xml);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_SANDBOX_GET_KEY, xml, String.class);
            log.info("【微信支付】获取沙箱秘钥返回: {}", responseEntity);
            // 调用远程接口失败
            if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                String code = String.valueOf(responseEntity.getStatusCodeValue());
                return resultDo.setError(MessageCode.ERROR_OPERATE_FAIL).setErrorDescription(code);
            }
            // 调用远程接口成功
            Map<String, String> data = WechatTools.xmlToMap(responseEntity.getBody());
            log.info("【微信支付】获取沙箱秘钥获取成功: {}", data);
            return resultDo.setResult(data);
        } catch (Exception e) {
            return resultDo.setError(MessageCode.ERROR_SYSTEM);
        }
    }
}
