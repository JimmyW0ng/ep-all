package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpWechatUnifiedOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderPayStatus;
import com.ep.domain.repository.domain.enums.EpOrderPayType;
import com.ep.domain.repository.domain.enums.EpOrderRefundStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 16:18 2018/5/10/010
 */
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatUnifiedOrderBo extends EpWechatUnifiedOrderPo {
    private String courseName;
    private String className;
    private Long orderRefundId;
    private EpOrderRefundStatus orderRefundApplyStstus;
    private EpOrderPayStatus orderPayStstus;
    private EpOrderPayType orderPayType;
}
