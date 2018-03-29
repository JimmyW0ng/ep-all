package com.ep.domain.pojo.bo;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganClassCommentPo;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class OrganClassCommentBo extends EpOrganClassCommentPo {

    private String childNickName;
    private String className;
    private String childAvatar;
    private Integer honorNum;
    private List<String> pics;
    private String ognName;
    private String courseName;

    public Boolean getExistHonor() {
        if (this.honorNum == null || this.honorNum.equals(BizConstant.DB_NUM_ZERO)) {
            return false;
        }
        return true;
    }
}
