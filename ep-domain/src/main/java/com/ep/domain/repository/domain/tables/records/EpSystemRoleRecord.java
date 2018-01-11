/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import com.ep.domain.repository.domain.tables.EpSystemRole;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Row11;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 角色表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpSystemRoleRecord extends UpdatableRecordImpl<EpSystemRoleRecord> implements Record11<Long, EpSystemRoleTarget, String, String, Long, Timestamp, Long, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 1774264823;

    /**
     * Setter for <code>ep.ep_system_role.id</code>. 主键
     */
    public EpSystemRoleRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpSystemRoleRecord
     */
    public EpSystemRoleRecord() {
        super(EpSystemRole.EP_SYSTEM_ROLE);
    }

    /**
     * Setter for <code>ep.ep_system_role.target</code>. 目标：前台，商户后台，系统后台
     */
    public EpSystemRoleRecord setTarget(EpSystemRoleTarget value) {
        set(1, value);
        return this;
    }

    /**
     * Create a detached, initialised EpSystemRoleRecord
     */
    public EpSystemRoleRecord(Long id, EpSystemRoleTarget target, String roleName, String roleCode, Long createBy, Timestamp createAt, Long updateBy, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpSystemRole.EP_SYSTEM_ROLE);

        set(0, id);
        set(1, target);
        set(2, roleName);
        set(3, roleCode);
        set(4, createBy);
        set(5, createAt);
        set(6, updateBy);
        set(7, updateAt);
        set(8, remark);
        set(9, delFlag);
        set(10, version);
    }

    /**
     * Setter for <code>ep.ep_system_role.role_name</code>. 角色名称
     */
    public EpSystemRoleRecord setRoleName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_role.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_system_role.role_code</code>. 角色标识
     */
    public EpSystemRoleRecord setRoleCode(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_role.target</code>. 目标：前台，商户后台，系统后台
     */
    public EpSystemRoleTarget getTarget() {
        return (EpSystemRoleTarget) get(1);
    }

    /**
     * Setter for <code>ep.ep_system_role.create_by</code>. 创建者
     */
    public EpSystemRoleRecord setCreateBy(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_role.role_name</code>. 角色名称
     */
    public String getRoleName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>ep.ep_system_role.create_at</code>. 创建时间
     */
    public EpSystemRoleRecord setCreateAt(Timestamp value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_role.role_code</code>. 角色标识
     */
    public String getRoleCode() {
        return (String) get(3);
    }

    /**
     * Setter for <code>ep.ep_system_role.update_by</code>. 更新者
     */
    public EpSystemRoleRecord setUpdateBy(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_role.create_by</code>. 创建者
     */
    public Long getCreateBy() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_system_role.update_at</code>. 更新时间
     */
    public EpSystemRoleRecord setUpdateAt(Timestamp value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_role.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>ep.ep_system_role.remark</code>. 备注信息
     */
    public EpSystemRoleRecord setRemark(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_system_role.update_by</code>. 更新者
     */
    public Long getUpdateBy() {
        return (Long) get(6);
    }

    /**
     * Getter for <code>ep.ep_system_role.del_flag</code>. 删除标志
     */
    public Boolean getDelFlag() {
        return (Boolean) get(9);
    }

    /**
     * Getter for <code>ep.ep_system_role.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(7);
    }

    /**
     * Getter for <code>ep.ep_system_role.remark</code>. 备注信息
     */
    public String getRemark() {
        return (String) get(8);
    }

    /**
     * Getter for <code>ep.ep_system_role.version</code>.
     */
    public Long getVersion() {
        return (Long) get(10);
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
    // Record11 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row11<Long, EpSystemRoleTarget, String, String, Long, Timestamp, Long, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row11<Long, EpSystemRoleTarget, String, String, Long, Timestamp, Long, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row11) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpSystemRole.EP_SYSTEM_ROLE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpSystemRoleTarget> field2() {
        return EpSystemRole.EP_SYSTEM_ROLE.TARGET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return EpSystemRole.EP_SYSTEM_ROLE.ROLE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return EpSystemRole.EP_SYSTEM_ROLE.ROLE_CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpSystemRole.EP_SYSTEM_ROLE.CREATE_BY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field6() {
        return EpSystemRole.EP_SYSTEM_ROLE.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field7() {
        return EpSystemRole.EP_SYSTEM_ROLE.UPDATE_BY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field8() {
        return EpSystemRole.EP_SYSTEM_ROLE.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return EpSystemRole.EP_SYSTEM_ROLE.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field10() {
        return EpSystemRole.EP_SYSTEM_ROLE.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field11() {
        return EpSystemRole.EP_SYSTEM_ROLE.VERSION;
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
    public EpSystemRoleTarget value2() {
        return getTarget();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getRoleName();
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
    public Long value5() {
        return getCreateBy();
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
    public Long value7() {
        return getUpdateBy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value8() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value10() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value11() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value2(EpSystemRoleTarget value) {
        setTarget(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value3(String value) {
        setRoleName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value4(String value) {
        setRoleCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value5(Long value) {
        setCreateBy(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value6(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value7(Long value) {
        setUpdateBy(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value8(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value9(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value10(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord value11(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRoleRecord values(Long value1, EpSystemRoleTarget value2, String value3, String value4, Long value5, Timestamp value6, Long value7, Timestamp value8, String value9, Boolean value10, Long value11) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>ep.ep_system_role.del_flag</code>. 删除标志
     */
    public EpSystemRoleRecord setDelFlag(Boolean value) {
        set(9, value);
        return this;
    }

    /**
     * Setter for <code>ep.ep_system_role.version</code>.
     */
    public EpSystemRoleRecord setVersion(Long value) {
        set(10, value);
        return this;
    }
}
