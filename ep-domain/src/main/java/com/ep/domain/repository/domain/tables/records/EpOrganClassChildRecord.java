/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpOrganClassChild;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 机构班级孩子表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganClassChildRecord extends UpdatableRecordImpl<EpOrganClassChildRecord> implements Record9<Long, Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 368967763;

    /**
     * Setter for <code>ep.ep_organ_class_child.id</code>. 主键
     */
    public EpOrganClassChildRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpOrganClassChildRecord
     */
    public EpOrganClassChildRecord() {
        super(EpOrganClassChild.EP_ORGAN_CLASS_CHILD);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.class_id</code>. 班级id
     */
    public EpOrganClassChildRecord setClassId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpOrganClassChildRecord
     */
    public EpOrganClassChildRecord(Long id, Long classId, Long childId, Long orderId, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrganClassChild.EP_ORGAN_CLASS_CHILD);

        set(0, id);
        set(1, classId);
        set(2, childId);
        set(3, orderId);
        set(4, createAt);
        set(5, updateAt);
        set(6, remark);
        set(7, delFlag);
        set(8, version);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.child_id</code>. 孩子id
     */
    public EpOrganClassChildRecord setChildId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.order_id</code>. 订单id
     */
    public EpOrganClassChildRecord setOrderId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.class_id</code>. 班级id
     */
    public Long getClassId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.create_at</code>. 创建时间
     */
    public EpOrganClassChildRecord setCreateAt(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.update_at</code>. 更新时间
     */
    public EpOrganClassChildRecord setUpdateAt(Timestamp value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.order_id</code>. 订单id
     */
    public Long getOrderId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.remark</code>. 备注
     */
    public EpOrganClassChildRecord setRemark(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(4);
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(7);
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(5);
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(6);
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.version</code>.
     */
    public Long getVersion() {
        return (Long) get(8);
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
    // Record9 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Long, Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Long, Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row9) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.CLASS_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.CHILD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.ORDER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field8() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field9() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.VERSION;
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
        return getClassId();
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
        return getOrderId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value5() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value6() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value8() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value9() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value2(Long value) {
        setClassId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value3(Long value) {
        setChildId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value4(Long value) {
        setOrderId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value5(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value6(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value7(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value8(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value9(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord values(Long value1, Long value2, Long value3, Long value4, Timestamp value5, Timestamp value6, String value7, Boolean value8, Long value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>ep.ep_organ_class_child.del_flag</code>. 删除标记
     */
    public EpOrganClassChildRecord setDelFlag(Boolean value) {
        set(7, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.version</code>.
     */
    public EpOrganClassChildRecord setVersion(Long value) {
        set(8, value);
        return this;
    }
}
