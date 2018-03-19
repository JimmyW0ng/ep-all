package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganVipPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:58 2018/3/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganVipBo extends EpOrganVipPo {
    private Long mobile;
    private String childNickName;
}
