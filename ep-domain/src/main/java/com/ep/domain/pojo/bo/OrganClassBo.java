package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 10:02 2018/2/11
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrganClassBo extends EpOrganClassPo {
    private String courseName;
    private List<EpOrganClassCatalogPo> organClassCatalogPos;
}
