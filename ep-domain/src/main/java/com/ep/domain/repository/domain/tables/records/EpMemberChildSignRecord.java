/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpMemberChildSign;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 孩子签名表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpMemberChildSignRecord extends UpdatableRecordImpl<EpMemberChildSignRecord> implements Record8<Long, Long, String, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 452736027;

    /**
     * Setter for <code>ep.ep_member_child_sign.id</code>. 主键
     */
    public EpMemberChildSignRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpMemberChildSignRecord
     */
    public EpMemberChildSignRecord() {
        super(EpMemberChildSign.EP_MEMBER_CHILD_SIGN);
    }

    /**
     * Setter for <code>ep.ep_member_child_sign.child_id</code>. 孩子id
     */
    public EpMemberChildSignRecord setChildId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpMemberChildSignRecord
     */
    public EpMemberChildSignRecord(Long id, Long childId, String content, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpMemberChildSign.EP_MEMBER_CHILD_SIGN);

        set(0, id);
        set(1, childId);
        set(2, content);
        set(3, createAt);
        set(4, updateAt);
        set(5, remark);
        set(6, delFlag);
        set(7, version);
    }

    /**
     * Setter for <code>ep.ep_member_child_sign.content</code>. 签名内容
     */
    public EpMemberChildSignRecord setContent(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_sign.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_member_child_sign.create_at</code>. 创建时间
     */
    public EpMemberChildSignRecord setCreateAt(Timestamp value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_sign.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_member_child_sign.update_at</code>. 更新时间
     */
    public EpMemberChildSignRecord setUpdateAt(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_sign.content</code>. 签名内容
     */
    public String getContent() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ep.ep_member_child_sign.remark</code>. 备注
     */
    public EpMemberChildSignRecord setRemark(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_sign.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(3);
    }

    /**
     * Getter for <code>ep.ep_member_child_sign.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(6);
    }

    /**
     * Getter for <code>ep.ep_member_child_sign.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(4);
    }

    /**
     * Getter for <code>ep.ep_member_child_sign.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(5);
    }

    /**
     * Getter for <code>ep.ep_member_child_sign.version</code>.
     */
    public Long getVersion() {
        return (Long) get(7);
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
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, String, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, String, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpMemberChildSign.EP_MEMBER_CHILD_SIGN.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpMemberChildSign.EP_MEMBER_CHILD_SIGN.CHILD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return EpMemberChildSign.EP_MEMBER_CHILD_SIGN.CONTENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field4() {
        return EpMemberChildSign.EP_MEMBER_CHILD_SIGN.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return EpMemberChildSign.EP_MEMBER_CHILD_SIGN.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return EpMemberChildSign.EP_MEMBER_CHILD_SIGN.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field7() {
        return EpMemberChildSign.EP_MEMBER_CHILD_SIGN.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field8() {
        return EpMemberChildSign.EP_MEMBER_CHILD_SIGN.VERSION;
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
    public String value3() {
        return getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value4() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value5() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value7() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value8() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSignRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSignRecord value2(Long value) {
        setChildId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSignRecord value3(String value) {
        setContent(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSignRecord value4(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSignRecord value5(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSignRecord value6(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSignRecord value7(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSignRecord value8(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSignRecord values(Long value1, Long value2, String value3, Timestamp value4, Timestamp value5, String value6, Boolean value7, Long value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>ep.ep_member_child_sign.del_flag</code>. 删除标记
     */
    public EpMemberChildSignRecord setDelFlag(Boolean value) {
        set(6, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_member_child_sign.version</code>.
     */
    public EpMemberChildSignRecord setVersion(Long value) {
        set(7, value);
        return this;
    }
}
