package com.ep.domain.service;

import com.ep.domain.pojo.bo.WechatUnifiedOrderBo;
import com.ep.domain.pojo.bo.WechatUnifiedOrderPayRefundBo;
import com.ep.domain.pojo.po.EpWechatUnifiedOrderPo;
import com.ep.domain.repository.WechatUnifiedOrderRepository;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 微信支付统一订单服务
 * @Author: CC.F
 * @Date: 10:57 2018/5/10/010
 */
@Service
public class WechatUnifiedOrderService {
    @Autowired
    private WechatUnifiedOrderRepository wechatUnifiedOrderRepository;

    public Page<WechatUnifiedOrderBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition
            , Timestamp timeEndStart, Timestamp timeEndEnd) {
        return wechatUnifiedOrderRepository.findbyPageAndCondition(pageable, condition, timeEndStart, timeEndEnd);
    }

    /**
     * 根据订单id获取微信统一下单记录
     *
     * @param orderId
     * @return
     */
    public List<EpWechatUnifiedOrderPo> findByOrderId(Long orderId) {
        return wechatUnifiedOrderRepository.findByOrderId(orderId);
    }

    /**
     * 根据orderId统一下单成功后退款bo
     *
     * @param orderId
     * @return
     */
    public List<WechatUnifiedOrderPayRefundBo> findUnifiedOrderPayRefundBoByOrderId(Long orderId) {
        return wechatUnifiedOrderRepository.findUnifiedOrderPayRefundBoByOrderId(orderId);
    }

    public EpWechatUnifiedOrderPo getByOutTradeNo(String outTradeNo) {
        return wechatUnifiedOrderRepository.getByOutTradeNo(outTradeNo);
    }
}
