package com.ep.domain.pojo.bo;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpOrganClassScheduleStatus;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description: 随堂excelBo
 * @Author: CC.F
 * @Date: 13:22 2018/4/17/017
 */
@Data
public class ClassScheduleExcelBo extends AbstractBasePojo {
    private String childNickName;
    private String childTrueName;
    private EpOrganClassScheduleStatus status;
    private Timestamp startTime;
    private Integer catalogIndex;
    private Timestamp createAt;

    public String getStatusText() {
        return SpringComponent.messageSource("EpOrganClassScheduleStatus." + status.getLiteral());
    }

    public String getFmtStartTime() {
        return DateTools.timestampToString(startTime, DateTools.TIME_PATTERN);
    }

    public String getFmtCreateAt() {
        return DateTools.timestampToString(createAt, DateTools.TIME_PATTERN);
    }
}
