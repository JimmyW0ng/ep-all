/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpOrganAccountStatus;
import com.ep.domain.repository.domain.tables.EpOrganAccount;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 机构账户关联信息表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpOrganAccountRecord extends UpdatableRecordImpl<EpOrganAccountRecord> implements Record12<Long, String, String, String, Long, EpOrganAccountStatus, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 1847926626;

    /**
     * Setter for <code>ep.ep_organ_account.id</code>. 主键
     */
    public EpOrganAccountRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_organ_account.account_name</code>. 机构内部名称
     */
    public EpOrganAccountRecord setAccountName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.account_name</code>. 机构内部名称
     */
    public String getAccountName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>ep.ep_organ_account.nick_name</code>. 对外昵称
     */
    public EpOrganAccountRecord setNickName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.nick_name</code>. 对外昵称
     */
    public String getNickName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ep.ep_organ_account.introduce</code>. 介绍
     */
    public EpOrganAccountRecord setIntroduce(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.introduce</code>. 介绍
     */
    public String getIntroduce() {
        return (String) get(3);
    }

    /**
     * Setter for <code>ep.ep_organ_account.ogn_id</code>. 所属机构id
     */
    public EpOrganAccountRecord setOgnId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.ogn_id</code>. 所属机构id
     */
    public Long getOgnId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_organ_account.status</code>. 状态：待激活；正常；已冻结；已注销；
     */
    public EpOrganAccountRecord setStatus(EpOrganAccountStatus value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.status</code>. 状态：待激活；正常；已冻结；已注销；
     */
    public EpOrganAccountStatus getStatus() {
        return (EpOrganAccountStatus) get(5);
    }

    /**
     * Setter for <code>ep.ep_organ_account.refer_mobile</code>. 关联手机号
     */
    public EpOrganAccountRecord setReferMobile(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.refer_mobile</code>. 关联手机号
     */
    public Long getReferMobile() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>ep.ep_organ_account.create_at</code>. 创建时间
     */
    public EpOrganAccountRecord setCreateAt(Timestamp value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>ep.ep_organ_account.update_at</code>. 更新时间
     */
    public EpOrganAccountRecord setUpdateAt(Timestamp value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(8);
    }

    /**
     * Setter for <code>ep.ep_organ_account.remark</code>. 备注
     */
    public EpOrganAccountRecord setRemark(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(9);
    }

    /**
     * Setter for <code>ep.ep_organ_account.del_flag</code>. 删除标记
     */
    public EpOrganAccountRecord setDelFlag(Boolean value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(10);
    }

    /**
     * Setter for <code>ep.ep_organ_account.version</code>.
     */
    public EpOrganAccountRecord setVersion(Long value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_organ_account.version</code>.
     */
    public Long getVersion() {
        return (Long) get(11);
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
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, String, String, String, Long, EpOrganAccountStatus, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, String, String, String, Long, EpOrganAccountStatus, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row12) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.ACCOUNT_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.NICK_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.INTRODUCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpOrganAccountStatus> field6() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field7() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.REFER_MOBILE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field9() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field11() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field12() {
        return EpOrganAccount.EP_ORGAN_ACCOUNT.VERSION;
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
    public String value2() {
        return getAccountName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getNickName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getIntroduce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getOgnId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountStatus value6() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value7() {
        return getReferMobile();
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
    public EpOrganAccountRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value2(String value) {
        setAccountName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value3(String value) {
        setNickName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value4(String value) {
        setIntroduce(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value5(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value6(EpOrganAccountStatus value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value7(Long value) {
        setReferMobile(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value8(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value9(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value10(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value11(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord value12(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganAccountRecord values(Long value1, String value2, String value3, String value4, Long value5, EpOrganAccountStatus value6, Long value7, Timestamp value8, Timestamp value9, String value10, Boolean value11, Long value12) {
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

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EpOrganAccountRecord
     */
    public EpOrganAccountRecord() {
        super(EpOrganAccount.EP_ORGAN_ACCOUNT);
    }

    /**
     * Create a detached, initialised EpOrganAccountRecord
     */
    public EpOrganAccountRecord(Long id, String accountName, String nickName, String introduce, Long ognId, EpOrganAccountStatus status, Long referMobile, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpOrganAccount.EP_ORGAN_ACCOUNT);

        set(0, id);
        set(1, accountName);
        set(2, nickName);
        set(3, introduce);
        set(4, ognId);
        set(5, status);
        set(6, referMobile);
        set(7, createAt);
        set(8, updateAt);
        set(9, remark);
        set(10, delFlag);
        set(11, version);
    }
}
