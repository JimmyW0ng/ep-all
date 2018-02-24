package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
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
    private String avatar;

    public String getCommentTimeFormat() {
        return DateTools.formatDatetoString(super.getCreateAt(), DateTools.DATE_FMT_4);
    }

}
