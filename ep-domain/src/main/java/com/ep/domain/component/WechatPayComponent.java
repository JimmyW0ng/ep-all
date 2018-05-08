package com.ep.domain.component;

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
    private static final String URL_SANDBOX_GET_KEY = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";

    @Value("${wechat.xcx.member.appid}")
    private String xcxMemberAppid;
    @Value("${wechat.pay.mchid}")
    private String wechatPayMchid;
    // @Value("${wechat.pay.key}")
    private String wechatPayKey = "3b4cfcbafaa069675e0b076d6dd8a759";

    @Autowired
    private RestTemplate restTemplate;

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
        reqData.put("mch_id", "");
        reqData.put("nonce_str", WechatTools.generateUUID());
        reqData.put("sign", WechatTools.generateSignature(reqData, wechatPayKey));
        return reqData;
    }

    /**
     * 小程序客户端微信支付
     *
     * @param body
     * @param detail
     * @param attach
     * @param out_trade_no
     * @param total_fee
     * @param spbill_create_ip
     * @param time_start
     * @param time_expire
     * @param openid
     * @return
     * @throws Exception
     */
    public ResultDo xcxUnifiedorder(String body, String detail, String attach, String out_trade_no, int total_fee,
                                    String spbill_create_ip, String time_start, String time_expire, String openid) throws Exception {
        Map<String, String> requestMap = Maps.newHashMap();
        requestMap.put("body", body);
        if (StringTools.isNotBlank(detail)) {
            requestMap.put("detail", detail);
        }
        if (StringTools.isNotBlank(attach)) {
            requestMap.put("attach", attach);
        }
        requestMap.put("out_trade_no", out_trade_no);
        requestMap.put("total_fee", String.valueOf(total_fee));
        requestMap.put("spbill_create_ip", spbill_create_ip);
        if (StringTools.isNotBlank(time_start)) {
            requestMap.put("time_start", time_start);
        }
        if (StringTools.isNotBlank(time_expire)) {
            requestMap.put("time_expire", time_expire);
        }
        requestMap.put("openid", openid);
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String xml = WechatTools.mapToXmlString(this.fillRequestData(requestMap));
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, xml, String.class);
        System.out.println(responseEntity.getBody());
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
