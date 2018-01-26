/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.tables.EpOrder;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record15;
import org.jooq.Row15;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 订单表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpOrderRecord extends UpdatableRecordImpl<EpOrderRecord> implements Record15<Long, Long, Long, Long, Long, Long, EpOrderStatus, Timestamp, Timestamp, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 140035760;

    /**
     * Setter for <code>ep.ep_order.id</code>. 主键
     */
    public EpOrderRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_order.member_id</code>. 会员id
     */
    public EpOrderRecord setMemberId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.member_id</code>. 会员id
     */
    public Long getMemberId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_order.child_id</code>. 孩子id
     */
    public EpOrderRecord setChildId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_order.ogn_id</code>. 机构id
     */
    public EpOrderRecord setOgnId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.ogn_id</code>. 机构id
     */
    public Long getOgnId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_order.course_id</code>. 课程id
     */
    public EpOrderRecord setCourseId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.course_id</code>. 课程id
     */
    public Long getCourseId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_order.sku_id</code>. sku.id
     */
    public EpOrderRecord setSkuId(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.sku_id</code>. sku.id
     */
    public Long getSkuId() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>ep.ep_order.status</code>. 订单状态:保存；成功；拒绝；取消；
     */
    public EpOrderRecord setStatus(EpOrderStatus value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.status</code>. 订单状态:保存；成功；拒绝；取消；
     */
    public EpOrderStatus getStatus() {
        return (EpOrderStatus) get(6);
    }

    /**
     * Setter for <code>ep.ep_order.auth_time</code>. 机构审核订单时间
     */
    public EpOrderRecord setAuthTime(Timestamp value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.auth_time</code>. 机构审核订单时间
     */
    public Timestamp getAuthTime() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>ep.ep_order.cancel_time</code>. 订单取消时间
     */
    public EpOrderRecord setCancelTime(Timestamp value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.cancel_time</code>. 订单取消时间
     */
    public Timestamp getCancelTime() {
        return (Timestamp) get(8);
    }

    /**
     * Setter for <code>ep.ep_order.child_version</code>. 孩子信息版本号
     */
    public EpOrderRecord setChildVersion(Long value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.child_version</code>. 孩子信息版本号
     */
    public Long getChildVersion() {
        return (Long) get(9);
    }

    /**
     * Setter for <code>ep.ep_order.create_at</code>. 创建时间
     */
    public EpOrderRecord setCreateAt(Timestamp value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(10);
    }

    /**
     * Setter for <code>ep.ep_order.update_at</code>. 更新时间
     */
    public EpOrderRecord setUpdateAt(Timestamp value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(11);
    }

    /**
     * Setter for <code>ep.ep_order.remark</code>. 备注
     */
    public EpOrderRecord setRemark(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(12);
    }

    /**
     * Setter for <code>ep.ep_order.del_flag</code>. 删除标记
     */
    public EpOrderRecord setDelFlag(Boolean value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(13);
    }

    /**
     * Setter for <code>ep.ep_order.version</code>.
     */
    public EpOrderRecord setVersion(Long value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_order.version</code>.
     */
    public Long getVersion() {
        return (Long) get(14);
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
    // Record15 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row15<Long, Long, Long, Long, Long, Long, EpOrderStatus, Timestamp, Timestamp, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row15) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row15<Long, Long, Long, Long, Long, Long, EpOrderStatus, Timestamp, Timestamp, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row15) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrder.EP_ORDER.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrder.EP_ORDER.MEMBER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrder.EP_ORDER.CHILD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return EpOrder.EP_ORDER.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpOrder.EP_ORDER.COURSE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return EpOrder.EP_ORDER.SKU_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpOrderStatus> field7() {
        return EpOrder.EP_ORDER.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return EpOrder.EP_ORDER.AUTH_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return EpOrder.EP_ORDER.CANCEL_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field10() {
        return EpOrder.EP_ORDER.CHILD_VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field11() {
        return EpOrder.EP_ORDER.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field12() {
        return EpOrder.EP_ORDER.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field13() {
        return EpOrder.EP_ORDER.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field14() {
        return EpOrder.EP_ORDER.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field15() {
        return EpOrder.EP_ORDER.VERSION;
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
        return getMemberId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getChildId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getOgnId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getCourseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getSkuId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderStatus value7() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value8() {
        return getAuthTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value9() {
        return getCancelTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value10() {
        return getChildVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value11() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value12() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value13() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value14() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value15() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value2(Long value) {
        setMemberId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value3(Long value) {
        setChildId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value4(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value5(Long value) {
        setCourseId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value6(Long value) {
        setSkuId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value7(EpOrderStatus value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value8(Timestamp value) {
        setAuthTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value9(Timestamp value) {
        setCancelTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value10(Long value) {
        setChildVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value11(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value12(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value13(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value14(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord value15(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrderRecord values(Long value1, Long value2, Long value3, Long value4, Long value5, Long value6, EpOrderStatus value7, Timestamp value8, Timestamp value9, Long value10, Timestamp value11, Timestamp value12, String value13, Boolean value14, Long value15) {
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
        value14(value14);
        value15(value15);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EpOrderRecord
     */
    public EpOrderRecord() {
        super(EpOrder.EP_ORDER);
    }

    /**
     * Create a detached, initialised EpOrderRecord
     */
    public EpOrderRecord(Long id, Long memberId, Long childId, Long ognId, Long courseId, Long skuId, EpOrderStatus status, Timestamp authTime, Timestamp cancelTime, Long childVersion, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrder.EP_ORDER);

        set(0, id);
        set(1, memberId);
        set(2, childId);
        set(3, ognId);
        set(4, courseId);
        set(5, skuId);
        set(6, status);
        set(7, authTime);
        set(8, cancelTime);
        set(9, childVersion);
        set(10, createAt);
        set(11, updateAt);
        set(12, remark);
        set(13, delFlag);
        set(14, version);
    }
}
