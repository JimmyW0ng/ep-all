package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    /**
     * 总已支付订单数
     */
    private Long totalPaidOrderNum;
    /**
     * 未提现订单数
     */
    private Long waitWithdrawOrderNum;
    /**
     * 最近一次提现金额
     */
    private Integer lastWithdrawAmount;
    /**
     * 最近一次提现订单数
     */
    private Integer lastWithdrawOrderNum;
    /**
     * 最近一次提现时间
     */
    private Integer lastWithdrawTime;
    /**
     * 最近一次提现状态
     */
    private Integer lastWithdrawStatus;
    /**
     * ep_wechat_pay_withdraw表主键
     */
    private Long payWithdrawId;

}
