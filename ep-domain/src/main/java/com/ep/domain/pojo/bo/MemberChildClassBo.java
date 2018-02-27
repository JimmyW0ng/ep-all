package com.ep.domain.pojo.bo;

import com.ep.common.tool.NumberTools;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberChildClassBo extends EpOrderPo {

    private String ognName;
    private String courseName;
    private Integer courseNum;
    private Integer honorNum;
    private Integer scheduleCommentNum;
    @JsonIgnore
    private Boolean courseCommentFlag;
    private Integer lastCatelogIndex;
    private String label;
    private String mainPicUrl;

    /**
     * 返回金额格式化
     *
     * @return
     */
    public String getPirzeFormat() {
        return NumberTools.getFormatPrice(super.getPrize());
    }

    /**
     * 待评价课程标识
     *
     * @return
     */
    public Boolean getWaitCommentFlag() {
        if (courseCommentFlag != null && !courseCommentFlag && super.getStatus().equals(EpOrderStatus.end)) {
            return true;
        }
        return false;
    }

}
