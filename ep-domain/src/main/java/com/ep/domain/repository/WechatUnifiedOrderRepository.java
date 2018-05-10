package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.WechatUnifiedOrderBo;
import com.ep.domain.pojo.po.EpWechatUnifiedOrderPo;
import com.ep.domain.repository.domain.tables.records.EpWechatUnifiedOrderRecord;
import com.google.common.collect.Lists;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import static com.ep.domain.repository.domain.Tables.*;

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

    public Page<WechatUnifiedOrderBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition,
                                                             Timestamp timeEndStart, Timestamp timeEndEnd) {
        SelectConditionStep recordCount = dslContext.selectCount()
                .from(EP_WECHAT_UNIFIED_ORDER)
                .innerJoin(EP_ORDER).on(EP_ORDER.ID.eq(EP_WECHAT_UNIFIED_ORDER.ORDER_ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_ORDER.COURSE_ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS.ID.eq(EP_ORDER.CLASS_ID))
                .where(condition);
        if (null != timeEndStart) {
            recordCount.and("unix_timestamp(`ep`.`ep_wechat_unified_order`.`time_end`)>=unix_timestamp(" + "'" + timeEndStart.toString() + "'" + ")");
        }
        if (null != timeEndEnd) {
            recordCount.and("unix_timestamp(`ep`.`ep_wechat_unified_order`.`time_end`)<=unix_timestamp(" + "'" + timeEndEnd.toString() + "'" + ")");
        }
        long totalCount = (Long) recordCount.fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_WECHAT_UNIFIED_ORDER.fields());
        fieldList.add(EP_ORGAN_COURSE.ID.as("courseId"));
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME.as("courseName"));
        fieldList.add(EP_ORGAN_CLASS.ID.as("classId"));
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME.as("className"));

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_WECHAT_UNIFIED_ORDER)
                .innerJoin(EP_ORDER).on(EP_ORDER.ID.eq(EP_WECHAT_UNIFIED_ORDER.ORDER_ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_ORDER.COURSE_ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS.ID.eq(EP_ORDER.CLASS_ID))
                .where(condition);
        if (null != timeEndStart) {
            record.and("unix_timestamp(`ep`.`ep_wechat_unified_order`.`time_end`)>=unix_timestamp(" + "'" + timeEndStart.toString() + "'" + ")");
        }
        if (null != timeEndEnd) {
            record.and("unix_timestamp(`ep`.`ep_wechat_unified_order`.`time_end`)<=unix_timestamp(" + "'" + timeEndEnd.toString() + "'" + ")");
        }
        List<WechatUnifiedOrderBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(WechatUnifiedOrderBo.class);
        PageImpl<WechatUnifiedOrderBo> pPage = new PageImpl<WechatUnifiedOrderBo>(list, pageable, totalCount);
        return pPage;
    }
}
