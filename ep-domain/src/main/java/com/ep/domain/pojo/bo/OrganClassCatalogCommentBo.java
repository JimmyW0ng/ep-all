package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpOrganClassScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganClassCatalogCommentBo extends AbstractBasePojo {

    private Long classScheduleId;
    private String catalogTitle;
    private String catalogDesc;
    private Boolean evaluateFlag;
    private Long childId;
    private String childNickName;
    private String avatar;
    private String comment;
    private List<MemberChildTagBo> tags;
    private EpOrganClassScheduleStatus status;

    public Boolean getCommentFlag() {
        if (Boolean.FALSE.equals(evaluateFlag) && (EpOrganClassScheduleStatus.normal.equals(status) || EpOrganClassScheduleStatus.late.equals(status))) {
            return true;
        }
        return false;
    }

    public String getCommentText() {
        if (Boolean.TRUE.equals(evaluateFlag)) {
            return "已评价";
        } else if (EpOrganClassScheduleStatus.normal.equals(status) || EpOrganClassScheduleStatus.late.equals(status)) {
            return "未评价";
        } else if (EpOrganClassScheduleStatus.absent.equals(status)) {
            return "缺勤";
        } else if (EpOrganClassScheduleStatus.leave.equals(status)) {
            return "请假";
        }
        return null;
    }

}
