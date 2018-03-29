package com.ep.domain.pojo.bo;

import com.ep.domain.pojo.AbstractBasePojo;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 14:33 2018/3/29/029
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTypeBo extends AbstractBasePojo {
    private Long id;
    private Long classId;
    private Long childId;
    private EpOrganClassType type;
    private EpOrderStatus status;
}
