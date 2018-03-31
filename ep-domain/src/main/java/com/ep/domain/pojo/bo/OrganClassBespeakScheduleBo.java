package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganClassSchedulePo;
import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 8:32 2018/3/31
 */
@Data
@ToString(callSuper = true)
public class OrganClassBespeakScheduleBo extends EpOrganClassSchedulePo {
    private Boolean rectifyFlag;
}
