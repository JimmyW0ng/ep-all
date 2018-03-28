package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpMemberChildCommentPo;
import com.ep.domain.repository.domain.enums.EpOrganClassScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@ToString(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberChildCommentBo extends EpMemberChildCommentPo {

    private String nickName;
    private String ognName;
    private String avatar;
    private String childTrueName;
    private String courseName;
    private String className;
    private String classCatalogTitle;
    private Long replyId;
    private String contentReply;
    private Long orderId;
    private EpOrganClassScheduleStatus scheduleStatus;
    private Timestamp startTime;
    private Integer catalogIndex;

    public String getCommentTimeFormat() {
        return DateTools.formatDatetoString(super.getCreateAt(), DateTools.DATE_FMT_4);
    }

}
