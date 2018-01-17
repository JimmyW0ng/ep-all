package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrganPo;
import lombok.Data;

@Data
public class OrganBo extends EpOrganPo {

    private String catalogIds;
    private String catalogLabels;
    private String fileUrl;

}
