package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.bo.OrganCourseBo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganCourseDto extends AbstractBasePojo {

    private OrganCourseBo course;
    private List<EpOrganClassPo> classes;
    private List<EpOrganAccountPo> team;
    private List<OrganClassCommentBo> commenets;
    private Long successOrders;

}
