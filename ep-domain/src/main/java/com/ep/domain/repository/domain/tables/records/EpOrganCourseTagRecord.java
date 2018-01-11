/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpOrganCourseTag;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 课程标签表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganCourseTagRecord extends UpdatableRecordImpl<EpOrganCourseTagRecord> implements Record10<Long, Long, String, Byte, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = -873816913;

    /**
     * Setter for <code>ep.ep_organ_course_tag.id</code>.
     */
    public EpOrganCourseTagRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpOrganCourseTagRecord
     */
    public EpOrganCourseTagRecord() {
        super(EpOrganCourseTag.EP_ORGAN_COURSE_TAG);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.course_id</code>. 课程id
     */
    public EpOrganCourseTagRecord setCourseId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpOrganCourseTagRecord
     */
    public EpOrganCourseTagRecord(Long id, Long courseId, String tagName, Byte tagLevel, Long sort, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrganCourseTag.EP_ORGAN_COURSE_TAG);

        set(0, id);
        set(1, courseId);
        set(2, tagName);
        set(3, tagLevel);
        set(4, sort);
        set(5, createAt);
        set(6, updateAt);
        set(7, remark);
        set(8, delFlag);
        set(9, version);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.tag_name</code>. 标签名称
     */
    public EpOrganCourseTagRecord setTagName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.tag_level</code>. 标签等级
     */
    public EpOrganCourseTagRecord setTagLevel(Byte value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.course_id</code>. 课程id
     */
    public Long getCourseId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.sort</code>. 排序
     */
    public EpOrganCourseTagRecord setSort(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.tag_name</code>. 标签名称
     */
    public String getTagName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.create_at</code>. 创建时间
     */
    public EpOrganCourseTagRecord setCreateAt(Timestamp value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.tag_level</code>. 标签等级
     */
    public Byte getTagLevel() {
        return (Byte) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.update_at</code>. 更新时间
     */
    public EpOrganCourseTagRecord setUpdateAt(Timestamp value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.sort</code>. 排序
     */
    public Long getSort() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.remark</code>. 备注
     */
    public EpOrganCourseTagRecord setRemark(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(5);
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(8);
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(6);
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(7);
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.version</code>.
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
    public Row10<Long, Long, String, Byte, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Long, Long, String, Byte, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row10) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.COURSE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.TAG_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field4() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.TAG_LEVEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.SORT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field9() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field10() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.VERSION;
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
        return getCourseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getTagName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value4() {
        return getTagLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getSort();
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
    public EpOrganCourseTagRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value2(Long value) {
        setCourseId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value3(String value) {
        setTagName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value4(Byte value) {
        setTagLevel(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value5(Long value) {
        setSort(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value6(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value7(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value8(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value9(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value10(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord values(Long value1, Long value2, String value3, Byte value4, Long value5, Timestamp value6, Timestamp value7, String value8, Boolean value9, Long value10) {
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
     * Setter for <code>ep.ep_organ_course_tag.del_flag</code>. 删除标记
     */
    public EpOrganCourseTagRecord setDelFlag(Boolean value) {
        set(8, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.version</code>.
     */
    public EpOrganCourseTagRecord setVersion(Long value) {
        set(9, value);
        return this;
    }
}
