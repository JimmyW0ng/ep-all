package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatPayBillDetailPo;
import com.ep.domain.repository.domain.tables.records.EpWechatPayBillDetailRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_WECHAT_PAY_BILL_DETAIL;

/**
 * @Description: 微信支付退单Repository
 * @Author: J.W
 * @Date: 17:28 2018/5/3/003
 */
@Repository
public class WechatPayBillDetailRepository extends AbstractCRUDRepository<EpWechatPayBillDetailRecord, Long, EpWechatPayBillDetailPo> {

    @Autowired
    public WechatPayBillDetailRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_PAY_BILL_DETAIL, EP_WECHAT_PAY_BILL_DETAIL.ID, EpWechatPayBillDetailPo.class);
    }

    /**
     * 根据billid删除记录
     *
     * @param billId
     */
    public int deleteByBillId(Long billId) {
        return dslContext.delete(EP_WECHAT_PAY_BILL_DETAIL)
                         .where(EP_WECHAT_PAY_BILL_DETAIL.BILL_ID.eq(billId))
                         .execute();
    }
}
