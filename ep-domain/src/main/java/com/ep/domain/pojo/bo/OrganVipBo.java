package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganVipPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:58 2018/3/19
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrganVipBo extends EpOrganVipPo {
    private Long mobile;
    private String childNickName;
}
