package com.ep.domain.pojo.bo;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.po.EpMemberChildTagPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganClassCatalogCommentBo extends AbstractBasePojo {

    private Long childId;
    private String childNickName;
    private String avatar;
    private String comment;
    private List<EpMemberChildTagPo> tags;

    public Boolean getWaitComment() {
        return CollectionsTools.isEmpty(tags) && StringTools.isBlank(comment);
    }

}
