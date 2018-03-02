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
    private String ognName;
    private String avatar;
    private String childTrueName;
    private String courseName;
    private String className;
    private String classCatalogTitle;
    private Long orderId;

    public String getCommentTimeFormat() {
        return DateTools.formatDatetoString(super.getCreateAt(), DateTools.DATE_FMT_4);
    }

}
