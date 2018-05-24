package com.ep.domain.component;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.SerialNumberTools;
import com.ep.common.tool.StringTools;
import com.ep.common.tool.wechat.WechatTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpOrderPayStatus;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private static final String URL_PAY_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    private static final String URL_PAY_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
    private static final String URL_PAY_REFUNDQUERY = "https://api.mch.weixin.qq.com/pay/refundquery";
    private static final String URL_DOWNLOAD_BILL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    private static final String URL_SANDBOX_GET_KEY = "https://api.mch.weixin.qq.com/pay/getsignkey";
    private static final String NOTIFY_UNIFIEDORDER_URL = "/security/wechat/pay/notify";
    private static final String NOTIFY_REFUND_URL = "/security/wechat/pay/refund/notify";

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
    private WechatPayRefundRepository wechatPayRefundRepository;
    @Autowired
    private WechatPayBillRepository wechatPayBillRepository;
    @Autowired
    private WechatPayBillDetailRepository wechatPayBillDetailRepository;
    @Autowired
    private OrderRefundRepository orderRefundRepository;
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
        String body = BizConstant.WECHAT_PAY_BODY + orderId;
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
        unifiedOrderPo.setTradeState(WechatTools.TRADE_STATE_INIT);
        wechatUnifiedOrderRepository.insert(unifiedOrderPo);
        // 发起接口调用
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("appid", xcxMemberAppid);
        requestMap.put("mch_id", wechatPayMchid);
        requestMap.put("nonce_str", WechatTools.generateUUID());
        requestMap.put("openid", openid);
        requestMap.put("body", body);
        requestMap.put("out_trade_no", outTradeNo);
        requestMap.put("total_fee", String.valueOf(totalFee));
        requestMap.put("spbill_create_ip", ip);
        requestMap.put("notify_url", notifyUrl + NOTIFY_UNIFIEDORDER_URL);
        requestMap.put("trade_type", tradeType);
        requestMap.put("sign", WechatTools.generateSignature(requestMap, wechatPayKey));
        String mapToXml = WechatTools.mapToXmlString(requestMap);
        log.debug("【微信支付】统一下单提交参数: outTradeNo={}, xml={}", outTradeNo, mapToXml);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_PAY_UNIFIEDORDER, mapToXml, String.class);
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
        if (!WechatTools.SUCCESS.equals(returnCode)) {
            log.error("【微信支付】统一下单返回处理失败, outTradeNo={}, returnCode={}, returnMsg={}", outTradeNo, returnCode, returnMsg);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_FAIL).setErrorDescription(returnMsg);
        }
        // 验签
        if (!WechatTools.isSignatureValid(data, wechatPayKey)) {
            log.error("【微信支付】统一下单返回验签失败, outTradeNo={}", outTradeNo);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_RETURN_SIGN);
        }
        // 业务结果失败
        if (!WechatTools.SUCCESS.equals(resultCode)) {
            log.error("【微信支付】统一下单返回结果失败, outTradeNo={}, resultCode={}, errCode={}, errCodeDes={}", outTradeNo, resultCode, errCode, errCodeDes);
            return ResultDo.build(MessageCode.ERROR_WECHAT_UNIFIED_ORDER_FAIL).setErrorDescription(errCodeDes);
        }
        // 封装返回结果
        ResultDo<Map<String, String>> resultDo = ResultDo.build();
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("appId", xcxMemberAppid);
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
    @Transactional(rollbackFor = Exception.class)
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
            String tradeState;
            if (WechatTools.SUCCESS.equals(notifyResultCode) && StringTools.isNotBlank(transactionId)) {
                tradeState = WechatTools.TRADE_STATE_SUCCESS;
            } else {
                tradeState = unifiedOrderPo.getTradeState();
            }
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
                    timeEnd,
                    tradeState,
                    BizConstant.WECHAT_PAY_NOTIFY_REMARK);
            if (WechatTools.TRADE_STATE_SUCCESS.equals(tradeState)) {
                // 订单更新支付状态
                Timestamp payConfirmTime = null;
                if (StringTools.isNotBlank(timeEnd)) {
                    try {
                        payConfirmTime = DateTools.dateToTimestamp(DateTools.stringToDate(timeEnd, DateTools.TIME_PATTERN_MILLISECOND));
                    } catch (Exception e) {
                        log.error("payConfirmTime时间格式化错误: timeEnd={}", timeEnd);
                    }
                }
                orderRepository.orderPaidById(unifiedOrderPo.getOrderId(), payConfirmTime);
            }
        }
        return ResultDo.build();
    }

    /**
     * 小程序客户端退单
     *
     * @param outTradeNo
     * @return
     * @throws Exception
     */
    public ResultDo xcxPayRefund(String outTradeNo) throws Exception {
        log.info("【微信支付退单】入参: outTradeNo={}", outTradeNo);
        // 保存本地微信支付统一下单表
        EpWechatUnifiedOrderPo unifiedOrderPo = wechatUnifiedOrderRepository.getByOutTradeNo(outTradeNo);
        // 商户订单号不存在
        if (unifiedOrderPo == null || unifiedOrderPo.getDelFlag()) {
            log.error("【微信支付退单】商户订单号不存在，utTradeNo={}", outTradeNo);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_OUT_TRADE_NO_NOT_EXIST);
        }
        // 商户订单号状态
        if (!WechatTools.TRADE_STATE_SUCCESS.equals(unifiedOrderPo.getTradeState())) {
            log.error("【微信支付退单】商户订单号不是支付成功状态，utTradeNo={}", outTradeNo);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_OUT_TRADE_NO_SUCCESS);
        }
        // 只存在一笔支付的订单必须校验报名支付状态是否是退款申请中
        List<EpWechatUnifiedOrderPo> successPaidOrders = wechatUnifiedOrderRepository.findSuccessByOrderId(unifiedOrderPo.getOrderId());
        if (CollectionsTools.isNotEmpty(successPaidOrders) && successPaidOrders.size() == BizConstant.DB_NUM_ONE) {
            EpWechatUnifiedOrderPo successPaid = successPaidOrders.get(BizConstant.DB_NUM_ZERO);
            if (!outTradeNo.equals(successPaid.getOutTradeNo())) {
                log.error("【微信支付退单】失败，微信支付统一下单商户订单号不匹配，inputOutTradeNo={}, queryOutTradeNo={}", outTradeNo, successPaid.getOutTradeNo());
                return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_OUT_TRADE_NO_SUCCESS);
            }
            Optional<EpOrderPo> existOrder = orderRepository.findById(successPaid.getOrderId());
            if (!existOrder.isPresent()) {
                log.error("【微信支付退单】失败，找不到报名订单，outTradeNo={}, orderId={}", outTradeNo, successPaid.getOrderId());
                return ResultDo.build(MessageCode.ERROR_ORDER_NOT_EXISTS);
            }
            EpOrderPo orderPo = existOrder.get();
            if (!EpOrderPayStatus.refund_apply.equals(orderPo.getPayStatus())) {
                log.error("【微信支付退单】失败，报名订单不是退款申请中，outTradeNo={}, orderId={}, payStatus={}", outTradeNo, successPaid.getOrderId(), orderPo.getPayStatus());
                return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_REFUND_NOT_APPLY);
            }
        }
        // 生成退单编号
        String outRefundNo = SerialNumberTools.generateOutRefundNo(unifiedOrderPo.getOrderId());
        // 生成退单记录
        EpWechatPayRefundPo wechatPayRefundPo = new EpWechatPayRefundPo();
        wechatPayRefundPo.setAppid(unifiedOrderPo.getAppid());
        wechatPayRefundPo.setMchId(unifiedOrderPo.getMchId());
        wechatPayRefundPo.setOutTradeNo(unifiedOrderPo.getOutTradeNo());
        wechatPayRefundPo.setTransactionId(unifiedOrderPo.getTransactionId());
        wechatPayRefundPo.setOutRefundNo(outRefundNo);
        wechatPayRefundPo.setTotalFee(unifiedOrderPo.getTotalFee());
        wechatPayRefundPo.setRefundFee(unifiedOrderPo.getTotalFee());
        wechatPayRefundRepository.insert(wechatPayRefundPo);
        // 发起接口调用
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("apiclient_cert.p12"));
        try {
            keyStore.load(instream, wechatPayMchid.toCharArray());
        } finally {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                                           .loadKeyMaterial(keyStore, wechatPayMchid.toCharArray())
                                           .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                                                    .setSSLSocketFactory(sslsf)
                                                    .build();
        HttpPost httppost = new HttpPost(URL_PAY_REFUND);
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("appid", xcxMemberAppid);
        requestMap.put("mch_id", wechatPayMchid);
        requestMap.put("nonce_str", WechatTools.generateUUID());
        requestMap.put("out_trade_no", outTradeNo);
        requestMap.put("out_refund_no", outRefundNo);
        requestMap.put("total_fee", unifiedOrderPo.getTotalFee().toString());
        requestMap.put("refund_fee", unifiedOrderPo.getTotalFee().toString());
        requestMap.put("notify_url", notifyUrl + NOTIFY_REFUND_URL);
        requestMap.put("sign", WechatTools.generateSignature(requestMap, wechatPayKey));
        String mapToXml = WechatTools.mapToXmlString(requestMap);
        log.debug("【微信支付退单】提交参数: outTradeNo={}, xml={}", outTradeNo, mapToXml);
        StringEntity se = new StringEntity(mapToXml);
        try {
            httppost.setEntity(se);
            log.info("【微信支付退单】提交 {}", httppost.getRequestLine());
            CloseableHttpResponse responseEntry = httpclient.execute(httppost);
            try {
                log.info("【微信支付退单】提交返回 {}", responseEntry.getStatusLine());
                if (HttpStatus.OK.value() != responseEntry.getStatusLine().getStatusCode()) {
                    String code = String.valueOf(responseEntry.getStatusLine().getStatusCode());
                    log.error("【微信支付退单】请求异常: outTradeNo={}, httpStatus={}", outTradeNo, code);
                    String errorMsg = SpringComponent.messageSource(MessageCode.ERROR_HTTP_STATUS, new String[]{code});
                    return ResultDo.build(MessageCode.ERROR_HTTP_STATUS).setErrorDescription(errorMsg);
                }
                HttpEntity entity = responseEntry.getEntity();
                Map<String, String> respMap = WechatTools.inputStreamToMap(entity.getContent());
                log.info("【微信支付退单】返回Map对象: outTradeNo={}, data={}", outTradeNo, respMap);
                String returnCode = respMap.get("return_code");
                String returnMsg = respMap.get("return_msg");
                String resultCode = respMap.get("result_code");
                String errCode = respMap.get("err_code");
                String errCodeDes = respMap.get("err_code_des");
                String refundId = respMap.get("refund_id");
                wechatPayRefundRepository.handlePayRefund(wechatPayRefundPo.getId(), returnCode, returnMsg, resultCode, errCode, errCodeDes, refundId);
                // 请求处理失败
                if (!WechatTools.SUCCESS.equals(returnCode)) {
                    log.error("【微信支付退单】返回处理失败, outTradeNo={}, returnCode={}, returnMsg={}", outTradeNo, returnCode, returnMsg);
                    return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_FAIL).setErrorDescription(returnMsg);
                }
                // 验签
                if (!WechatTools.isSignatureValid(respMap, wechatPayKey)) {
                    log.error("【微信支付退单】返回验签失败, outTradeNo={}", outTradeNo);
                    return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_RETURN_SIGN);
                }
                // 业务结果失败
                if (!WechatTools.SUCCESS.equals(resultCode)) {
                    log.error("【微信支付】统一下单返回结果失败, outTradeNo={}, resultCode={}, errCode={}, errCodeDes={}", outTradeNo, resultCode, errCode, errCodeDes);
                    return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_REFUND_FAIL).setErrorDescription(errCodeDes);
                }
                return ResultDo.build();
            } finally {
                try {
                    responseEntry.close();
                } catch (IOException e) {
                    responseEntry = null;
                }
            }
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                httpclient = null;
            }
        }
    }

    /**
     * 微信支付退单notify处理
     *
     * @param respMap
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo handlePayRefundNotify(Map<String, String> respMap) throws Exception {
        if (!respMap.containsKey("req_info")) {
            log.error("【微信支付退单】notify通知缺少加密数据, 返回数据: {}", respMap);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_REFUND_REQ_INFO);
        }
        String reqInfoDecode = WechatTools.decryptData(respMap.get("req_info"), wechatPayKey);
        log.info("【微信支付退单】解密后数据: {}", reqInfoDecode);
        Map<String, String> reqInfoMap = WechatTools.xmlToMap(reqInfoDecode);
        String outRefundNo = reqInfoMap.get("out_refund_no");
        String refundId = reqInfoMap.get("refund_id");
        String refundAccount = reqInfoMap.get("refund_account");
        String refundRecvAccout = reqInfoMap.get("refund_recv_accout");
        String refundStatus = reqInfoMap.get("refund_status");
        String settlementRefundFee = reqInfoMap.get("settlement_refund_fee");
        String settlementTotalFee = reqInfoMap.get("settlement_total_fee");
        String successTime = reqInfoMap.get("success_time");
        EpWechatPayRefundPo refundPo = wechatPayRefundRepository.getByOutRefundNo(outRefundNo);
        if (StringTools.isBlank(refundPo.getRefundStatus())) {
            log.info("【微信支付退单】更新退单结果: outRefundNo={}", outRefundNo);
            // 更新退单结果
            wechatPayRefundRepository.handleNotify(outRefundNo,
                    refundId,
                    refundAccount,
                    refundRecvAccout,
                    refundStatus,
                    Integer.valueOf(settlementRefundFee),
                    Integer.valueOf(settlementTotalFee),
                    successTime);
            if (WechatTools.REFUND_STATUS_SUCCESS.equals(refundStatus)) {
                // 更新微信订单交易状态
                wechatUnifiedOrderRepository.refundByOutTradeNo(refundPo.getOutTradeNo());
                orderRefundRepository.successByOutTradeNo(refundPo.getOutTradeNo());
                // 订单更新支付状态
                EpWechatUnifiedOrderPo unifiedOrderPo = wechatUnifiedOrderRepository.findByOutTradeNo(refundPo.getOutTradeNo());
                List<EpWechatUnifiedOrderPo> unifiedOrders = wechatUnifiedOrderRepository.findByOrderId(unifiedOrderPo.getOrderId());
                Boolean allRefund = true;
                for (EpWechatUnifiedOrderPo po : unifiedOrders) {
                    if (po.getOutTradeNo().equals(refundPo.getOutTradeNo())) {
                        continue;
                    }
                    if (WechatTools.TRADE_STATE_SUCCESS.equals(po.getTradeState())) {
                        allRefund = false;
                    }
                }
                if (allRefund) {
                    log.info("【微信支付退单】报名订单已全部退款: orderId={}", unifiedOrderPo.getOrderId());
                    orderRepository.orderRefundFinishedById(unifiedOrderPo.getOrderId());
                }
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
    @Transactional(rollbackFor = Exception.class)
    public ResultDo orderQuery(String transactionId, String outTradeNo, Boolean synFlag) throws Exception {
        log.info("【微信支付】查询订单开始: transactionId={}, outTradeNo={}", transactionId, outTradeNo);
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
        String xml = WechatTools.mapToXmlString(this.fillRequestData(requestMap));
        log.info("【微信支付】查询订单，接口入参={}。", xml);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL_PAY_ORDERQUERY, xml, String.class);
        log.info("【微信支付】查询订单返回: responseEntity={}", outTradeNo, responseEntity);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            log.error("【微信支付】查询订单通信异常: responseEntity={}");
            return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
        }
        String resultStr = responseEntity.getBody();
        if (!synFlag) {
            return ResultDo.build().setResult(resultStr);
        }
        Map<String, String> data = WechatTools.xmlToMap(resultStr);
        log.info("【微信支付】查询订单开始同步本地支付订单: data={}", data);
        // 请求处理失败
        String returnCode = data.get("return_code");
        String returnMsg = data.get("return_msg");
        if (!WechatTools.SUCCESS.equals(returnCode)) {
            log.error("【微信支付】查询订单返回处理失败, outTradeNo={}, returnCode={}, returnMsg={}", outTradeNo, returnCode, returnMsg);
            return ResultDo.build(returnCode).setErrorDescription(returnMsg);
        }
        // 验签
        if (!WechatTools.isSignatureValid(data, wechatPayKey)) {
            log.error("【微信支付】查询订单返回验签失败, outTradeNo={}", outTradeNo);
            return ResultDo.build(MessageCode.ERROR_WECHAT_PAY_RETURN_SIGN);
        }
        // 业务结果失败
        String resultCode = data.get("result_code");
        String errCode = data.get("err_code");
        String errCodeDes = data.get("err_code_des");
        if (!WechatTools.SUCCESS.equals(resultCode)) {
            log.error("【微信支付】查询订单返回结果失败, outTradeNo={}, resultCode={}, errCode={}, errCodeDes={}", outTradeNo, resultCode, errCode, errCodeDes);
            return ResultDo.build(errCode).setErrorDescription(errCodeDes);
        }
        String tradeState = data.get("trade_state");
        String openid = data.get("openid");
        String isSubscribe = data.get("is_subscribe");
        String bankType = data.get("bank_type");
        String retOutTradeNo = data.get("out_trade_no");
        String retTransactionId = data.get("transaction_id");
        String timeEnd = data.get("time_end");
        // 如果是支付成功
        if (WechatTools.TRADE_STATE_SUCCESS.equals(tradeState) && outTradeNo.equals(retOutTradeNo)) {
            log.info("【微信支付】查询订单已支付: outTradeNo={}", outTradeNo);
            EpWechatUnifiedOrderPo unifiedOrderPo = wechatUnifiedOrderRepository.getByOutTradeNo(outTradeNo);
            int num = wechatUnifiedOrderRepository.handleNotify(outTradeNo,
                    returnCode,
                    returnMsg,
                    resultCode,
                    errCode,
                    errCodeDes,
                    isSubscribe,
                    openid,
                    bankType,
                    retTransactionId,
                    timeEnd,
                    tradeState,
                    BizConstant.WECHAT_PAY_QUERY_REMARK);
            if (num == BizConstant.DB_NUM_ONE) {
                // 订单更新支付状态
                Timestamp payConfirmTime = null;
                if (StringTools.isNotBlank(timeEnd)) {
                    try {
                        payConfirmTime = DateTools.dateToTimestamp(DateTools.stringToDate(timeEnd, DateTools.TIME_PATTERN_SESSION));
                    } catch (Exception e) {
                        log.error("payConfirmTime时间格式化错误: timeEnd={}", timeEnd);
                    }
                }
                orderRepository.orderPaidById(unifiedOrderPo.getOrderId(), payConfirmTime);
            }
        }
        // 已退款
        if (WechatTools.TRADE_STATE_REFUND.equals(tradeState) && outTradeNo.equals(retOutTradeNo)) {
            log.info("【微信支付】查询订单已退款: outTradeNo={}", outTradeNo);
            // 更新微信订单交易状态
            wechatUnifiedOrderRepository.refundByOutTradeNo(outTradeNo);
            orderRefundRepository.successByOutTradeNo(outTradeNo);
            // 订单更新支付状态
            EpWechatUnifiedOrderPo unifiedOrderPo = wechatUnifiedOrderRepository.findByOutTradeNo(outTradeNo);
            List<EpWechatUnifiedOrderPo> unifiedOrders = wechatUnifiedOrderRepository.findByOrderId(unifiedOrderPo.getOrderId());
            Boolean allRefund = true;
            for (EpWechatUnifiedOrderPo po : unifiedOrders) {
                if (po.getOutTradeNo().equals(outTradeNo)) {
                    continue;
                }
                if (WechatTools.TRADE_STATE_SUCCESS.equals(po.getTradeState())) {
                    allRefund = false;
                }
            }
            if (allRefund) {
                log.info("【微信支付】查询订单报名订单已全部退款: orderId={}", unifiedOrderPo.getOrderId());
                orderRepository.orderRefundFinishedById(unifiedOrderPo.getOrderId());
            }
        }
        return ResultDo.build();
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
     * 下载对账单
     *
     * @param billDate
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo downloadbill(Date billDate) {
        ResultDo resultDo = ResultDo.build();
        String billDateStr = DateTools.toString(billDate, DateTools.DATE_FMT_0);
        try {
            // 判断当天结账数据是否存在
            int billDateInt = Integer.valueOf(billDateStr);
            Optional<EpWechatPayBillPo> existBill = wechatPayBillRepository.getByBillDate(billDateInt);
            if (existBill.isPresent() && WechatTools.SUCCESS.equals(existBill.get().getReturnCode())) {
                log.info("【下载对账单】已存在本地对账记录, billDateStr={}", billDateStr);
                return resultDo.setError(MessageCode.ERROR_WECHAT_PAY_DOWNLOAD_BILL_EXIST);
            }
            // 删除
            if (existBill.isPresent()) {
                Long billId = existBill.get().getId();
                wechatPayBillRepository.delete(billId);
                wechatPayBillDetailRepository.deleteByBillId(billId);
            }
            EpWechatPayBillPo billPo = new EpWechatPayBillPo();
            billPo.setBillDate(billDateInt);
            wechatPayBillRepository.insert(billPo);
            Map<String, String> requestMap = Maps.newHashMap();
            requestMap.put("appid", xcxMemberAppid);
            requestMap.put("mch_id", wechatPayMchid);
            requestMap.put("nonce_str", WechatTools.generateUUID());
            requestMap.put("bill_date", billDateStr);
            requestMap.put("bill_type", "ALL");
            requestMap.put("sign", WechatTools.generateSignature(requestMap, wechatPayKey));
            String xml = WechatTools.mapToXmlString(this.fillRequestData(requestMap));
            log.info("【下载对账单】接口入参={}", xml);
            ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(URL_DOWNLOAD_BILL, xml, byte[].class);
            if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                log.error("【下载对账单】微信请求失败, httpStatus={}", responseEntity.getStatusCode());
                return ResultDo.build(MessageCode.ERROR_WECHAT_HTTP_REQUEST);
            }
            byte[] body = responseEntity.getBody();
            log.info("【下载对账单】接口返回 Byte[].length={}", body.length);
            // 写入文本文件
            String filePath = "/home/ep/bill/bill_" + billDateStr + ".txt";
            log.info("【下载对账单】写入文本文件 file={}", filePath);
            OutputStream os = new FileOutputStream(filePath);
            InputStream is = new ByteArrayInputStream(body);
            byte[] buff = new byte[BizConstant.NUM_ONE_MB];
            int len = BizConstant.DB_NUM_ZERO;
            while ((len = is.read(buff)) != BizConstant.DB_MINUS_ONE) {
                os.write(buff, BizConstant.DB_NUM_ZERO, len);
            }
            is.close();
            os.close();
            // 解析文本
            log.info("【下载对账单】解析文本 file={}", filePath);
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            Boolean success = true;
            StringBuffer errorMsg = new StringBuffer();
            int lineNum = BizConstant.DB_NUM_ZERO;
            int dataNum = BizConstant.DB_NUM_ZERO;
            String[] lastStrSplit = null;
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                lineNum++;
                log.debug(lineTxt);
                if (lineNum == BizConstant.DB_NUM_ONE && lineTxt.startsWith("<xml>")) {
                    success = false;
                }
                if (!success) {
                    errorMsg.append(lineTxt);
                    continue;
                }
                if (success && lineTxt.startsWith("`")) {
                    lastStrSplit = lineTxt.replaceAll("`", "").trim().split(",");
                    if (lastStrSplit.length < 24) {
                        continue;
                    }
                    dataNum++;
                    EpWechatPayBillDetailPo detailPo = new EpWechatPayBillDetailPo();
                    int dataIndex = 0;
                    detailPo.setTransactionTime(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setAppid(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setMchId(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setSubMchId(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setDeviceNo(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setTransactionId(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setOutTradeNo(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setOpenid(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setTradeType(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setTradeState(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setBankType(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setFeeType(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setTotalFee(new BigDecimal(lastStrSplit[dataIndex]));
                    dataIndex++;
                    detailPo.setCouponFee(new BigDecimal(lastStrSplit[dataIndex]));
                    dataIndex++;
                    detailPo.setRefundId(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setOutRefundNo(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setRefundFee(new BigDecimal(lastStrSplit[dataIndex]));
                    dataIndex++;
                    detailPo.setRefundCouponFee(new BigDecimal(lastStrSplit[dataIndex]));
                    dataIndex++;
                    detailPo.setRefundType(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setRefundStatus(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setBody(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setAttach(lastStrSplit[dataIndex]);
                    dataIndex++;
                    detailPo.setPoundage(new BigDecimal(lastStrSplit[dataIndex]));
                    dataIndex++;
                    detailPo.setPoundageRate(lastStrSplit[dataIndex]);
                    detailPo.setBillId(billPo.getId());
                    EpWechatUnifiedOrderPo unifiedOrderPo = wechatUnifiedOrderRepository.getByOutTradeNo(detailPo.getOutTradeNo());
                    if (unifiedOrderPo != null) {
                        EpOrderPo orderPo = orderRepository.getById(unifiedOrderPo.getOrderId());
                        detailPo.setOgnId(orderPo.getOgnId());
                        detailPo.setCourseId(orderPo.getCourseId());
                        detailPo.setClassId(orderPo.getClassId());
                        detailPo.setOrderId(orderPo.getId());
                    }
                    wechatPayBillDetailRepository.insert(detailPo);
                }
            }
            if (!success) {
                Map<String, String> respMap = WechatTools.xmlToMap(errorMsg.toString());
                log.info("【下载对账单】返回异常: respMap={}", respMap);
                wechatPayBillRepository.handleFail(billPo.getId(), respMap.get("return_code"), respMap.get("return_msg"));
            } else {
                log.info("【下载对账单】数据解析成功: lineNum={}, dataNum={}, lastText={}", lineNum, dataNum, lastStrSplit);
                int dataIndex = 0;
                int totalNum = Integer.parseInt(lastStrSplit[dataIndex]);
                dataIndex++;
                BigDecimal totalAmount = new BigDecimal(lastStrSplit[dataIndex]);
                dataIndex++;
                BigDecimal totalRefundAmount = new BigDecimal(lastStrSplit[dataIndex]);
                dataIndex++;
                BigDecimal totalCouponRefundAmount = new BigDecimal(lastStrSplit[dataIndex]);
                dataIndex++;
                BigDecimal totalPoundage = new BigDecimal(lastStrSplit[dataIndex]);
                wechatPayBillRepository.handleSuccess(billPo.getId(), totalNum, totalAmount, totalRefundAmount, totalCouponRefundAmount, totalPoundage);
            }
            br.close();
            fr.close();
            return resultDo;
        } catch (Exception e) {
            log.error("【下载对账单】解析失败, billDateStr={}", billDateStr, e);
            return resultDo.setError(MessageCode.ERROR_SYSTEM).setErrorDescription(e.getMessage());
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
