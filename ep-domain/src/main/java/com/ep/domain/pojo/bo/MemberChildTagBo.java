package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpMemberChildTagPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MemberChildTagBo extends EpMemberChildTagPo {

    private String tagName;
    private Integer num;

}
