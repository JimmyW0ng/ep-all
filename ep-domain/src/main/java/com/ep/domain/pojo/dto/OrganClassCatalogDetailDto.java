package com.ep.domain.pojo.dto;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.bo.MemberChildTagBo;
import com.ep.domain.pojo.bo.OrganClassCatalogBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganClassCatalogDetailDto extends AbstractBasePojo {

    private List<MemberChildTagBo> tags;
    private List<OrganClassCatalogBo> classCatalogs;

}
