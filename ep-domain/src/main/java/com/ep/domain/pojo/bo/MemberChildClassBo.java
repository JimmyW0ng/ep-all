package com.ep.domain.pojo.bo;

import com.ep.common.tool.NumberTools;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderPayType;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MemberChildClassBo extends EpOrderPo {

    private String ognName;
    private String courseName;
    private String className;
    private EpOrganCourseCourseType courseType;
    private Integer courseNum;
    private Integer honorNum;
    private Integer scheduleCommentNum;
    @JsonIgnore
    private Boolean courseCommentFlag;
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

    public String getCourseTypeText() {
        if (EpOrganCourseCourseType.training.equals(this.getCourseType())) {
            return "课程";
        } else if (EpOrganCourseCourseType.activity.equals(this.getCourseType())) {
            return "活动";
        }
        return null;
    }

    /**
     * 支付类型文案
     *
     * @return
     */
    public String getPayTypeFormat() {
        if (super.getPayType() != null) {
            if (EpOrderPayType.offline.equals(super.getPayType())) {
                return "商户支付途径支付";
            } else if (EpOrderPayType.wechat_pay.equals(super.getPayType())) {
                return "平台在线支付";
            }
        }
        return null;
    }

}
