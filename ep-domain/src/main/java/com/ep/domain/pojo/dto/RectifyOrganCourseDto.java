package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.bo.RectifyOrganClassCatalogBo;
import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 商家后台紧急修改课程dto
 * @Author: CC.F
 * @Date: 17:41 2018/2/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RectifyOrganCourseDto extends AbstractBasePojo {
    private EpOrganCoursePo organCoursePo;
    private List<EpConstantTagPo> constantTagPos;
    private String mainpicUrlPreCode;
    private List<String> courseDescPicPreCodes;
    private List<RectifyOrganClassCatalogBo> rectifyOrganClassCatalogBos;
    private List<EpOrganClassPo> organClassPos;

}
