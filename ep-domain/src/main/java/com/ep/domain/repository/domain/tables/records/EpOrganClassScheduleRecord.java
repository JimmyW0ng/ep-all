/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpOrganClassScheduleScheduleStatus;
import com.ep.domain.repository.domain.tables.EpOrganClassSchedule;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 机构行程信息表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganClassScheduleRecord extends UpdatableRecordImpl<EpOrganClassScheduleRecord> implements Record9<Long, Long, Long, EpOrganClassScheduleScheduleStatus, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 1740992789;

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
     * Setter for <code>ep.ep_organ_class_schedule.class_id</code>. 班级id
     */
    public EpOrganClassScheduleRecord setClassId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpOrganClassScheduleRecord
     */
    public EpOrganClassScheduleRecord(Long id, Long classId, Long childId, EpOrganClassScheduleScheduleStatus scheduleStatus, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE);

        set(0, id);
        set(1, classId);
        set(2, childId);
        set(3, scheduleStatus);
        set(4, createAt);
        set(5, updateAt);
        set(6, remark);
        set(7, delFlag);
        set(8, version);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.child_id</code>. 孩子id
     */
    public EpOrganClassScheduleRecord setChildId(Long value) {
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
     * Setter for <code>ep.ep_organ_class_schedule.schedule_status</code>. 行程状态：正常；迟到；缺席
     */
    public EpOrganClassScheduleRecord setScheduleStatus(EpOrganClassScheduleScheduleStatus value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.class_id</code>. 班级id
     */
    public Long getClassId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.create_at</code>. 创建时间
     */
    public EpOrganClassScheduleRecord setCreateAt(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.update_at</code>. 更新时间
     */
    public EpOrganClassScheduleRecord setUpdateAt(Timestamp value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.schedule_status</code>. 行程状态：正常；迟到；缺席
     */
    public EpOrganClassScheduleScheduleStatus getScheduleStatus() {
        return (EpOrganClassScheduleScheduleStatus) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.remark</code>. 备注
     */
    public EpOrganClassScheduleRecord setRemark(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(4);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(7);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(5);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(6);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule.version</code>.
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
    public Row9<Long, Long, Long, EpOrganClassScheduleScheduleStatus, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<Long, Long, Long, EpOrganClassScheduleScheduleStatus, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row9) super.valuesRow();
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
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CLASS_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CHILD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpOrganClassScheduleScheduleStatus> field4() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.SCHEDULE_STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field8() {
        return EpOrganClassSchedule.EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field9() {
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
    public EpOrganClassScheduleScheduleStatus value4() {
        return getScheduleStatus();
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
    public EpOrganClassScheduleRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value2(Long value) {
        setClassId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value3(Long value) {
        setChildId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value4(EpOrganClassScheduleScheduleStatus value) {
        setScheduleStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value5(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value6(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value7(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value8(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord value9(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleRecord values(Long value1, Long value2, Long value3, EpOrganClassScheduleScheduleStatus value4, Timestamp value5, Timestamp value6, String value7, Boolean value8, Long value9) {
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
     * Setter for <code>ep.ep_organ_class_schedule.del_flag</code>. 删除标记
     */
    public EpOrganClassScheduleRecord setDelFlag(Boolean value) {
        set(7, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule.version</code>.
     */
    public EpOrganClassScheduleRecord setVersion(Long value) {
        set(8, value);
        return this;
    }
}
