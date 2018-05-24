package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatPayWithdrawDetailPo;
import com.ep.domain.repository.domain.tables.records.EpWechatPayWithdrawDetailRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_WECHAT_PAY_WITHDRAW_DETAIL;

/**
 * @Description: 微信支付退款Repository
 * @Author: CC.F
 * @Date: 22:29 2018/5/16
 */
@Repository
public class WechatPayWithdrawDetailRepository extends AbstractCRUDRepository<EpWechatPayWithdrawDetailRecord, Long, EpWechatPayWithdrawDetailPo> {

    @Autowired
    public WechatPayWithdrawDetailRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_PAY_WITHDRAW_DETAIL, EP_WECHAT_PAY_WITHDRAW_DETAIL.ID, EpWechatPayWithdrawDetailPo.class);
    }

    /**
     * 根据提现申请获取订单ids
     *
     * @param withdrawId
     * @return
     */
    public List<Long> findOrderIdsByWithdrawId(Long withdrawId) {
        return dslContext.select(EP_WECHAT_PAY_WITHDRAW_DETAIL.ORDER_ID)
                         .from(EP_WECHAT_PAY_WITHDRAW_DETAIL)
                         .where(EP_WECHAT_PAY_WITHDRAW_DETAIL.WITHDRAW_ID.eq(withdrawId))
                         .and(EP_WECHAT_PAY_WITHDRAW_DETAIL.DEL_FLAG.eq(false))
                         .fetchInto(Long.class);
    }
}
