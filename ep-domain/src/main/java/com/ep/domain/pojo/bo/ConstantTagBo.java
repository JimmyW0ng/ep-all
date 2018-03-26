package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpConstantTagPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 14:13 2018/3/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstantTagBo extends EpConstantTagPo {
    private Boolean usedFlag;
    private Long usedOrganCourseTag;
    private String ognName;
}
