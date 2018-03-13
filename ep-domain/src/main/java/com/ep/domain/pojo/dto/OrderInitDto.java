package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.bo.MemberCourseOrderInitBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInitDto extends AbstractBasePojo {

    private List<MemberCourseOrderInitBo> children;
    private Integer childrenNum;
    private Integer childrenNumLimit;

}
