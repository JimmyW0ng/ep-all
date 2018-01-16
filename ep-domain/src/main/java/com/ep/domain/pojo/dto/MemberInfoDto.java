package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.bo.MemberChildBo;
import com.ep.domain.pojo.po.EpMemberPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author J.W on 2017/11/9 0009.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto extends AbstractBasePojo {

    private EpMemberPo mbrInfo;
    private List<MemberChildBo> children;

}
