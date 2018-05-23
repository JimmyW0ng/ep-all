package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrderRefundBo;
import com.ep.domain.pojo.bo.OrderRefundPayRefundBo;
import com.ep.domain.pojo.po.EpOrderRefundPo;
import com.ep.domain.repository.domain.enums.EpOrderRefundStatus;
import com.ep.domain.repository.domain.tables.records.EpOrderRefundRecord;
import com.google.common.collect.Lists;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_ORDER_REFUND;
import static com.ep.domain.repository.domain.Tables.EP_ORGAN;
import static com.ep.domain.repository.domain.tables.EpWechatPayRefund.EP_WECHAT_PAY_REFUND;

/**
 * @Description: 订单退款申请表
 * @Author: CC.F
 * @Date: 12:49 2018/5/22/022
 */
@Repository
public class OrderRefundRepository extends AbstractCRUDRepository<EpOrderRefundRecord, Long, EpOrderRefundPo> {

    @Autowired
    public OrderRefundRepository(DSLContext dslContext) {
        super(dslContext, EP_ORDER_REFUND, EP_ORDER_REFUND.ID, EpOrderRefundPo.class);
    }

    public Optional<EpOrderRefundPo> findById(Long id) {
        EpOrderRefundPo data = dslContext.selectFrom(EP_ORDER_REFUND)
                .where(EP_ORDER_REFUND.ID.eq(id))
                .and(EP_ORDER_REFUND.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrderRefundPo.class);
        return Optional.ofNullable(data);
    }

    public Page<OrderRefundBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_ORDER_REFUND)
                .leftJoin(EP_ORGAN).on(EP_ORDER_REFUND.OGN_ID.eq(EP_ORGAN.ID))
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORDER_REFUND.fields());
        fieldList.add(EP_ORGAN.OGN_NAME);

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORDER_REFUND)
                .leftJoin(EP_ORGAN).on(EP_ORDER_REFUND.OGN_ID.eq(EP_ORGAN.ID))
                .where(condition);

        List<OrderRefundBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrderRefundBo.class);
        PageImpl<OrderRefundBo> pPage = new PageImpl<OrderRefundBo>(list, pageable, totalCount);
        return pPage;
    }

    public List<OrderRefundPayRefundBo> findOrderRefundPayRefundBoByOrderId(Long orderId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORDER_REFUND.fields());
        fieldList.add(EP_WECHAT_PAY_REFUND.TRANSACTION_ID);
        fieldList.add(EP_WECHAT_PAY_REFUND.OUT_REFUND_NO);
        fieldList.add(EP_WECHAT_PAY_REFUND.TOTAL_FEE);
        fieldList.add(EP_WECHAT_PAY_REFUND.REFUND_FEE);
        fieldList.add(EP_WECHAT_PAY_REFUND.RESULT_CODE);
        fieldList.add(EP_WECHAT_PAY_REFUND.REFUND_STATUS);
        fieldList.add(EP_WECHAT_PAY_REFUND.REFUND_RECV_ACCOUT);
        fieldList.add(EP_WECHAT_PAY_REFUND.SUCCESS_TIME);
        return dslContext.select(fieldList).from(EP_ORDER_REFUND)
                .leftJoin(EP_WECHAT_PAY_REFUND)
                .on(EP_ORDER_REFUND.OUT_TRADE_NO.eq(EP_WECHAT_PAY_REFUND.OUT_REFUND_NO)
                        .and(EP_WECHAT_PAY_REFUND.REFUND_STATUS.eq("SUCCESS"))
                        .and(EP_ORDER_REFUND.STATUS.eq(EpOrderRefundStatus.success)))
                .where(EP_ORDER_REFUND.ORDER_ID.eq(orderId))
                .and(EP_ORDER_REFUND.DEL_FLAG.eq(false))
                .fetchInto(OrderRefundPayRefundBo.class);
    }

    /**
     * 拒绝退款申请
     *
     * @param orderId
     * @return
     */
    public int refuseOrderRefund(Long orderId) {
        return dslContext.update(EP_ORDER_REFUND)
                .set(EP_ORDER_REFUND.STATUS, EpOrderRefundStatus.refuse)
                .where(EP_ORDER_REFUND.ORDER_ID.eq(orderId))
                .and(EP_ORDER_REFUND.STATUS.eq(EpOrderRefundStatus.save))
                .and(EP_ORDER_REFUND.DEL_FLAG.eq(false))
                .execute();


    }

    /**
     * 退款成功
     *
     * @param outTradeNo
     */
    public int successByOutTradeNo(String outTradeNo) {
        return dslContext.update(EP_ORDER_REFUND)
                         .set(EP_ORDER_REFUND.STATUS, EpOrderRefundStatus.success)
                         .where(EP_ORDER_REFUND.OUT_TRADE_NO.eq(outTradeNo))
                         .and(EP_ORDER_REFUND.STATUS.eq(EpOrderRefundStatus.save))
                         .and(EP_ORDER_REFUND.DEL_FLAG.eq(false))
                         .execute();
    }
}
