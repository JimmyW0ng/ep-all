package com.ep.domain.pojo.dto;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.bo.OrganClassCatalogCommentBo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganClassCatalogCommentDto extends AbstractBasePojo {

    private EpOrganClassCatalogPo classCatalog;
    private List<OrganClassCatalogCommentBo> childList;
    private List<OrganCourseTagBo> courseTagList;

}
