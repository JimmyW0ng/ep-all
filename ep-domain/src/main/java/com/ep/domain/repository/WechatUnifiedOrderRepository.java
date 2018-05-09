package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatUnifiedOrderPo;
import com.ep.domain.repository.domain.tables.records.EpWechatUnifiedOrderRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_WECHAT_UNIFIED_ORDER;

/**
 * @Description: 微信通知下单Repository
 * @Author: J.W
 * @Date: 17:28 2018/5/3/003
 */
@Repository
public class WechatUnifiedOrderRepository extends AbstractCRUDRepository<EpWechatUnifiedOrderRecord, Long, EpWechatUnifiedOrderPo> {

    @Autowired
    public WechatUnifiedOrderRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_UNIFIED_ORDER, EP_WECHAT_UNIFIED_ORDER.ID, EpWechatUnifiedOrderPo.class);
    }

    /**
     * 保存统一下单返回信息
     *
     * @return
     */
    public int handleUnifiedOrder(Long id,
                                  String returnCode,
                                  String returnMsg,
                                  String resultCode,
                                  String errCode,
                                  String errCodeDes,
                                  String prepayId) {
        return dslContext.update(EP_WECHAT_UNIFIED_ORDER)
                         .set(EP_WECHAT_UNIFIED_ORDER.RETURN_CODE, returnCode)
                         .set(EP_WECHAT_UNIFIED_ORDER.RETURN_MSG, returnMsg)
                         .set(EP_WECHAT_UNIFIED_ORDER.RESULT_CODE, resultCode)
                         .set(EP_WECHAT_UNIFIED_ORDER.ERR_CODE, errCode)
                         .set(EP_WECHAT_UNIFIED_ORDER.ERR_CODE_DES, errCodeDes)
                         .set(EP_WECHAT_UNIFIED_ORDER.PREPAY_ID, prepayId)
                         .where(EP_WECHAT_UNIFIED_ORDER.ID.eq(id))
                         .and(EP_WECHAT_UNIFIED_ORDER.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 根据商户订单号获取支付数据
     *
     * @param outTradeNo
     * @return
     */
    public EpWechatUnifiedOrderPo getByOutTradeNo(String outTradeNo) {
        return dslContext.selectFrom(EP_WECHAT_UNIFIED_ORDER)
                         .where(EP_WECHAT_UNIFIED_ORDER.OUT_TRADE_NO.eq(outTradeNo))
                         .and(EP_WECHAT_UNIFIED_ORDER.DEL_FLAG.eq(false))
                         .fetchOneInto(EpWechatUnifiedOrderPo.class);
    }

    /**
     * 微信支付通知结果处理
     *
     * @param outTradeNo
     * @param notifyReturnCode
     * @param notifyReturnMsg
     * @param notifyResultCode
     * @param notifyErrCode
     * @param notifyErrCodeDes
     * @param isSubscribe
     * @param openid
     * @param bankType
     * @param transactionId
     * @param timeEnd
     */
    public int handleNotify(String outTradeNo,
                            String notifyReturnCode,
                            String notifyReturnMsg,
                            String notifyResultCode,
                            String notifyErrCode,
                            String notifyErrCodeDes,
                            String isSubscribe,
                            String openid,
                            String bankType,
                            String transactionId,
                            String timeEnd) {
        return dslContext.update(EP_WECHAT_UNIFIED_ORDER)
                         .set(EP_WECHAT_UNIFIED_ORDER.NOTIFY_RETURN_CODE, notifyReturnCode)
                         .set(EP_WECHAT_UNIFIED_ORDER.NOTIFY_RETURN_MSG, notifyReturnMsg)
                         .set(EP_WECHAT_UNIFIED_ORDER.NOTIFY_RESULT_CODE, notifyResultCode)
                         .set(EP_WECHAT_UNIFIED_ORDER.NOTIFY_ERR_CODE, notifyErrCode)
                         .set(EP_WECHAT_UNIFIED_ORDER.NOTIFY_ERR_CODE_DES, notifyErrCodeDes)
                         .set(EP_WECHAT_UNIFIED_ORDER.IS_SUBSCRIBE, isSubscribe)
                         .set(EP_WECHAT_UNIFIED_ORDER.OPENID, openid)
                         .set(EP_WECHAT_UNIFIED_ORDER.BANK_TYPE, bankType)
                         .set(EP_WECHAT_UNIFIED_ORDER.TRANSACTION_ID, transactionId)
                         .set(EP_WECHAT_UNIFIED_ORDER.TIME_END, timeEnd)
                         .where(EP_WECHAT_UNIFIED_ORDER.OUT_TRADE_NO.eq(outTradeNo))
                         .and(EP_WECHAT_UNIFIED_ORDER.NOTIFY_RESULT_CODE.isNull())
                         .execute();
    }
}
