package com.ep.domain.service;

//import com.ep.common.tool.sdk.WXPayConstants;
//import com.ep.common.tool.sdk.WXPayUtil;
//import com.ep.common.tool.wechat.WechatTools;

import com.ep.common.tool.StringTools;
import com.ep.common.tool.wechat.WechatTools;
import com.ep.domain.pojo.ResultDo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class WechatPayService {
    @Value("${wechat.xcx.member.appid}")
    private String wechatXcxMemberAppid;
    @Value("${wechat.pay.key}")
    private String wechatPayKey;
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
        reqData.put("appid", wechatXcxMemberAppid);
        reqData.put("mch_id", "");
        reqData.put("nonce_str", WechatTools.generateUUID());
        reqData.put("sign", WechatTools.generateSignature(reqData, wechatPayKey));
        return reqData;
    }

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
}
