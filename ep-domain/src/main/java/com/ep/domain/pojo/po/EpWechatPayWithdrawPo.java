/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.pojo.po;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpWechatPayWithdrawStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * 微信支付提现申请
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpWechatPayWithdrawPo extends AbstractBasePojo {

    private Long id;
    private Long courseId;
    private Long classId;
    private BigDecimal totalAmount;
    private Integer orderNum;
    private Integer offlinePayNum;
    private Integer wechatPayNum;
    private BigDecimal wechatPayFee;
    private BigDecimal withdrawFee;
    private String withdrawNo;
    private EpWechatPayWithdrawStatus status;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String remark;
    private Boolean delFlag;
    private Long version;

}
