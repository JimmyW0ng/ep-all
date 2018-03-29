package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpMemberChildHonorPo;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class MemberChildHonorBo extends EpMemberChildHonorPo {

    private Long courseId;
    private String courseName;
    private String coursePic;
    private String ognName;
    private String childNickName;
    private String childTrueName;
    private String className;

    public String getAwardTimeFormat() {
        return DateTools.dateToString(super.getCreateAt(), DateTools.DATE_FMT_4);
    }
}
