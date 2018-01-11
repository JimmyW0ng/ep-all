/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import com.ep.domain.repository.domain.tables.records.EpSystemRoleRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


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
public class EpSystemRole extends TableImpl<EpSystemRoleRecord> {

    private static final long serialVersionUID = 146464595;

    /**
     * The reference instance of <code>ep.ep_system_role</code>
     */
    public static final EpSystemRole EP_SYSTEM_ROLE = new EpSystemRole();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpSystemRoleRecord> getRecordType() {
        return EpSystemRoleRecord.class;
    }

    /**
     * The column <code>ep.ep_system_role.id</code>. 主键
     */
    public final TableField<EpSystemRoleRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_system_role.target</code>. 目标：前台，商户后台，系统后台
     */
    public final TableField<EpSystemRoleRecord, EpSystemRoleTarget> TARGET = createField("target", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(com.ep.domain.repository.domain.enums.EpSystemRoleTarget.class), this, "目标：前台，商户后台，系统后台");

    /**
     * The column <code>ep.ep_system_role.role_name</code>. 角色名称
     */
    public final TableField<EpSystemRoleRecord, String> ROLE_NAME = createField("role_name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "角色名称");

    /**
     * The column <code>ep.ep_system_role.role_code</code>. 角色标识
     */
    public final TableField<EpSystemRoleRecord, String> ROLE_CODE = createField("role_code", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "角色标识");

    /**
     * The column <code>ep.ep_system_role.create_by</code>. 创建者
     */
    public final TableField<EpSystemRoleRecord, Long> CREATE_BY = createField("create_by", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "创建者");

    /**
     * The column <code>ep.ep_system_role.create_at</code>. 创建时间
     */
    public final TableField<EpSystemRoleRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_system_role.update_by</code>. 更新者
     */
    public final TableField<EpSystemRoleRecord, Long> UPDATE_BY = createField("update_by", org.jooq.impl.SQLDataType.BIGINT, this, "更新者");

    /**
     * The column <code>ep.ep_system_role.update_at</code>. 更新时间
     */
    public final TableField<EpSystemRoleRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_system_role.remark</code>. 备注信息
     */
    public final TableField<EpSystemRoleRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注信息");

    /**
     * The column <code>ep.ep_system_role.del_flag</code>. 删除标志
     */
    public final TableField<EpSystemRoleRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标志");

    /**
     * The column <code>ep.ep_system_role.version</code>.
     */
    public final TableField<EpSystemRoleRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_system_role</code> table reference
     */
    public EpSystemRole() {
        this("ep_system_role", null);
    }

    /**
     * Create an aliased <code>ep.ep_system_role</code> table reference
     */
    public EpSystemRole(String alias) {
        this(alias, EP_SYSTEM_ROLE);
    }

    private EpSystemRole(String alias, Table<EpSystemRoleRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpSystemRole(String alias, Table<EpSystemRoleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "角色表");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Ep.EP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<EpSystemRoleRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_SYSTEM_ROLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpSystemRoleRecord> getPrimaryKey() {
        return Keys.KEY_EP_SYSTEM_ROLE_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpSystemRoleRecord>> getKeys() {
        return Arrays.<UniqueKey<EpSystemRoleRecord>>asList(Keys.KEY_EP_SYSTEM_ROLE_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpSystemRoleRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemRole as(String alias) {
        return new EpSystemRole(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpSystemRole rename(String name) {
        return new EpSystemRole(name, null);
    }
}
