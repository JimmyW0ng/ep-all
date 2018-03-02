package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.po.EpOrganPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganInfoDto extends AbstractBasePojo {

    private EpOrganPo ognInfo;
    private String logoUrl;
    private String mainPicUrl;
    private Long totalCommentNum;

}
