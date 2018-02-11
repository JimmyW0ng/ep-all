package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganClassCatelogPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 10:02 2018/2/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpOrganClassBo extends EpOrganClassPo {
    private List<EpOrganClassCatelogPo> organClassCatelogPos;
}
