package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatPayRefundPo;
import com.ep.domain.repository.domain.tables.records.EpWechatPayRefundRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_WECHAT_PAY_REFUND;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:22 2018/5/15
 */
@Repository
public class WechatPayRefundRepository extends AbstractCRUDRepository<EpWechatPayRefundRecord, Long, EpWechatPayRefundPo> {

    @Autowired
    public WechatPayRefundRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_PAY_REFUND, EP_WECHAT_PAY_REFUND.ID, EpWechatPayRefundPo.class);
    }

//    public List<EpWechatPayRefundPo> findByOrderId(Long orderId){
//        return dslContext.selectFrom(EP_WECHAT_PAY_REFUND)
//                .where(EP_WECHAT_PAY_REFUND.ORDER_)
//    }
}
