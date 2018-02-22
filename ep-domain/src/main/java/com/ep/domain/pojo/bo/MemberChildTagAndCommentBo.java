package com.ep.domain.pojo.bo;


import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.po.EpMemberChildTagPo;
import lombok.Data;

import java.util.List;

@Data
public class MemberChildTagAndCommentBo extends AbstractBasePojo {

    private Long childId;
    private String avatar;
    private String comment;
    private List<EpMemberChildTagPo> tags;

}
