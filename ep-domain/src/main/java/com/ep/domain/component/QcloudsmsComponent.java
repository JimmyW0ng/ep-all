package com.ep.domain.component;

import com.ep.domain.constant.BizConstant;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Description: 腾讯云短信组件
 * @Author: CC.F
 * @Date: 14:50 2018/4/23/023
 */
@Slf4j
@Component
public class QcloudsmsComponent {
    @Value("${qcloudsms.appid}")
    private String appid;
    @Value("${qcloudsms.appkey}")
    private String appkey;
    @Value("${qcloudsms.smsSign}")
    private String smsSign;


    /**
     * 指定模板ID单发短信
     *
     * @param templateId
     * @param phoneNumber
     * @param params
     */
    public void singleSend(int templateId, String phoneNumber, String[] params) {
        try {
            SmsSingleSender ssender = new SmsSingleSender(Integer.parseInt(appid), appkey);
            // 签名参数未提供或者为空时，会使用默认签名发送短信
            SmsSingleSenderResult result = ssender.sendWithParam(BizConstant.QCLOUDSMS_NATION_CODE, phoneNumber,
                    templateId, params, smsSign, "", "");
            System.out.print(result);
        } catch (HTTPException e) {
            log.error("[腾讯云短信]，指定模板ID单发短信失败。", e);
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            log.error("[腾讯云短信]，指定模板ID单发短信失败。", e);
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            log.error("[腾讯云短信]，指定模板ID单发短信失败。", e);
            // 网络IO错误
            e.printStackTrace();
        }
    }

    /**
     * 单发短信
     *
     * @param phoneNumber
     * @param msg         必须套用申请通过的模板
     */
    public void singleSend(String phoneNumber, String msg) {
        try {
            SmsSingleSender ssender = new SmsSingleSender(Integer.parseInt(appid), appkey);
            SmsSingleSenderResult result = ssender.send(0, BizConstant.QCLOUDSMS_NATION_CODE, phoneNumber,
                    msg, "", "");
            System.out.print(result);
        } catch (HTTPException e) {
            log.error("[腾讯云短信]，单发短信失败。", e);
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            log.error("[腾讯云短信]，单发短信失败。", e);
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            log.error("[腾讯云短信]，单发短信失败。", e);
            // 网络IO错误
            e.printStackTrace();
        }
    }

    /**
     * 指定模板ID群发
     *
     * @param templateId
     * @param phoneNumbers
     * @param params
     */
    public void multiSend(int templateId, ArrayList<String> phoneNumbers, ArrayList<String> params) {
        try {
            SmsMultiSender msender = new SmsMultiSender(Integer.parseInt(appid), appkey);
            SmsMultiSenderResult result = msender.sendWithParam(BizConstant.QCLOUDSMS_NATION_CODE, phoneNumbers, templateId, params, smsSign, "", "");
            System.out.print(result);
        } catch (HTTPException e) {
            log.error("[腾讯云短信]，指定模板ID群发短信失败。", e);
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            log.error("[腾讯云短信]，指定模板ID群发短信失败。", e);
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            log.error("[腾讯云短信]，指定模板ID群发短信失败。", e);
            // 网络IO错误
            e.printStackTrace();
        }
    }

    /**
     * 群发短信
     *
     * @param phoneNumbers
     * @param msg          必须套用申请通过的模板
     */
    public void multiSend(ArrayList<String> phoneNumbers, String msg) {
        try {
            SmsMultiSender msender = new SmsMultiSender(Integer.parseInt(appid), appkey);
            SmsMultiSenderResult result = msender.send(0, BizConstant.QCLOUDSMS_NATION_CODE, phoneNumbers,
                    msg, "", "");
            System.out.print(result);
            System.out.print(result);
        } catch (HTTPException e) {
            log.error("[腾讯云短信]，群发短信失败。", e);
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            log.error("[腾讯云短信]，群发短信失败。", e);
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            log.error("[腾讯云短信]，群发短信失败。", e);
            // 网络IO错误
            e.printStackTrace();
        }
    }
}
