package com.ep.domain.pojo.dto;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.bo.MemberChildBo;
import com.ep.domain.pojo.bo.MemberChildTagAndCommentBo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.pojo.po.EpOrganClassCatelogPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganClassCatelogCommentDto extends AbstractBasePojo {

    private EpOrganClassCatelogPo classCatelog;
    private List<MemberChildBo> childList;
    private List<OrganCourseTagBo> courseTagList;
    private List<MemberChildTagAndCommentBo> childTagAndCommentList;

}
