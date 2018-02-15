package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganCourseTagPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:14 2018/2/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganCourseTagBo extends EpOrganCourseTagPo {
    private String tagName;
}
