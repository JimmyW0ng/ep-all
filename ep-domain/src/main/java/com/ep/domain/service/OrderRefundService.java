package com.ep.domain.service;

import com.ep.common.exception.ServiceRuntimeException;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrderRefundBo;
import com.ep.domain.pojo.bo.OrderRefundPayRefundBo;
import com.ep.domain.pojo.po.EpOrderRefundPo;
import com.ep.domain.repository.OrderRefundRepository;
import com.ep.domain.repository.OrderRepository;
import com.ep.domain.repository.domain.enums.EpOrderRefundStatus;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
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
    @Autowired
    private OrderRepository orderRepository;

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
    @Transactional(rollbackFor = Exception.class)
    public ResultDo saveOrderRefund(Long ognId, Long orderId, String outTradeNo, String refundReason, Long userId) {
        log.info("[微信退款]申请微信退款开始，ognId={},orderId={},outTradeNo={},refundReason={},userId={}。", ognId, orderId, outTradeNo, refundReason, userId);
        if (orderRepository.refundApplyOrder(orderId) == BizConstant.DB_NUM_ONE) {
            log.info("[微信退款]申请微信退款，ep_order表更新状态为refund_apply,orderId={}。", orderId);
        } else {
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        EpOrderRefundPo orderRefundPo = new EpOrderRefundPo();
        orderRefundPo.setOgnId(ognId);
        orderRefundPo.setOrderId(orderId);
        orderRefundPo.setOutTradeNo(outTradeNo);
        orderRefundPo.setRefundReason(refundReason);
        orderRefundPo.setApplyId(userId);
        orderRefundPo.setStatus(EpOrderRefundStatus.save);
        orderRefundRepository.insert(orderRefundPo);
        log.info("[微信退款]申请微信退款，ep_order_refund表插入数据orderRefundPo={}。", orderRefundPo);
        log.info("[微信退款]申请微信退款成功，orderId={}。", orderId);
        return ResultDo.build();
    }

    public List<OrderRefundPayRefundBo> findOrderRefundPayRefundBoByOrderId(Long orderId) {
        return orderRefundRepository.findOrderRefundPayRefundBoByOrderId(orderId);
    }

    /**
     * 拒绝退款
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo refuseOrderRefund(Long orderId, Long operateId) throws Exception {
        log.info("[微信退款]拒绝微信退款开始，orderId={}。", orderId);
        if (orderRepository.refundRefuseOrder(orderId) == BizConstant.DB_NUM_ONE) {
            log.info("[微信退款]拒绝微信退款，ep_order表更新状态为refund_apply,orderId={}。", orderId);
        } else {
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
        int count = orderRefundRepository.refuseOrderRefund(orderId, operateId);
        if (count == BizConstant.DB_NUM_ONE) {
            log.info("[微信退款]拒绝微信退款，ep_order_refund表更新申请状态为refuse,orderId={},operateId={}。", orderId, operateId);
        } else if (count == BizConstant.DB_NUM_ZERO) {
            log.error("[微信退款]拒绝微信退款失败，不存在orderId={},status=save的记录。", orderId);
            throw new ServiceRuntimeException("拒绝微信退款失败，不存在orderId=" + orderId + ",status=save的记录");
        } else {
            //存在多条orderId相同,status=save 抛出异常
            log.error("[微信退款]拒绝微信退款失败，存在多条orderId={},status=save的记录。", orderId);
            throw new ServiceRuntimeException("拒绝微信退款失败，存在多条orderId=" + orderId + ",status=save的记录");

        }
        log.info("[微信退款]拒绝微信退款，ep_order_refund表更新申请状态为refuse,orderId={}。", orderId);
        return ResultDo.build();
    }
}
