package com.ep.domain.pojo.bo;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpOrganClassCommentPo;
import lombok.Data;

import java.util.List;

@Data
public class OrganClassCommentBo extends EpOrganClassCommentPo {

    private String childNickName;
    private String className;
    private String childAvatar;
    private Integer honorNum;
    private List<String> pics;

    public Boolean getExistHonor() {
        if (this.honorNum == null || this.honorNum.equals(BizConstant.DB_NUM_ZERO)) {
            return false;
        }
        return true;
    }
}
