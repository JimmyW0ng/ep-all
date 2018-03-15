package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganClassChildPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 15:02 2018/3/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganClassChildBo extends EpOrganClassChildPo {
    private String childTrueName;
    private String childNickName;
}
