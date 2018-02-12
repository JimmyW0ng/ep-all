package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganClassPo;
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
    private Long classCatelogId;

    public Boolean getViewCommentFlag() {
        return this.childEvaluatedNum >= super.getEnteredNum();
    }

}
