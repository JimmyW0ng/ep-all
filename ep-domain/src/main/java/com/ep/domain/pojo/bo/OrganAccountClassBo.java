package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrganAccountClassBo extends EpOrganClassPo {

    private Long classId;
    private String courseName;
    private String mainPicUrl;
    private Long classCatalogId;
    private String catalogTitle;
    private String catalogDesc;
    private Timestamp startTime;
    private Integer childNum;
    private Integer childEvaluatedNum;

    public Boolean getWaitCommentFlag() {
        if (EpOrganClassStatus.opening.equals(super.getStatus())) {
            if (this.childEvaluatedNum == null) {
                return false;
            }
            return this.childEvaluatedNum < super.getEnteredNum();
        }
        return false;
    }

    public Boolean getViewCommentFlag() {
        if (EpOrganClassStatus.opening.equals(super.getStatus())) {
            if (this.childEvaluatedNum == null) {
                return false;
            }
            return this.childEvaluatedNum >= super.getEnteredNum();
        } else if (EpOrganClassStatus.end.equals(super.getStatus())) {
            if (this.childEvaluatedNum == null) {
                return false;
            }
            return this.childEvaluatedNum > BizConstant.DB_NUM_ZERO;
        }
        return false;
    }

    public Long getStartTimeStamp() {
        if (this.getStartTime() != null) {
            return this.getStartTime().getTime();
        }
        return null;
    }

    public String getStartTimeFormat() {
        return DateTools.formatDatetoString(this.getStartTime(), DateTools.DATE_FMT_4);
    }
}
