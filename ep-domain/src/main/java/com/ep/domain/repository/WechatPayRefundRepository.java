package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatPayRefundPo;
import com.ep.domain.repository.domain.tables.records.EpWechatPayRefundRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_WECHAT_PAY_REFUND;

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
     * @param outRefundNo
     * @return
     */
    public EpWechatPayRefundPo getByOutRefundNo(String outRefundNo) {
        return dslContext.selectFrom(EP_WECHAT_PAY_REFUND)
                         .where(EP_WECHAT_PAY_REFUND.OUT_REFUND_NO.eq(outRefundNo))
                         .and(EP_WECHAT_PAY_REFUND.DEL_FLAG.eq(false))
                         .fetchOneInto(EpWechatPayRefundPo.class);
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
        return dslContext.update(EP_WECHAT_PAY_REFUND)
                         .set(EP_WECHAT_PAY_REFUND.RETURN_CODE, returnCode)
                         .set(EP_WECHAT_PAY_REFUND.RETURN_MSG, returnMsg)
                         .set(EP_WECHAT_PAY_REFUND.RESULT_CODE, resultCode)
                         .set(EP_WECHAT_PAY_REFUND.ERR_CODE, errCode)
                         .set(EP_WECHAT_PAY_REFUND.ERR_CODE_DES, errCodeDes)
                         .set(EP_WECHAT_PAY_REFUND.REFUND_ID, refundId)
                         .where(EP_WECHAT_PAY_REFUND.ID.eq(id))
                         .and(EP_WECHAT_PAY_REFUND.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * notify处理
     *
     * @param outRefundNo
     * @param refundId
     * @param refundAccount
     * @param refundRecvAccout
     * @param refundStatus
     * @param settlementRefundFee
     * @param settlementTotalFee
     * @param successTime
     * @return
     */
    public int handleNotify(String outRefundNo,
                            String refundId,
                            String refundAccount,
                            String refundRecvAccout,
                            String refundStatus,
                            Integer settlementRefundFee,
                            Integer settlementTotalFee,
                            String successTime) {
        return dslContext.update(EP_WECHAT_PAY_REFUND)
                         .set(EP_WECHAT_PAY_REFUND.REFUND_ID, refundId)
                         .set(EP_WECHAT_PAY_REFUND.REFUND_ACCOUNT, refundAccount)
                         .set(EP_WECHAT_PAY_REFUND.REFUND_RECV_ACCOUT, refundRecvAccout)
                         .set(EP_WECHAT_PAY_REFUND.REFUND_STATUS, refundStatus)
                         .set(EP_WECHAT_PAY_REFUND.SETTLEMENT_REFUND_FEE, settlementRefundFee)
                         .set(EP_WECHAT_PAY_REFUND.SETTLEMENT_TOTAL_FEE, settlementTotalFee)
                         .set(EP_WECHAT_PAY_REFUND.SUCCESS_TIME, successTime)
                         .set(EP_WECHAT_PAY_REFUND.REFUND_ID, refundId)
                         .where(EP_WECHAT_PAY_REFUND.OUT_REFUND_NO.eq(outRefundNo))
                         .and(EP_WECHAT_PAY_REFUND.REFUND_STATUS.isNull())
                         .and(EP_WECHAT_PAY_REFUND.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 根据商户订单号outTradeNo获取记录
     *
     * @param outTradeNo
     * @return
     */
    public List<EpWechatPayRefundPo> findByOutTradeNo(String outTradeNo) {
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_WECHAT_PAY_REFUND.ID);
        fieldList.add(EP_WECHAT_PAY_REFUND.OUT_TRADE_NO);
        fieldList.add(EP_WECHAT_PAY_REFUND.TRANSACTION_ID);
        fieldList.add(EP_WECHAT_PAY_REFUND.OUT_REFUND_NO);
        fieldList.add(EP_WECHAT_PAY_REFUND.TOTAL_FEE);
        fieldList.add(EP_WECHAT_PAY_REFUND.REFUND_FEE);
        fieldList.add(EP_WECHAT_PAY_REFUND.SUCCESS_TIME);
        fieldList.add(EP_WECHAT_PAY_REFUND.REFUND_ACCOUNT);
        fieldList.add(EP_WECHAT_PAY_REFUND.NOTIFY_RETURN_CODE);
        return dslContext.select(fieldList)
                .from(EP_WECHAT_PAY_REFUND)
                .where(EP_WECHAT_PAY_REFUND.OUT_TRADE_NO.eq(outTradeNo))
                .and(EP_WECHAT_PAY_REFUND.DEL_FLAG.eq(false))
                .fetchInto(EpWechatPayRefundPo.class);
    }

    /**
     * 根据商户订单号outTradeNo获取业务结果为SUCCESS的记录
     *
     * @param outTradeNo
     * @return
     */
    public List<EpWechatPayRefundPo> findSuccessPoByOutTradeNo(String outTradeNo) {
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_WECHAT_PAY_REFUND.ID);
        fieldList.add(EP_WECHAT_PAY_REFUND.OUT_TRADE_NO);
        fieldList.add(EP_WECHAT_PAY_REFUND.TRANSACTION_ID);
        fieldList.add(EP_WECHAT_PAY_REFUND.OUT_REFUND_NO);
        fieldList.add(EP_WECHAT_PAY_REFUND.TOTAL_FEE);
        fieldList.add(EP_WECHAT_PAY_REFUND.REFUND_FEE);
        fieldList.add(EP_WECHAT_PAY_REFUND.SUCCESS_TIME);
        fieldList.add(EP_WECHAT_PAY_REFUND.REFUND_ACCOUNT);
        fieldList.add(EP_WECHAT_PAY_REFUND.NOTIFY_RETURN_CODE);
        return dslContext.select()
                .from(EP_WECHAT_PAY_REFUND).
                        where(EP_WECHAT_PAY_REFUND.OUT_TRADE_NO.eq(outTradeNo))
                .and(EP_WECHAT_PAY_REFUND.NOTIFY_RETURN_CODE.eq("SUCCESS"))
                .and(EP_WECHAT_PAY_REFUND.DEL_FLAG.eq(false))
                .fetchInto(EpWechatPayRefundPo.class);
    }
}
