package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.Data;

import java.util.List;

@Data
public class MemberChildAbstractBo extends AbstractBasePojo {

    private Long childId;
    private String childNickName;
    private String currentSchool;
    private String currentClass;
    private String sign;
    private Long totalOrder;
    private Long totalHonor;
    private List<MemberChildTagBo> tags;
}
