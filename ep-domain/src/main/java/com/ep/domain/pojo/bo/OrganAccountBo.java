package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganAccountPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrganAccountBo extends EpOrganAccountPo {

    private String avatar;
    private String ognName;
    private String avatarUrlPreCode;

}
