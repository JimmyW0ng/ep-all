/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.tables.EpSystemUserRole;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * 用户-角色
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpSystemUserRoleRecord extends UpdatableRecordImpl<EpSystemUserRoleRecord> implements Record8<Long, Long, Long, String, Timestamp, Timestamp, Boolean, Long> {

    private static final long serialVersionUID = 1031459082;

    /**
     * Setter for <code>ep.ep_system_user_role.id</code>.
     */
    public EpSystemUserRoleRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_user_role.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_system_user_role.user_id</code>. 用户id
     */
    public EpSystemUserRoleRecord setUserId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_user_role.user_id</code>. 用户id
     */
    public Long getUserId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_system_user_role.role_id</code>. 角色id
     */
    public EpSystemUserRoleRecord setRoleId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_user_role.role_id</code>. 角色id
     */
    public Long getRoleId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_system_user_role.role_code</code>. 角色code
     */
    public EpSystemUserRoleRecord setRoleCode(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_user_role.role_code</code>. 角色code
     */
    public String getRoleCode() {
        return (String) get(3);
    }

    /**
     * Setter for <code>ep.ep_system_user_role.create_at</code>. 创建时间
     */
    public EpSystemUserRoleRecord setCreateAt(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_user_role.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>ep.ep_system_user_role.update_at</code>. 更新时间
     */
    public EpSystemUserRoleRecord setUpdateAt(Timestamp value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_user_role.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>ep.ep_system_user_role.del_flag</code>. 删除标志
     */
    public EpSystemUserRoleRecord setDelFlag(Boolean value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_user_role.del_flag</code>. 删除标志
     */
    public Boolean getDelFlag() {
        return (Boolean) get(6);
    }

    /**
     * Setter for <code>ep.ep_system_user_role.version</code>.
     */
    public EpSystemUserRoleRecord setVersion(Long value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_user_role.version</code>.
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
    public Row8<Long, Long, Long, String, Timestamp, Timestamp, Boolean, Long> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, Long, Long, String, Timestamp, Timestamp, Boolean, Long> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpSystemUserRole.EP_SYSTEM_USER_ROLE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpSystemUserRole.EP_SYSTEM_USER_ROLE.USER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpSystemUserRole.EP_SYSTEM_USER_ROLE.ROLE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return EpSystemUserRole.EP_SYSTEM_USER_ROLE.ROLE_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return EpSystemUserRole.EP_SYSTEM_USER_ROLE.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return EpSystemUserRole.EP_SYSTEM_USER_ROLE.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field7() {
        return EpSystemUserRole.EP_SYSTEM_USER_ROLE.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field8() {
        return EpSystemUserRole.EP_SYSTEM_USER_ROLE.VERSION;
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
        return getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getRoleId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getRoleCode();
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
    public EpSystemUserRoleRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemUserRoleRecord value2(Long value) {
        setUserId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemUserRoleRecord value3(Long value) {
        setRoleId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemUserRoleRecord value4(String value) {
        setRoleCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemUserRoleRecord value5(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemUserRoleRecord value6(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemUserRoleRecord value7(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemUserRoleRecord value8(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemUserRoleRecord values(Long value1, Long value2, Long value3, String value4, Timestamp value5, Timestamp value6, Boolean value7, Long value8) {
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
     * Create a detached EpSystemUserRoleRecord
     */
    public EpSystemUserRoleRecord() {
        super(EpSystemUserRole.EP_SYSTEM_USER_ROLE);
    }

    /**
     * Create a detached, initialised EpSystemUserRoleRecord
     */
    public EpSystemUserRoleRecord(Long id, Long userId, Long roleId, String roleCode, Timestamp createAt, Timestamp updateAt, Boolean delFlag, Long version) {
        super(EpSystemUserRole.EP_SYSTEM_USER_ROLE);

        set(0, id);
        set(1, userId);
        set(2, roleId);
        set(3, roleCode);
        set(4, createAt);
        set(5, updateAt);
        set(6, delFlag);
        set(7, version);
    }
}
