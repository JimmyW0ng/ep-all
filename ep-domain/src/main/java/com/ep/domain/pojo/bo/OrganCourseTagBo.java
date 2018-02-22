package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganCourseTagPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganCourseTagBo extends EpOrganCourseTagPo {

    private String tagName;

}
