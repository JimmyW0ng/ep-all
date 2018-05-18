package com.ep.domain.repository;

import com.ep.common.tool.wechat.WechatTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpWechatPayBillPo;
import com.ep.domain.repository.domain.tables.records.EpWechatPayBillRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

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

    /**
     * 获取最近的微信支付对账表
     *
     * @return
     */
    public EpWechatPayBillPo getLastPayBill() {
        return dslContext.selectFrom(EP_WECHAT_PAY_BILL)
                .where(EP_WECHAT_PAY_BILL.RETURN_CODE.eq("SUCCESS"))
                .and(EP_WECHAT_PAY_BILL.DEL_FLAG.eq(false))
                .orderBy(EP_WECHAT_PAY_BILL.ID.desc())
                .limit(BizConstant.DB_NUM_ONE)
                .fetchOneInto(EpWechatPayBillPo.class);
    }

    public Optional<EpWechatPayBillPo> getByBillDate(Integer billDate) {
        EpWechatPayBillPo billPo = dslContext.selectFrom(EP_WECHAT_PAY_BILL)
                                             .where(EP_WECHAT_PAY_BILL.BILL_DATE.eq(billDate))
                                             .and(EP_WECHAT_PAY_BILL.DEL_FLAG.eq(false))
                                             .fetchOneInto(EpWechatPayBillPo.class);
        return Optional.ofNullable(billPo);
    }

    /**
     * 处理失败更新
     *
     * @param id
     * @param returnCode
     * @param returnMsg
     * @return
     */
    public int handleFail(Long id, String returnCode, String returnMsg) {
        return dslContext.update(EP_WECHAT_PAY_BILL)
                         .set(EP_WECHAT_PAY_BILL.RETURN_CODE, returnCode)
                         .set(EP_WECHAT_PAY_BILL.RETURN_MSG, returnMsg)
                         .where(EP_WECHAT_PAY_BILL.ID.eq(id))
                         .execute();
    }

    /**
     * 处理成功更新
     *
     * @param id
     * @param totalNum
     * @param totalAmount
     * @param totalRefundAmount
     * @param totalCouponRefundAmount
     * @param totalPoundage
     */
    public int handleSuccess(Long id, int totalNum, BigDecimal totalAmount, BigDecimal totalRefundAmount, BigDecimal totalCouponRefundAmount, BigDecimal totalPoundage) {
        return dslContext.update(EP_WECHAT_PAY_BILL)
                         .set(EP_WECHAT_PAY_BILL.TOTAL_NUM, totalNum)
                         .set(EP_WECHAT_PAY_BILL.TOTAL_AMOUNT, totalAmount)
                         .set(EP_WECHAT_PAY_BILL.TOTAL_REFUND_AMOUNT, totalRefundAmount)
                         .set(EP_WECHAT_PAY_BILL.TOTAL_COUPON_REFUND_AMOUNT, totalCouponRefundAmount)
                         .set(EP_WECHAT_PAY_BILL.TOTAL_POUNDAGE, totalPoundage)
                         .set(EP_WECHAT_PAY_BILL.RETURN_CODE, WechatTools.SUCCESS)
                         .where(EP_WECHAT_PAY_BILL.ID.eq(id))
                         .execute();
    }
}
