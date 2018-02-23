package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpMemberChildCommentPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberChildCommentBo extends EpMemberChildCommentPo {

    private String nickName;
    private String organName;

}
