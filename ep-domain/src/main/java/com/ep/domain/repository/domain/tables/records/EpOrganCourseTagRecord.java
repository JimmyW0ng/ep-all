/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpOrganCourseTag;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
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
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpOrganCourseTagRecord extends UpdatableRecordImpl<EpOrganCourseTagRecord> implements Record9<Long, Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 1301703735;

    /**
     * Setter for <code>ep.ep_organ_course_tag.id</code>.
     */
    public EpOrganCourseTagRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Create a detached, initialised EpOrganCourseTagRecord
     */
    public EpOrganCourseTagRecord(Long id, Long tagId, Long courseId, Long sort, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrganCourseTag.EP_ORGAN_COURSE_TAG);

        set(0, id);
        set(1, tagId);
        set(2, courseId);
        set(3, sort);
        set(4, createAt);
        set(5, updateAt);
        set(6, remark);
        set(7, delFlag);
        set(8, version);
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.tag_id</code>. 标签id
     */
    public Long getTagId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.tag_id</code>. 标签id
     */
    public EpOrganCourseTagRecord setTagId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.course_id</code>. 课程id
     */
    public Long getCourseId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.course_id</code>. 课程id
     */
    public EpOrganCourseTagRecord setCourseId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.sort</code>. 排序
     */
    public Long getSort() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.sort</code>. 排序
     */
    public EpOrganCourseTagRecord setSort(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.create_at</code>. 创建时间
     */
    public EpOrganCourseTagRecord setCreateAt(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.update_at</code>. 更新时间
     */
    public EpOrganCourseTagRecord setUpdateAt(Timestamp value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(6);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.remark</code>. 备注
     */
    public EpOrganCourseTagRecord setRemark(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(7);
    }

    /**
     * Setter for <code>ep.ep_organ_course_tag.del_flag</code>. 删除标记
     */
    public EpOrganCourseTagRecord setDelFlag(Boolean value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_course_tag.version</code>.
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
     * Setter for <code>ep.ep_organ_course_tag.version</code>.
     */
    public EpOrganCourseTagRecord setVersion(Long value) {
        set(8, value);
        return this;
    }

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
    public Field<Long> field1() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.ID;
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
    public Field<Long> field2() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.TAG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.COURSE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.SORT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field8() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.DEL_FLAG;
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
    public Field<Long> field9() {
        return EpOrganCourseTag.EP_ORGAN_COURSE_TAG.VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getTagId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getCourseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getSort();
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
    public EpOrganCourseTagRecord value1(Long value) {
        setId(value);
        return this;
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
    public EpOrganCourseTagRecord value2(Long value) {
        setTagId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value3(Long value) {
        setCourseId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value4(Long value) {
        setSort(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value5(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value6(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value7(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value8(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord value9(Long value) {
        setVersion(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EpOrganCourseTagRecord
     */
    public EpOrganCourseTagRecord() {
        super(EpOrganCourseTag.EP_ORGAN_COURSE_TAG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseTagRecord values(Long value1, Long value2, Long value3, Long value4, Timestamp value5, Timestamp value6, String value7, Boolean value8, Long value9) {
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
}
