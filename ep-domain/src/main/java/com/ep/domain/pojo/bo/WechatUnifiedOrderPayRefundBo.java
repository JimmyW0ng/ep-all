package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpWechatUnifiedOrderPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:统一下单后退款bo
 * @Author: CC.F
 * @Date: 6:13 2018/5/16
 */
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatUnifiedOrderPayRefundBo extends EpWechatUnifiedOrderPo {
    private String outRefundNo;
    private Integer refundFee;
    private String refundId;
    private String refundStatus;
    private String successTime;
    private String refundRecvAccout;
    private String refundAccount;
    private String orderStatus;
}
