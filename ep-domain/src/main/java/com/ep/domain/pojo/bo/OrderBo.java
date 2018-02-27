package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.po.EpOrderPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBo extends EpOrderPo {
    private Long mobile;
    private String childTrueName;
    private String courseName;
    private String className;
}
