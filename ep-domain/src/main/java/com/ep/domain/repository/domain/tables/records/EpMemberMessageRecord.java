/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpMemberMessageStatus;
import com.ep.domain.repository.domain.enums.EpMemberMessageType;
import com.ep.domain.repository.domain.tables.EpMemberMessage;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record15;
import org.jooq.Row15;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 会员消息表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpMemberMessageRecord extends UpdatableRecordImpl<EpMemberMessageRecord> implements Record15<Long, Long, String, String, Long, Long, EpMemberMessageType, EpMemberMessageStatus, String, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = -1147876091;

    /**
     * Create a detached EpMemberMessageRecord
     */
    public EpMemberMessageRecord() {
        super(EpMemberMessage.EP_MEMBER_MESSAGE);
    }

    /**
     * Create a detached, initialised EpMemberMessageRecord
     */
    public EpMemberMessageRecord(Long id, Long senderOgnAccountId, String senderName, String senderDesc, Long memberId, Long childId, EpMemberMessageType type, EpMemberMessageStatus status, String content, Long sourceId, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpMemberMessage.EP_MEMBER_MESSAGE);

        set(0, id);
        set(1, senderOgnAccountId);
        set(2, senderName);
        set(3, senderDesc);
        set(4, memberId);
        set(5, childId);
        set(6, type);
        set(7, status);
        set(8, content);
        set(9, sourceId);
        set(10, createAt);
        set(11, updateAt);
        set(12, remark);
        set(13, delFlag);
        set(14, version);
    }

    /**
     * Getter for <code>ep.ep_member_message.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_member_message.id</code>.
     */
    public EpMemberMessageRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.sender_ogn_account_id</code>. 发件人对应机构账户id
     */
    public Long getSenderOgnAccountId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_member_message.sender_ogn_account_id</code>. 发件人对应机构账户id
     */
    public EpMemberMessageRecord setSenderOgnAccountId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.sender_name</code>. 发件人姓名
     */
    public String getSenderName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ep.ep_member_message.sender_name</code>. 发件人姓名
     */
    public EpMemberMessageRecord setSenderName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.sender_desc</code>. 发件人描述
     */
    public String getSenderDesc() {
        return (String) get(3);
    }

    /**
     * Setter for <code>ep.ep_member_message.sender_desc</code>. 发件人描述
     */
    public EpMemberMessageRecord setSenderDesc(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.member_id</code>. 会员id
     */
    public Long getMemberId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_member_message.member_id</code>. 会员id
     */
    public EpMemberMessageRecord setMemberId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>ep.ep_member_message.child_id</code>. 孩子id
     */
    public EpMemberMessageRecord setChildId(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.type</code>. 类型：随堂评价
     */
    public EpMemberMessageType getType() {
        return (EpMemberMessageType) get(6);
    }

    /**
     * Setter for <code>ep.ep_member_message.type</code>. 类型：随堂评价
     */
    public EpMemberMessageRecord setType(EpMemberMessageType value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.status</code>. 状态：未读；已读；
     */
    public EpMemberMessageStatus getStatus() {
        return (EpMemberMessageStatus) get(7);
    }

    /**
     * Setter for <code>ep.ep_member_message.status</code>. 状态：未读；已读；
     */
    public EpMemberMessageRecord setStatus(EpMemberMessageStatus value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.content</code>. 消息内容
     */
    public String getContent() {
        return (String) get(8);
    }

    /**
     * Setter for <code>ep.ep_member_message.content</code>. 消息内容
     */
    public EpMemberMessageRecord setContent(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.source_id</code>. 业务id
     */
    public Long getSourceId() {
        return (Long) get(9);
    }

    /**
     * Setter for <code>ep.ep_member_message.source_id</code>. 业务id
     */
    public EpMemberMessageRecord setSourceId(Long value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(10);
    }

    /**
     * Setter for <code>ep.ep_member_message.create_at</code>. 创建时间
     */
    public EpMemberMessageRecord setCreateAt(Timestamp value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(11);
    }

    /**
     * Setter for <code>ep.ep_member_message.update_at</code>. 更新时间
     */
    public EpMemberMessageRecord setUpdateAt(Timestamp value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(12);
    }

    /**
     * Setter for <code>ep.ep_member_message.remark</code>. 备注
     */
    public EpMemberMessageRecord setRemark(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_message.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(13);
    }

    /**
     * Setter for <code>ep.ep_member_message.del_flag</code>. 删除标记
     */
    public EpMemberMessageRecord setDelFlag(Boolean value) {
        set(13, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>ep.ep_member_message.version</code>.
     */
    public Long getVersion() {
        return (Long) get(14);
    }

    // -------------------------------------------------------------------------
    // Record15 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>ep.ep_member_message.version</code>.
     */
    public EpMemberMessageRecord setVersion(Long value) {
        set(14, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row15<Long, Long, String, String, Long, Long, EpMemberMessageType, EpMemberMessageStatus, String, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row15) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row15<Long, Long, String, String, Long, Long, EpMemberMessageType, EpMemberMessageStatus, String, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row15) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.SENDER_OGN_ACCOUNT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.SENDER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.SENDER_DESC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.MEMBER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.CHILD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpMemberMessageType> field7() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpMemberMessageStatus> field8() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.CONTENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field10() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.SOURCE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field11() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field12() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field13() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field14() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field15() {
        return EpMemberMessage.EP_MEMBER_MESSAGE.VERSION;
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
        return getSenderOgnAccountId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getSenderName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getSenderDesc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getMemberId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getChildId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageType value7() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageStatus value8() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value10() {
        return getSourceId();
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
    public EpMemberMessageRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value2(Long value) {
        setSenderOgnAccountId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value3(String value) {
        setSenderName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value4(String value) {
        setSenderDesc(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value5(Long value) {
        setMemberId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value6(Long value) {
        setChildId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value7(EpMemberMessageType value) {
        setType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value8(EpMemberMessageStatus value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value9(String value) {
        setContent(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value10(Long value) {
        setSourceId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value11(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value12(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value13(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord value14(Boolean value) {
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
    public EpMemberMessageRecord value15(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberMessageRecord values(Long value1, Long value2, String value3, String value4, Long value5, Long value6, EpMemberMessageType value7, EpMemberMessageStatus value8, String value9, Long value10, Timestamp value11, Timestamp value12, String value13, Boolean value14, Long value15) {
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
}
