package com.ep.domain.component;

import com.ep.common.tool.SerialNumberTools;
import com.ep.common.tool.StringTools;
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
    private static final String URL_PAY_ORDERQUERY = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";
    private static final String URL_PAY_REFUNDQUERY = "https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery";
    private static final String URL_SANDBOX_GET_KEY = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";

    @Value("${wechat.xcx.member.appid}")
    private String xcxMemberAppid;
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
     * 查询订单
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户订单号
     * @return
     * @throws Exception
     */
    public ResultDo orderquery(String transactionId, String outTradeNo) throws Exception {
        Map<String, String> requestMap = Maps.newHashMap();
        if (StringTools.isBlank(transactionId) && StringTools.isBlank(outTradeNo)) {
            return ResultDo.build(MessageCode.ERROR_WECHAT_API_REQPARAM);
        }
        requestMap.put("transaction_id", transactionId);
        requestMap.put("out_trade_no", outTradeNo);
        String url = URL_PAY_ORDERQUERY;
        String xml = WechatTools.mapToXmlString(this.fillRequestData(requestMap));
        log.info("[微信支付]查询订单，接口入参={}。", xml);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, xml, String.class);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        }
        String resultStr = responseEntity.getBody();
        log.info("[微信支付]查询订单，接口返回={}。", resultStr);
        return ResultDo.build().setResult(resultStr);
    }


    /**
     * 查询退款
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户订单号
     * @param outRefundNo   商户退款单号
     * @param refundId      微信退款单号
     * @param offset      位移量
     * @return
     * @throws Exception
     */
    public ResultDo refundquery(String transactionId, String outTradeNo, String outRefundNo, String refundId, Integer offset) throws Exception {
        Map<String, String> requestMap = Maps.newHashMap();
        if (StringTools.isBlank(transactionId) && StringTools.isBlank(outTradeNo)
                && StringTools.isBlank(outRefundNo) && StringTools.isBlank(refundId)) {
            return ResultDo.build(MessageCode.ERROR_WECHAT_API_REQPARAM);
        }
        requestMap.put("transaction_id", transactionId);
        requestMap.put("out_trade_no", outTradeNo);
        requestMap.put("out_refund_no", outRefundNo);
        requestMap.put("refund_id", refundId);
        if (null != offset) {
            requestMap.put("offset", offset.toString());
        }
        String url = URL_PAY_REFUNDQUERY;
        String xml = WechatTools.mapToXmlString(this.fillRequestData(requestMap));
        log.info("[微信支付]查询退款，接口入参={}。", xml);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, xml, String.class);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        }
        String resultStr = responseEntity.getBody();
        log.info("[微信支付]查询退款，接口返回={}。", resultStr);
        return ResultDo.build().setResult(resultStr);
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

    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @param reqData
     * @return
     * @throws Exception
     */
    public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
        reqData.put("appid", xcxMemberAppid);
        reqData.put("mch_id", wechatPayMchid);
        reqData.put("nonce_str", WechatTools.generateNonceStr());
        reqData.put("sign", WechatTools.generateSignature(reqData, wechatPayKey));
        return reqData;
    }
}
