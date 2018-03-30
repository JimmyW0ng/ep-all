package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganClassCatalogCommentBo extends AbstractBasePojo {

    private Long classScheduleId;
    private Long catalogTitle;
    private Long catalogDesc;
    private Boolean evaluateFlag;
    private Long childId;
    private String childNickName;
    private String avatar;
    private String comment;
    private List<MemberChildTagBo> tags;

}
