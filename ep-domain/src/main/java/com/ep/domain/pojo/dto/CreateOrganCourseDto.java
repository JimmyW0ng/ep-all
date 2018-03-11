package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.bo.OrganClassBo;
import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 商家后台创建课程dto
 * @Author: CC.F
 * @Date: 17:41 2018/2/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganCourseDto extends AbstractBasePojo {
    private EpOrganCoursePo organCoursePo;
    private List<OrganClassBo> organClassBos;
    private List<EpConstantTagPo> constantTagPos;
    private String mainpicUrlPreCode;

}
