package com.ep.domain.pojo.bo;

import com.ep.common.tool.NumberTools;
import com.ep.domain.pojo.po.EpOrderPo;
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
    private Boolean courseCommentFlag;
    private Integer lastCatelogIndex;
    private String label;

    /**
     * 返回金额格式化
     *
     * @return
     */
    public String getPirzeFormat() {
        return NumberTools.getFormatPrice(super.getPrize());
    }

}
