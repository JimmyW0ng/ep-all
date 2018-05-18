package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatPayBillPo;
import com.ep.domain.repository.domain.tables.records.EpWechatPayBillRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_WECHAT_PAY_BILL;

/**
 * @Description: 微信支付退单Repository
 * @Author: J.W
 * @Date: 17:28 2018/5/3/003
 */
@Repository
public class WechatPayBillRepository extends AbstractCRUDRepository<EpWechatPayBillRecord, Long, EpWechatPayBillPo> {

    @Autowired
    public WechatPayBillRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_PAY_BILL, EP_WECHAT_PAY_BILL.ID, EpWechatPayBillPo.class);
    }


}
