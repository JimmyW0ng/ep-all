package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatPayBillDetailPo;
import com.ep.domain.repository.domain.tables.records.EpWechatPayBillDetailRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

    /**
     * 根据班次id和时间区间获取可提现总金额
     *
     * @param classId
     * @param startTime
     * @param endTime
     * @return
     */
    public BigDecimal sumWithdrawFeeByClassId(Long classId, Timestamp startTime, Timestamp endTime) {
        if (null == startTime) {
            return dslContext.select(DSL.ifnull(DSL.sum(EP_WECHAT_PAY_BILL_DETAIL.TOTAL_FEE), 0))
                    .from(EP_WECHAT_PAY_BILL_DETAIL)
                    .where(EP_WECHAT_PAY_BILL_DETAIL.CLASS_ID.eq(classId))
                    .and(EP_WECHAT_PAY_BILL_DETAIL.CREATE_AT.lessThan(endTime))
                    .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq("SUCCESS"))
                    .and(EP_WECHAT_PAY_BILL_DETAIL.REFUND_STATUS.ne("SUCCESS"))
                    .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                    .fetchOneInto(BigDecimal.class);
        } else {
            return dslContext.select(DSL.ifnull(DSL.sum(EP_WECHAT_PAY_BILL_DETAIL.TOTAL_FEE.subtract(EP_WECHAT_PAY_BILL_DETAIL.POUNDAGE)), 0))
                    .from(EP_WECHAT_PAY_BILL_DETAIL)
                    .where(EP_WECHAT_PAY_BILL_DETAIL.CLASS_ID.eq(classId))
                    .and(EP_WECHAT_PAY_BILL_DETAIL.CREATE_AT.greaterOrEqual(startTime))
                    .and(EP_WECHAT_PAY_BILL_DETAIL.CREATE_AT.lessThan(endTime))
                    .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq("SUCCESS"))
                    .and(EP_WECHAT_PAY_BILL_DETAIL.REFUND_STATUS.ne("SUCCESS"))
                    .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                    .fetchOneInto(BigDecimal.class);
        }
    }

    /**
     * 根据班次id和时间区间获取课提现总金额
     *
     * @param classId
     * @param startTime
     * @param endTime
     * @return
     */
    public BigDecimal sumWechatPayFeeByClassId(Long classId, Timestamp startTime, Timestamp endTime) {
        return dslContext.select(DSL.ifnull(DSL.sum(EP_WECHAT_PAY_BILL_DETAIL.POUNDAGE), 0))
                .from(EP_WECHAT_PAY_BILL_DETAIL)
                .where(EP_WECHAT_PAY_BILL_DETAIL.CLASS_ID.eq(classId))
                .and(EP_WECHAT_PAY_BILL_DETAIL.CREATE_AT.greaterThan(startTime))
                .and(EP_WECHAT_PAY_BILL_DETAIL.CREATE_AT.lessOrEqual(endTime))
                .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq("SUCCESS"))
                .and(EP_WECHAT_PAY_BILL_DETAIL.REFUND_STATUS.ne("SUCCESS"))
                .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                .fetchOneInto(BigDecimal.class);
    }
}
