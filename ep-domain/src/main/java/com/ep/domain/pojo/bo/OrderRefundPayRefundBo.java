package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrderRefundPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 9:31 2018/5/23/023
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderRefundPayRefundBo extends EpOrderRefundPo {
    private String transactionId;
    private String outRefundNo;
    private String totalFee;
    private String refundFee;
    private String resultCode;
    private String refundRecvAccout;
    private String refundStatus;
    private String successTime;
}
