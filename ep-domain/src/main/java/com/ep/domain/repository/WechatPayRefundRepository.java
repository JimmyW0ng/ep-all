package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatPayRefundPo;
import com.ep.domain.repository.domain.tables.records.EpWechatPayRefundRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_WECHAT_PAY_REFUND;
import static com.ep.domain.repository.domain.Tables.EP_WECHAT_UNIFIED_ORDER;

/**
 * @Description: 微信支付退单Repository
 * @Author: J.W
 * @Date: 17:28 2018/5/3/003
 */
@Repository
public class WechatPayRefundRepository extends AbstractCRUDRepository<EpWechatPayRefundRecord, Long, EpWechatPayRefundPo> {

    @Autowired
    public WechatPayRefundRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_PAY_REFUND, EP_WECHAT_PAY_REFUND.ID, EpWechatPayRefundPo.class);
    }

    /**
     * 保存统一下单返回信息
     *
     * @return
     */
    public int handlePayRefund(Long id,
                               String returnCode,
                               String returnMsg,
                               String resultCode,
                               String errCode,
                               String errCodeDes,
                               String refundId) {
        return dslContext.update(EP_WECHAT_UNIFIED_ORDER)
                         .set(EP_WECHAT_PAY_REFUND.RETURN_CODE, returnCode)
                         .set(EP_WECHAT_PAY_REFUND.RETURN_MSG, returnMsg)
                         .set(EP_WECHAT_PAY_REFUND.RESULT_CODE, resultCode)
                         .set(EP_WECHAT_PAY_REFUND.ERR_CODE, errCode)
                         .set(EP_WECHAT_PAY_REFUND.ERR_CODE_DES, errCodeDes)
                         .set(EP_WECHAT_PAY_REFUND.REFUND_ID, refundId)
                         .where(EP_WECHAT_PAY_REFUND.ID.eq(id))
                         .and(EP_WECHAT_UNIFIED_ORDER.DEL_FLAG.eq(false))
                         .execute();
    }
}
