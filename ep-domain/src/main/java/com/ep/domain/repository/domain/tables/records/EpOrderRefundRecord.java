/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpOrderRefundStatus;
import com.ep.domain.repository.domain.tables.EpOrderRefund;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 订单退款申请表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrderRefundRecord extends UpdatableRecordImpl<EpOrderRefundRecord> implements Record13<Long, Long, Long, String, String, EpOrderRefundStatus, Long, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 27403863;

    /**
     * Setter for <code>ep.ep_order_refund.id</code>. 主键
     */
    public EpOrderRefundRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_order_refund.ogn_id</code>. 机构id
     */
    public EpOrderRefundRecord setOgnId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.ogn_id</code>. 机构id
     */
    public Long getOgnId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_order_refund.order_id</code>. 订单id
     */
    public EpOrderRefundRecord setOrderId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.order_id</code>. 订单id
     */
    public Long getOrderId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_order_refund.out_trade_no</code>. 商户订单号
     */
    public EpOrderRefundRecord setOutTradeNo(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.out_trade_no</code>. 商户订单号
     */
    public String getOutTradeNo() {
        return (String) get(3);
    }

    /**
     * Setter for <code>ep.ep_order_refund.refund_reason</code>. 退单原因
     */
    public EpOrderRefundRecord setRefundReason(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.refund_reason</code>. 退单原因
     */
    public String getRefundReason() {
        return (String) get(4);
    }

    /**
     * Setter for <code>ep.ep_order_refund.status</code>. 状态：已保存；成功；失败；拒绝；撤销；
     */
    public EpOrderRefundRecord setStatus(EpOrderRefundStatus value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.status</code>. 状态：已保存；成功；失败；拒绝；撤销；
     */
    public EpOrderRefundStatus getStatus() {
        return (EpOrderRefundStatus) get(5);
    }

    /**
     * Setter for <code>ep.ep_order_refund.apply_id</code>. 申请人员
     */
    public EpOrderRefundRecord setApplyId(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.apply_id</code>. 申请人员
     */
    public Long getApplyId() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>ep.ep_order_refund.operate_id</code>. 操作人员
     */
    public EpOrderRefundRecord setOperateId(Long value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.operate_id</code>. 操作人员
     */
    public Long getOperateId() {
        return (Long) get(7);
    }

    /**
     * Setter for <code>ep.ep_order_refund.create_at</code>. 创建时间
     */
    public EpOrderRefundRecord setCreateAt(Timestamp value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(8);
    }

    /**
     * Setter for <code>ep.ep_order_refund.update_at</code>. 更新时间
     */
    public EpOrderRefundRecord setUpdateAt(Timestamp value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(9);
    }

    /**
     * Setter for <code>ep.ep_order_refund.remark</code>. 备注
     */
    public EpOrderRefundRecord setRemark(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(10);
    }

    /**
     * Setter for <code>ep.ep_order_refund.del_flag</code>. 删除标记
     */
    public EpOrderRefundRecord setDelFlag(Boolean value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(11);
    }

    /**
     * Setter for <code>ep.ep_order_refund.version</code>.
     */
    public EpOrderRefundRecord setVersion(Long value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order_refund.version</code>.
     */
    public Long getVersion() {
        return (Long) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Long, Long, Long, String, String, EpOrderRefundStatus, Long, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Long, Long, Long, String, String, EpOrderRefundStatus, Long, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row13) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrderRefund.EP_ORDER_REFUND.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrderRefund.EP_ORDER_REFUND.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrderRefund.EP_ORDER_REFUND.ORDER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return EpOrderRefund.EP_ORDER_REFUND.OUT_TRADE_NO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return EpOrderRefund.EP_ORDER_REFUND.REFUND_REASON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpOrderRefundStatus> field6() {
        return EpOrderRefund.EP_ORDER_REFUND.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field7() {
        return EpOrderRefund.EP_ORDER_REFUND.APPLY_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field8() {
        return EpOrderRefund.EP_ORDER_REFUND.OPERATE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return EpOrderRefund.EP_ORDER_REFUND.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field10() {
        return EpOrderRefund.EP_ORDER_REFUND.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return EpOrderRefund.EP_ORDER_REFUND.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field12() {
        return EpOrderRefund.EP_ORDER_REFUND.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field13() {
        return EpOrderRefund.EP_ORDER_REFUND.VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getOgnId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getOrderId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getOutTradeNo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getRefundReason();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundStatus value6() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value7() {
        return getApplyId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value8() {
        return getOperateId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value9() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value10() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value12() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value13() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value2(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value3(Long value) {
        setOrderId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value4(String value) {
        setOutTradeNo(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value5(String value) {
        setRefundReason(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value6(EpOrderRefundStatus value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value7(Long value) {
        setApplyId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value8(Long value) {
        setOperateId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value9(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value10(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value11(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value12(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord value13(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRefundRecord values(Long value1, Long value2, Long value3, String value4, String value5, EpOrderRefundStatus value6, Long value7, Long value8, Timestamp value9, Timestamp value10, String value11, Boolean value12, Long value13) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EpOrderRefundRecord
     */
    public EpOrderRefundRecord() {
        super(EpOrderRefund.EP_ORDER_REFUND);
    }

    /**
     * Create a detached, initialised EpOrderRefundRecord
     */
    public EpOrderRefundRecord(Long id, Long ognId, Long orderId, String outTradeNo, String refundReason, EpOrderRefundStatus status, Long applyId, Long operateId, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrderRefund.EP_ORDER_REFUND);

        set(0, id);
        set(1, ognId);
        set(2, orderId);
        set(3, outTradeNo);
        set(4, refundReason);
        set(5, status);
        set(6, applyId);
        set(7, operateId);
        set(8, createAt);
        set(9, updateAt);
        set(10, remark);
        set(11, delFlag);
        set(12, version);
    }
}
