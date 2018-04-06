/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpOrganClassScheduleStatus;
import com.ep.domain.repository.domain.tables.EpOrganClassSchedule;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record17;
import org.jooq.Row17;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 班次行程表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganClassScheduleRecord extends UpdatableRecordImpl<EpOrganClassScheduleRecord> implements Record17<Long, Long, Long, Long, Timestamp, Integer, String, String, Integer, EpOrganClassScheduleStatus, Boolean, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = -1615385041;

    /**
     * Setter for <code>ep.ep_organ_class_schedule.id</code>. 主键
     */
    public EpOrganClassScheduleRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpOrganClassScheduleRecord
     */
    public EpOrganClassScheduleRecord() {
        super(EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.child_id</code>. 孩子id
     */
    public EpOrganClassScheduleRecord setChildId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpOrganClassScheduleRecord
     */
    public EpOrganClassScheduleRecord(Long id, Long childId, Long classId, Long orderId, Timestamp startTime, Integer duration, String catalogTitle, String catalogDesc, Integer catalogIndex, EpOrganClassScheduleStatus status, Boolean evaluateFlag, Long classCatalogId, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE);

        set(0, id);
        set(1, childId);
        set(2, classId);
        set(3, orderId);
        set(4, startTime);
        set(5, duration);
        set(6, catalogTitle);
        set(7, catalogDesc);
        set(8, catalogIndex);
        set(9, status);
        set(10, evaluateFlag);
        set(11, classCatalogId);
        set(12, createAt);
        set(13, updateAt);
        set(14, remark);
        set(15, delFlag);
        set(16, version);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.class_id</code>. 班次id
     */
    public EpOrganClassScheduleRecord setClassId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.order_id</code>. 订单id
     */
    public EpOrganClassScheduleRecord setOrderId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.start_time</code>. 开始时间
     */
    public EpOrganClassScheduleRecord setStartTime(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.class_id</code>. 班次id
     */
    public Long getClassId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.duration</code>. 持续时长
     */
    public EpOrganClassScheduleRecord setDuration(Integer value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.order_id</code>. 订单id
     */
    public Long getOrderId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.catalog_title</code>. 目录标题
     */
    public EpOrganClassScheduleRecord setCatalogTitle(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.start_time</code>. 开始时间
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.catalog_desc</code>. 目录描述
     */
    public EpOrganClassScheduleRecord setCatalogDesc(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.duration</code>. 持续时长
     */
    public Integer getDuration() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.catalog_index</code>. 目录索引
     */
    public EpOrganClassScheduleRecord setCatalogIndex(Integer value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.catalog_title</code>. 目录标题
     */
    public String getCatalogTitle() {
        return (String) get(6);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.status</code>. 状态：等待；正常；迟到；缺勤；请假；关闭
     */
    public EpOrganClassScheduleRecord setStatus(EpOrganClassScheduleStatus value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.catalog_desc</code>. 目录描述
     */
    public String getCatalogDesc() {
        return (String) get(7);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.evaluate_flag</code>. 随堂评价标记
     */
    public EpOrganClassScheduleRecord setEvaluateFlag(Boolean value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.catalog_index</code>. 目录索引
     */
    public Integer getCatalogIndex() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.class_catalog_id</code>. 班次课程内容目录id
     */
    public EpOrganClassScheduleRecord setClassCatalogId(Long value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.status</code>. 状态：等待；正常；迟到；缺勤；请假；关闭
     */
    public EpOrganClassScheduleStatus getStatus() {
        return (EpOrganClassScheduleStatus) get(9);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.create_at</code>. 创建时间
     */
    public EpOrganClassScheduleRecord setCreateAt(Timestamp value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.evaluate_flag</code>. 随堂评价标记
     */
    public Boolean getEvaluateFlag() {
        return (Boolean) get(10);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.update_at</code>. 更新时间
     */
    public EpOrganClassScheduleRecord setUpdateAt(Timestamp value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.class_catalog_id</code>. 班次课程内容目录id
     */
    public Long getClassCatalogId() {
        return (Long) get(11);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.remark</code>. 备注
     */
    public EpOrganClassScheduleRecord setRemark(String value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(12);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.del_flag</code>. 删除标记
     */
    public EpOrganClassScheduleRecord setDelFlag(Boolean value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(13);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(14);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.version</code>.
     */
    public Long getVersion() {
        return (Long) get(16);
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
    // Record17 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row17<Long, Long, Long, Long, Timestamp, Integer, String, String, Integer, EpOrganClassScheduleStatus, Boolean, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row17) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row17<Long, Long, Long, Long, Timestamp, Integer, String, String, Integer, EpOrganClassScheduleStatus, Boolean, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row17) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CHILD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CLASS_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.ORDER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.START_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.DURATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CATALOG_TITLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CATALOG_DESC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field9() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpOrganClassScheduleStatus> field10() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field11() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.EVALUATE_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field12() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field13() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field14() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field15() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field16() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field17() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.VERSION;
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
        return getChildId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getClassId();
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
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getDuration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getCatalogTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getCatalogDesc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value9() {
        return getCatalogIndex();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleStatus value10() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value11() {
        return getEvaluateFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value12() {
        return getClassCatalogId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value13() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value14() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value15() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value16() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value17() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value2(Long value) {
        setChildId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value3(Long value) {
        setClassId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value4(Long value) {
        setOrderId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value5(Timestamp value) {
        setStartTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value6(Integer value) {
        setDuration(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value7(String value) {
        setCatalogTitle(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value8(String value) {
        setCatalogDesc(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value9(Integer value) {
        setCatalogIndex(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value10(EpOrganClassScheduleStatus value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value11(Boolean value) {
        setEvaluateFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value12(Long value) {
        setClassCatalogId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value13(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value14(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value15(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value16(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value17(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord values(Long value1, Long value2, Long value3, Long value4, Timestamp value5, Integer value6, String value7, String value8, Integer value9, EpOrganClassScheduleStatus value10, Boolean value11, Long value12, Timestamp value13, Timestamp value14, String value15, Boolean value16, Long value17) {
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
        value16(value16);
        value17(value17);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>ep.ep_organ_class_schedule.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(15);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.version</code>.
     */
    public EpOrganClassScheduleRecord setVersion(Long value) {
        set(16, value);
        return this;
    }
}
