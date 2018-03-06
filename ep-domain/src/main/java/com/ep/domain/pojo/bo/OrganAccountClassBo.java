package com.ep.domain.pojo.bo;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganAccountClassBo extends EpOrganClassPo {

    private String ognName;
    private String courseName;
    private Integer childEvaluatedNum;
    private String mainPicUrl;
    private Long classCatalogId;
    private Integer catalogIndex;

    public Boolean getWiatCommentFlag() {
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

}
