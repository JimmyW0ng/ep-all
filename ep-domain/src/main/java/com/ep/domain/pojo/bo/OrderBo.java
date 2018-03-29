package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBo extends EpOrderPo {
    private Long mobile;
    private String childTrueName;
    private String childNickName;
    private String courseName;
    private String className;
    private EpOrganClassType classType;
    private EpOrganClassStatus classStatus;
}
