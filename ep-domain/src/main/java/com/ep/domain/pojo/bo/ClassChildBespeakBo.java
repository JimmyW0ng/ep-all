package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpMemberChildPo;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString(callSuper = true)
public class ClassChildBespeakBo extends EpMemberChildPo {

    private Long classScheduleId;
    private Long classId;
    private Long childId;
    private String courseName;
    private String catalogTitle;
    private String catalogDesc;
    private Timestamp startTime;
    private Boolean evaluateFlag;

    public Long getStartTimeStamp() {
        if (this.getStartTime() != null) {
            return this.getStartTime().getTime();
        }
        return null;
    }

    public String getStartTimeFormat() {
        return DateTools.formatDatetoString(this.getStartTime(), DateTools.DATE_FMT_13);
    }
}
