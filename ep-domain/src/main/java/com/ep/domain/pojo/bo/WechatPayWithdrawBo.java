package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpWechatPayWithdrawPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:26 2018/5/16
 */
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatPayWithdrawBo extends EpWechatPayWithdrawPo {
    private String courseName;
    private String className;
}
