package com.ep.domain.service;

import com.ep.domain.pojo.bo.OrderRefundBo;
import com.ep.domain.repository.OrderRefundRepository;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 12:46 2018/5/22/022
 */
@Service
@Slf4j
public class OrderRefundService {
    @Autowired
    private OrderRefundRepository orderRefundRepository;

    public Page<OrderRefundBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return orderRefundRepository.findbyPageAndCondition(pageable, condition);
    }
}
