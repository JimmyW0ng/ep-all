/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


/**
 * 微信支付统一订单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpWechatUnifiedOrderPo extends AbstractBasePojo {

    private Long id;
    private Long orderId;
    private String appid;
    private String mchId;
    private String outTradeNo;
    private String body;
    private Integer totalFee;
    private String spbillCreateIp;
    private String tradeType;
    private String tradeState;
    private String returnCode;
    private String returnMsg;
    private String resultCode;
    private String errCode;
    private String errCodeDes;
    private String prepayId;
    private String notifyReturnCode;
    private String notifyReturnMsg;
    private String notifyResultCode;
    private String notifyErrCode;
    private String notifyErrCodeDes;
    private String isSubscribe;
    private String openid;
    private String bankType;
    private String transactionId;
    private String timeEnd;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String remark;
    private Boolean delFlag;
    private Long version;

}
