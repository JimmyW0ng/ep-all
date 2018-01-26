package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 13:59 2018/1/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserBo extends EpSystemUserPo {
    private List<EpSystemRolePo> systemRolePos;
}
