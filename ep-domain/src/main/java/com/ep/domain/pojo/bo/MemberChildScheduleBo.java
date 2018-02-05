package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpOrderPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberChildScheduleBo extends EpOrderPo {

    private String ognName;
    private String courseName;
    private Integer courseNum;
    private Integer catelogIndex;
    private String label;
    @JsonIgnore
    private Timestamp startTime;
    private String mainPicUrl;

    /**
     * 开课时间格式化
     *
     * @return
     */
    public String getStartTimeFormat() {
        return DateTools.formatDatetoString(this.startTime, DateTools.DATE_FMT_13);
    }

}
