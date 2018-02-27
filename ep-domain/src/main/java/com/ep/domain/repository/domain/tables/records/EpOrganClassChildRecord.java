/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpOrganClassChild;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
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
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpOrganClassChildRecord extends UpdatableRecordImpl<EpOrganClassChildRecord> implements Record13<Long, Long, Long, Long, Integer, Integer, Boolean, Integer, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 777378858;

    /**
     * Setter for <code>ep.ep_organ_class_child.id</code>. 主键
     */
    public EpOrganClassChildRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.class_id</code>. 班级id
     */
    public EpOrganClassChildRecord setClassId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.class_id</code>. 班级id
     */
    public Long getClassId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.child_id</code>. 孩子id
     */
    public EpOrganClassChildRecord setChildId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.order_id</code>. 订单id
     */
    public EpOrganClassChildRecord setOrderId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.order_id</code>. 订单id
     */
    public Long getOrderId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.honor_num</code>. 获得的荣誉数
     */
    public EpOrganClassChildRecord setHonorNum(Integer value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.honor_num</code>. 获得的荣誉数
     */
    public Integer getHonorNum() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.schedule_comment_num</code>. 获得的老师评价数
     */
    public EpOrganClassChildRecord setScheduleCommentNum(Integer value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.schedule_comment_num</code>. 获得的老师评价数
     */
    public Integer getScheduleCommentNum() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.course_comment_flag</code>. 是否评论课程
     */
    public EpOrganClassChildRecord setCourseCommentFlag(Boolean value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.course_comment_flag</code>. 是否评论课程
     */
    public Boolean getCourseCommentFlag() {
        return (Boolean) get(6);
    }

    /**
     * Create a detached, initialised EpOrganClassChildRecord
     */
    public EpOrganClassChildRecord(Long id, Long classId, Long childId, Long orderId, Integer honorNum, Integer scheduleCommentNum, Boolean courseCommentFlag, Integer lastCatalogIndex, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrganClassChild.EP_ORGAN_CLASS_CHILD);

        set(0, id);
        set(1, classId);
        set(2, childId);
        set(3, orderId);
        set(4, honorNum);
        set(5, scheduleCommentNum);
        set(6, courseCommentFlag);
        set(7, lastCatalogIndex);
        set(8, createAt);
        set(9, updateAt);
        set(10, remark);
        set(11, delFlag);
        set(12, version);
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.last_catalog_index</code>. 最近参加的课时目录
     */
    public Integer getLastCatalogIndex() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.create_at</code>. 创建时间
     */
    public EpOrganClassChildRecord setCreateAt(Timestamp value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(8);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.update_at</code>. 更新时间
     */
    public EpOrganClassChildRecord setUpdateAt(Timestamp value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(9);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.remark</code>. 备注
     */
    public EpOrganClassChildRecord setRemark(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(10);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.del_flag</code>. 删除标记
     */
    public EpOrganClassChildRecord setDelFlag(Boolean value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(11);
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.version</code>.
     */
    public EpOrganClassChildRecord setVersion(Long value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_child.version</code>.
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
    public Row13<Long, Long, Long, Long, Integer, Integer, Boolean, Integer, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<Long, Long, Long, Long, Integer, Integer, Boolean, Integer, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row13) super.valuesRow();
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
    public Field<Integer> field5() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.HONOR_NUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.SCHEDULE_COMMENT_NUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field7() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.COURSE_COMMENT_FLAG;
    }

    /**
     * Setter for <code>ep.ep_organ_class_child.last_catalog_index</code>. 最近参加的课时目录
     */
    public EpOrganClassChildRecord setLastCatalogIndex(Integer value) {
        set(7, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field10() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field12() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field13() {
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
    public Integer value5() {
        return getHonorNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getScheduleCommentNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value7() {
        return getCourseCommentFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field8() {
        return EpOrganClassChild.EP_ORGAN_CLASS_CHILD.LAST_CATALOG_INDEX;
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
    public EpOrganClassChildRecord value5(Integer value) {
        setHonorNum(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value6(Integer value) {
        setScheduleCommentNum(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value7(Boolean value) {
        setCourseCommentFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value8() {
        return getLastCatalogIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value9(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value10(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value11(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value12(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value13(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord values(Long value1, Long value2, Long value3, Long value4, Integer value5, Integer value6, Boolean value7, Integer value8, Timestamp value9, Timestamp value10, String value11, Boolean value12, Long value13) {
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
     * Create a detached EpOrganClassChildRecord
     */
    public EpOrganClassChildRecord() {
        super(EpOrganClassChild.EP_ORGAN_CLASS_CHILD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassChildRecord value8(Integer value) {
        setLastCatalogIndex(value);
        return this;
    }
}
