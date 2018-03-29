package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpConstantCatalogPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 22:55 2018/2/5
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ConstantCatalogBo extends EpConstantCatalogPo {
    private String parentName;
}
