package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpWechatUnifiedOrderPo;
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
}
