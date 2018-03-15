package com.ep.domain.pojo.bo;

import com.ep.common.tool.DateTools;
import com.ep.domain.pojo.po.EpMemberMessagePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberMessageBo extends EpMemberMessagePo {

    private String avatar;
    private Long orderId;

    public String getCommentTimeFormat() {
        return DateTools.dateToString(super.getCreateAt(), DateTools.DATE_FMT_4);
    }
}
