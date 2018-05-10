package com.ep.domain.pojo.dto;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 订单报名成功返回
 * @Author J.W
 * @Date: 下午 4:24 2018/5/9 0009
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends AbstractBasePojo {

    private Long orderId;
    private Boolean waitPayFlag;

}
