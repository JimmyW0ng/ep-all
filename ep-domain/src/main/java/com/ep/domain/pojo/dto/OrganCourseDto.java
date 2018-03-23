package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.bo.OrganClassEnterBo;
import com.ep.domain.pojo.bo.OrganCourseBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganCourseDto extends AbstractBasePojo {

    private OrganCourseBo course;
    private List<OrganClassEnterBo> classes;
    private List<OrganAccountBo> team;
    private List<OrganClassCommentBo> comments;
    private Long totalCommentNum;

}
