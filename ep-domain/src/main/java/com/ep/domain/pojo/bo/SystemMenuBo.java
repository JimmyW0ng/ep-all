package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpSystemMenuPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:51 2018/1/23
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SystemMenuBo extends EpSystemMenuPo {
    private String parentName;
    private List<SystemMenuBo> childList;
    private Long roleAuthId;
}
