/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpOrganClassScheduleConfig;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 机构班级行程配置表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganClassScheduleConfigRecord extends UpdatableRecordImpl<EpOrganClassScheduleConfigRecord> implements Record10<Long, Long, Long, Timestamp, Timestamp, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = -854311176;

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.id</code>. 主键
     */
    public EpOrganClassScheduleConfigRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpOrganClassScheduleConfigRecord
     */
    public EpOrganClassScheduleConfigRecord() {
        super(EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.class_id</code>. 班级id
     */
    public EpOrganClassScheduleConfigRecord setClassId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpOrganClassScheduleConfigRecord
     */
    public EpOrganClassScheduleConfigRecord(Long id, Long classId, Long catelogId, Timestamp startTime, Timestamp endTime, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG);

        set(0, id);
        set(1, classId);
        set(2, catelogId);
        set(3, startTime);
        set(4, endTime);
        set(5, createAt);
        set(6, updateAt);
        set(7, remark);
        set(8, delFlag);
        set(9, version);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.catelog_id</code>. 课程sku目录id
     */
    public EpOrganClassScheduleConfigRecord setCatelogId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.start_time</code>. 开始时间
     */
    public EpOrganClassScheduleConfigRecord setStartTime(Timestamp value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.class_id</code>. 班级id
     */
    public Long getClassId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.end_time</code>. 结束时间
     */
    public EpOrganClassScheduleConfigRecord setEndTime(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.catelog_id</code>. 课程sku目录id
     */
    public Long getCatelogId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.create_at</code>. 创建时间
     */
    public EpOrganClassScheduleConfigRecord setCreateAt(Timestamp value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.start_time</code>. 开始时间
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.update_at</code>. 更新时间
     */
    public EpOrganClassScheduleConfigRecord setUpdateAt(Timestamp value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.end_time</code>. 结束时间
     */
    public Timestamp getEndTime() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.remark</code>. 备注
     */
    public EpOrganClassScheduleConfigRecord setRemark(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(7);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(5);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(8);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(6);
    }

    /**
     * Getter for <code>ep.ep_organ_class_schedule_config.version</code>.
     */
    public Long getVersion() {
        return (Long) get(9);
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
    // Record10 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Long, Long, Long, Timestamp, Timestamp, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Long, Long, Long, Timestamp, Timestamp, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row10) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.CLASS_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.CATELOG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.START_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.END_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field9() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field10() {
        return EpOrganClassScheduleConfig.EP_ORGAN_CLASS_SCHEDULE_CONFIG.VERSION;
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
        return getCatelogId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value4() {
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value5() {
        return getEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value6() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value7() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value9() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value10() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value2(Long value) {
        setClassId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value3(Long value) {
        setCatelogId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value4(Timestamp value) {
        setStartTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value5(Timestamp value) {
        setEndTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value6(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value7(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value8(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value9(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord value10(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassScheduleConfigRecord values(Long value1, Long value2, Long value3, Timestamp value4, Timestamp value5, Timestamp value6, Timestamp value7, String value8, Boolean value9, Long value10) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.del_flag</code>. 删除标记
     */
    public EpOrganClassScheduleConfigRecord setDelFlag(Boolean value) {
        set(8, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_class_schedule_config.version</code>.
     */
    public EpOrganClassScheduleConfigRecord setVersion(Long value) {
        set(9, value);
        return this;
    }
}
