package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 14:56 2018/3/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RectifyOrganClassCatalogBo extends EpOrganClassCatalogPo {
    private Boolean rectifyFlag;
}
