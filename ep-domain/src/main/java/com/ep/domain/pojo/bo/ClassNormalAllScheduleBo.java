package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpOrganClassPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClassNormalAllScheduleBo extends EpOrganClassPo {

    private Long classId;
    private String courseName;
    private Long classCatalogId;
    private Integer catalogIndex;
    private String catalogTitle;
    private String catalogDesc;
    private Timestamp startTime;
    private Integer childNum;
    private Integer childEvaluatedNum;

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
