package com.ep.domain.component;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.SerialNumberTools;
import com.ep.common.tool.StringTools;
import com.ep.common.tool.wechat.WechatTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpWechatUnifiedOrderPo;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.WechatUnifiedOrderRepository;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
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
    private static final String URL_PAY_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
    private static final String URL_PAY_REFUNDQUERY = "https://api.mch.weixin.qq.com/pay/refundquery";
    private static final String URL_SANDBOX_GET_KEY = "https://api.mch.weixin.qq.com/pay/getsignkey";

    @Value("${wechat.xcx.member.appid}")
    private String xcxMemberAppid;
    @Value("${wechat.pay.mchid}")
    private String wechatPayMchid;
    @Value("${wechat.pay.key}")
    private String wechatPayKey;
    @Value("${wechat.pay.notfy}")
    private String notifyUrl;

    @Autowired
    private WechatUnifiedOrderRepository wechatUnifiedOrderRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 小程序客户端统一下单
     *
     * @param orderId
     * @param ognName
     * @param prize
     * @param openid
     * @param ip
     * @return
     * @throws Exception
     */
    public ResultDo xcxUnifiedorder(Long orderId,
                                    String ognName,
                                    BigDecimal prize,
                                    String openid,
                                    String ip) throws Exception {
        log.info("【微信支付】统一下单入参: orderId={}, ognName={}, prize={}, openid={}, ip={}", orderId, ognName, prize, openid, ip);
        // 保存本地微信支付统一下单表
        String body = Joiner.on(WechatTools.BODY_SPLIT).join(ognName, BizConstant.WECHAT_PAY_BODY);
        String outTradeNo = SerialNumberTools.generateOutTradeNo(orderId);
        Integer totalFee = prize.multiply(new BigDecimal(BizConstant.NUM_ONE_HUNDRED)).intValue();
        String tradeType = WechatTools.XCX_PAY_TRADE_TYPE;
        EpWechatUnifiedOrderPo unifiedOrderPo = new EpWechatUnifiedOrderPo();
        log.info("【微信支付】统一下单生成商户订单号: outTradeNo={}", outTradeNo);
        unifiedOrderPo.setOrderId(orderId);
        unifiedOrderPo.setAppid(xcxMemberAppid);
        unifiedOrderPo.setMchId(wechatPayMchid);
        unifiedOrderPo.setOutTradeNo(outTradeNo);
        unifiedOrderPo.setBody(body);
        unifiedOrderPo.setTotalFee(totalFee);
        unifiedOrderPo.setSpbillCreateIp(ip);
        unifiedOrderPo.setTradeType(tradeType);
        wechatUnifiedOrderRepository.insert(unifiedOrderPo);
        // 发起接口调用
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
        requestMap.put("trade_type", tradeType);
        requestMap.put("sign", WechatTools.generateSignature(requestMap, wechatPayKey));
        String mapToXml = WechatTools.mapToXmlString(requestMap);
        log.debug("【微信支付】统一下单提交参数: outTradeNo={}, xml={}", outTradeNo, mapToXml);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_PAY_UNIFIEDORDER, mapToXml, String.class);
        log.info("【微信支付】统一下单返回: outTradeNo={}, responseEntity={}", outTradeNo, responseEntity);
        // 更新本地状态
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            log.error("【微信支付】统一下单请求异常: outTradeNo={}, httpStatus={}", outTradeNo, responseEntity.getStatusCodeValue());
            String code = String.valueOf(responseEntity.getStatusCodeValue());
            String errorMsg = SpringComponent.messageSource(MessageCode.ERROR_HTTP_STATUS, new String[]{code});
            return ResultDo.build(MessageCode.ERROR_HTTP_STATUS).setErrorDescription(errorMsg);
        }
        // 调用远程接口成功
        Map<String, String> data = WechatTools.xmlToMap(responseEntity.getBody());
        log.info("【微信支付】统一下单返回Map对象: outTradeNo={}, data={}", outTradeNo, data);
        String returnCode = data.get("return_code");
        String returnMsg = data.get("return_msg");
        String resultCode = data.get("result_code");
        String errCode = data.get("err_code");
        String errCodeDes = data.get("err_code_des");
        String prepayId = data.get("prepay_id");
        wechatUnifiedOrderRepository.handleUnifiedOrder(unifiedOrderPo.getId(), returnCode, returnMsg, resultCode, errCode, errCodeDes, prepayId);
        // 请求处理失败
        if (!returnCode.equals(WechatTools.SUCCESS)) {
            log.error("【微信支付】统一下单返回处理失败, outTradeNo={}, returnCode={}, returnMsg={}", outTradeNo, returnCode, returnMsg);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_FAIL).setErrorDescription(returnMsg);
        }
        // 验签
        if (!WechatTools.isSignatureValid(data, wechatPayKey)) {
            log.error("【微信支付】统一下单返回验签失败, outTradeNo={}", outTradeNo);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_RETURN_SIGN);
        }
        // 业务结果失败
        if (!resultCode.equals(WechatTools.SUCCESS)) {
            log.error("【微信支付】统一下单返回结果失败, outTradeNo={}, resultCode={}, errCode={}, errCodeDes={}", outTradeNo, resultCode, errCode, errCodeDes);
            return ResultDo.build(MessageCode.ERROR_WECHAT_UNIFIED_ORDER_FAIL).setErrorDescription(errCodeDes);
        }
        // 封装返回结果
        ResultDo<Map<String, String>> resultDo = ResultDo.build();
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("appid", xcxMemberAppid);
        resultMap.put("timeStamp", String.valueOf(DateTools.getCurrentDate().getTime() / 1000));
        resultMap.put("nonceStr", WechatTools.generateUUID());
        resultMap.put("package", "prepay_id=" + prepayId);
        resultMap.put("signType", WechatTools.SignType.MD5.name());
        resultMap.put("paySign", WechatTools.generateSignature(resultMap, wechatPayKey));
        resultMap.remove("appid");
        log.info("【微信支付】统一下单成功: outTradeNo={}", outTradeNo);
        return resultDo.setResult(resultMap);
    }

    /**
     * 微信支付notify处理
     *
     * @param respMap
     * @return
     */
    public ResultDo handlePayNotify(Map<String, String> respMap) throws Exception {
        // 验签
        try {
            if (!WechatTools.isSignatureValid(respMap, wechatPayKey)) {
                log.error("【微信支付】notify通知验签不通过, 返回数据: {}", respMap);
                return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_RETURN_SIGN);
            }
        } catch (Exception e) {
            log.error("【微信支付】notify通知验签异常, 返回数据: {}", respMap);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_RETURN_SIGN);
        }
        if (!respMap.containsKey("out_trade_no")) {
            log.error("【微信支付】notify通知缺少商户订单号数据, 返回数据: {}", respMap);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_NO_OUT_TRADE_NO);
        }
        // 更新支付结果
        String outTradeNo = respMap.get("out_trade_no");
        EpWechatUnifiedOrderPo unifiedOrderPo = wechatUnifiedOrderRepository.getByOutTradeNo(outTradeNo);
        if (StringTools.isBlank(unifiedOrderPo.getNotifyResultCode())) {
            // 更新支付结果
            String notifyReturnCode = respMap.get("return_code");
            String notifyReturnMsg = respMap.get("return_msg");
            String notifyResultCode = respMap.get("result_code");
            String notifyErrCode = respMap.get("err_code");
            String notifyErrCodeDes = respMap.get("err_code_des");
            String isSubscribe = respMap.get("is_subscribe");
            String openid = respMap.get("openid");
            String bankType = respMap.get("bank_type");
            String transactionId = respMap.get("transaction_id");
            String timeEnd = respMap.get("time_end");
            int num = wechatUnifiedOrderRepository.handleNotify(outTradeNo,
                    notifyReturnCode,
                    notifyReturnMsg,
                    notifyResultCode,
                    notifyErrCode,
                    notifyErrCodeDes,
                    isSubscribe,
                    openid,
                    bankType,
                    transactionId,
                    timeEnd);
            if (num == BizConstant.DB_NUM_ONE && WechatTools.SUCCESS.equals(notifyResultCode)) {
                // 订单更新支付状态
                orderRepository.orderPaidById(unifiedOrderPo.getOrderId());
            }
        }

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
        if (StringTools.isNotBlank(transactionId)) {
            requestMap.put("transaction_id", transactionId);
        }
        if (StringTools.isNotBlank(outTradeNo)) {
            requestMap.put("out_trade_no", outTradeNo);
        }
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
    private Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
        reqData.put("appid", xcxMemberAppid);
        reqData.put("mch_id", wechatPayMchid);
        reqData.put("nonce_str", WechatTools.generateUUID());
        reqData.put("sign", WechatTools.generateSignature(reqData, wechatPayKey));
        return reqData;
    }

}
