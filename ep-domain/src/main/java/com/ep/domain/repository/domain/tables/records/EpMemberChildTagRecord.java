/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpMemberChildTag;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 孩子标签记录表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpMemberChildTagRecord extends UpdatableRecordImpl<EpMemberChildTagRecord> implements Record12<Long, Long, Long, Long, Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = -1529129353;

    /**
     * Setter for <code>ep.ep_member_child_tag.id</code>. 主键
     */
    public EpMemberChildTagRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpMemberChildTagRecord
     */
    public EpMemberChildTagRecord() {
        super(EpMemberChildTag.EP_MEMBER_CHILD_TAG);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.child_id</code>. 孩子id
     */
    public EpMemberChildTagRecord setChildId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpMemberChildTagRecord
     */
    public EpMemberChildTagRecord(Long id, Long childId, Long ognId, Long courseId, Long classId, Long classCatelogId, Long tagId, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpMemberChildTag.EP_MEMBER_CHILD_TAG);

        set(0, id);
        set(1, childId);
        set(2, ognId);
        set(3, courseId);
        set(4, classId);
        set(5, classCatelogId);
        set(6, tagId);
        set(7, createAt);
        set(8, updateAt);
        set(9, remark);
        set(10, delFlag);
        set(11, version);
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(1);
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.ogn_id</code>. 机构id
     */
    public Long getOgnId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.ogn_id</code>. 机构id
     */
    public EpMemberChildTagRecord setOgnId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.course_id</code>. 课程id
     */
    public Long getCourseId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.course_id</code>. 课程id
     */
    public EpMemberChildTagRecord setCourseId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.class_id</code>. 班次id
     */
    public Long getClassId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.class_id</code>. 班次id
     */
    public EpMemberChildTagRecord setClassId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.class_catelog_id</code>. 课程内容目录id
     */
    public Long getClassCatelogId() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.class_catelog_id</code>. 课程内容目录id
     */
    public EpMemberChildTagRecord setClassCatelogId(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.tag_id</code>. 标签id
     */
    public Long getTagId() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.tag_id</code>. 标签id
     */
    public EpMemberChildTagRecord setTagId(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.create_at</code>. 创建时间
     */
    public EpMemberChildTagRecord setCreateAt(Timestamp value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(8);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.update_at</code>. 更新时间
     */
    public EpMemberChildTagRecord setUpdateAt(Timestamp value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(9);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.remark</code>. 备注
     */
    public EpMemberChildTagRecord setRemark(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_tag.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(10);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.del_flag</code>. 删除标记
     */
    public EpMemberChildTagRecord setDelFlag(Boolean value) {
        set(10, value);
        return this;
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
    // Record12 type implementation
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>ep.ep_member_child_tag.version</code>.
     */
    public Long getVersion() {
        return (Long) get(11);
    }

    /**
     * Setter for <code>ep.ep_member_child_tag.version</code>.
     */
    public EpMemberChildTagRecord setVersion(Long value) {
        set(11, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.CHILD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, Long, Long, Long, Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, Long, Long, Long, Long, Long, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row12) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.COURSE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.CLASS_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.CLASS_CATELOG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field7() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.TAG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.REMARK;
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
    public Field<Boolean> field11() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field12() {
        return EpMemberChildTag.EP_MEMBER_CHILD_TAG.VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getOgnId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getCourseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getClassId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getClassCatelogId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value7() {
        return getTagId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value8() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value9() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value2(Long value) {
        setChildId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value11() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value12() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value3(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value4(Long value) {
        setCourseId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value5(Long value) {
        setClassId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value6(Long value) {
        setClassCatelogId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value7(Long value) {
        setTagId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value8(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value9(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value10(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value11(Boolean value) {
        setDelFlag(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord value12(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTagRecord values(Long value1, Long value2, Long value3, Long value4, Long value5, Long value6, Long value7, Timestamp value8, Timestamp value9, String value10, Boolean value11, Long value12) {
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
        return this;
    }
}
