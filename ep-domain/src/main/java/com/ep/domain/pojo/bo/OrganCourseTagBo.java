package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganCourseTagPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:14 2018/2/15
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrganCourseTagBo extends EpOrganCourseTagPo {
    private String tagName;
    private Boolean ognFlag;
}
