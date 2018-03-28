package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 15:28 2018/2/1
 */
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemOrganBo extends EpOrganPo {
    private String ognCreateDateStr;
    private String mainpicUrlPreCode;
    private String logoUrlPreCode;
    private Boolean supportTag;

}
