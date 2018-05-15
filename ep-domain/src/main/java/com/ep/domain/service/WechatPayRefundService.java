package com.ep.domain.service;

import com.ep.domain.repository.WechatPayRefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:21 2018/5/15
 */
@Service
public class WechatPayRefundService {
    @Autowired
    private WechatPayRefundRepository wechatPayRefundRepository;

//    public Page<WechatUnifiedOrderBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition
//            , Timestamp timeEndStart, Timestamp timeEndEnd) {
//        return wechatUnifiedOrderRepository.findbyPageAndCondition(pageable, condition, timeEndStart, timeEndEnd);
//    }
}
