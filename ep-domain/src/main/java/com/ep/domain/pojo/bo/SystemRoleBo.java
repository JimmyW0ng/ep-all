package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 10:50 2018/1/25
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SystemRoleBo extends EpSystemRolePo {
    private List<EpSystemRoleAuthorityPo> systemRoleAuthorityPos;
}
