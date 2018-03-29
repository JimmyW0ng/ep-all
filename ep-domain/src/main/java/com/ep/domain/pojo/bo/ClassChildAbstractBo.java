package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpMemberChildPo;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
public class ClassChildAbstractBo extends EpMemberChildPo {

    private String avatar;
    private String childNickName;
    private String sign;
    private List<MemberChildTagBo> tags;
    private List<MemberChildCommentBo> comments;
}
