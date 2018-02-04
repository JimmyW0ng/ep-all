package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.po.EpFilePo;
import com.ep.domain.pojo.po.EpOrganPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganInfoDto extends AbstractBasePojo {

    private EpOrganPo ognInfo;
    private List<EpFilePo> ognBanners;
    private EpFilePo ognLogo;

}
