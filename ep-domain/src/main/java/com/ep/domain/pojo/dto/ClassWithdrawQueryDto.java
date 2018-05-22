package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpWechatPayWithdrawStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Description: 商户提现分页列表dto
 * @Author: CC.F
 * @Date: 13:42 2018/5/17/017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassWithdrawQueryDto extends AbstractBasePojo {
    private Long courseId;
    private String courseName;
    private Long classId;
    private String className;
    private Long orderId;
    /**
     * 微信已支付订单数
     */
    private Long wechatPaidOrderNum;
    /**
     * 微信已支付总订单金额
     */
    private BigDecimal sumWechatPaidTotalFee;
    /**
     * 微信已支付总手续费
     */
    private BigDecimal sumWechatPaidPoundage;

    /**
     * ep_wechat_pay_withdraw表id
     */
    private Long WechatPayWithdrawId;
    /**
     * 总已支付订单数
     */
    private Integer totalWechatPaidOrderNum;
    /**
     * 未提现订单数
     */
    private Integer waitWithdrawOrderNum;
    /**
     * 最近一次提现金额
     */
    private BigDecimal lastWithdrawAmount;
    /**
     * 最近一次提现订单数
     */
    private Integer lastWithdrawOrderNum;
    /**
     * 最近一次提现时间
     */
    private Timestamp lastWithdrawTime;
    /**
     * 最近一次提现状态
     */
    private EpWechatPayWithdrawStatus lastWithdrawStatus;
    /**
     * ep_wechat_pay_withdraw表主键
     */
    private Long payWithdrawId;

}
