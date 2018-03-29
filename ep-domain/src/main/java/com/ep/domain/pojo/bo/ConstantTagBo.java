package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpConstantTagPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 14:13 2018/3/5
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ConstantTagBo extends EpConstantTagPo {
    private Boolean usedFlag;
    private Long usedOrganCourseTag;
    private String ognName;
}
