package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrderRefundPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 13:31 2018/5/22/022
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderRefundBo extends EpOrderRefundPo {
    private String ognName;
}
