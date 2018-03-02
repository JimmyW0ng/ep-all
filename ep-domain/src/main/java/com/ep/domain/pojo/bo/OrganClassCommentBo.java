package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganClassCommentPo;
import lombok.Data;

@Data
public class OrganClassCommentBo extends EpOrganClassCommentPo {

    private String childNickName;
    private String className;
    private String childAvatar;
    private Integer honorNum;
}
