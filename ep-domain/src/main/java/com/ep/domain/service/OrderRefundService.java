package com.ep.domain.service;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrderRefundBo;
import com.ep.domain.pojo.po.EpOrderRefundPo;
import com.ep.domain.repository.OrderRefundRepository;
import com.ep.domain.repository.domain.enums.EpOrderRefundStatus;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

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

    public Optional<EpOrderRefundPo> findById(Long id) {
        return orderRefundRepository.findById(id);
    }

    /**
     * 申请退款
     *
     * @param ognId
     * @param orderId
     * @param outTradeNo
     * @param refundReason
     * @param userId
     * @return
     */
    public ResultDo saveOrderRefund(Long ognId, Long orderId, String outTradeNo, String refundReason, Long userId) {
        log.info("[微信退款]申请微信退款开始，ognId={},orderId={},outTradeNo={},refundReason={},userId={}。", ognId, orderId, outTradeNo, refundReason, userId);
        EpOrderRefundPo orderRefundPo = new EpOrderRefundPo();
        orderRefundPo.setOgnId(ognId);
        orderRefundPo.setOrderId(orderId);
        orderRefundPo.setOutTradeNo(outTradeNo);
        orderRefundPo.setRefundReason(refundReason);
        orderRefundPo.setApplyId(userId);
        orderRefundPo.setStatus(EpOrderRefundStatus.save);
        orderRefundRepository.insert(orderRefundPo);
        log.info("[微信退款]申请微信退款成功，ep_order_refund表插入数据orderRefundPo={}。", orderRefundPo);
        return ResultDo.build();
    }

}
