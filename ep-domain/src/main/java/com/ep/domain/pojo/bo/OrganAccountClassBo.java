package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganAccountClassBo extends EpOrganClassPo {

    private String ognName;
    private String courseName;
    private String mainPicUrl;
    private Long classCatalogId;
    private String catalogTitle;
    private String catalogDesc;
    private Timestamp startTime;
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

    public String getStatusText() {
        if (EpOrganClassStatus.save.equals(super.getStatus())) {
            return "报名中";
        } else if (EpOrganClassStatus.online.equals(super.getStatus())) {
            return "确认开班";
        } else if (EpOrganClassStatus.opening.equals(super.getStatus())) {
            return "学习中";
        } else if (EpOrganClassStatus.end.equals(super.getStatus())) {
            return "已结束";
        }
        return null;
    }

    public String getStartTimeFormat() {
        return DateTools.formatDatetoString(this.getStartTime(), DateTools.DATE_FMT_4);
    }
}
