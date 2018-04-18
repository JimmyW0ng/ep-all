package com.ep.domain.pojo.event;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.pojo.po.EpOrderPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassOpenEventBo extends AbstractBasePojo {

    private Long classId;
    private List<EpOrderPo> openingOrders;

}
