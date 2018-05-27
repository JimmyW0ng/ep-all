package com.ep.domain.pojo.event;

import com.ep.domain.pojo.AbstractBasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderBespeakEventBo extends AbstractBasePojo {

    private Long orderId;
}
