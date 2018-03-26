/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.tables.records.EpSystemUserRoleRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


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
public class EpSystemUserRole extends TableImpl<EpSystemUserRoleRecord> {

    /**
     * The reference instance of <code>ep.ep_system_user_role</code>
     */
    public static final EpSystemUserRole EP_SYSTEM_USER_ROLE = new EpSystemUserRole();
    private static final long serialVersionUID = -1483760875;
    /**
     * The column <code>ep.ep_system_user_role.id</code>.
     */
    public final TableField<EpSystemUserRoleRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");
    /**
     * The column <code>ep.ep_system_user_role.user_id</code>. 用户id
     */
    public final TableField<EpSystemUserRoleRecord, Long> USER_ID = createField("user_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "用户id");
    /**
     * The column <code>ep.ep_system_user_role.role_id</code>. 角色id
     */
    public final TableField<EpSystemUserRoleRecord, Long> ROLE_ID = createField("role_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "角色id");
    /**
     * The column <code>ep.ep_system_user_role.create_at</code>. 创建时间
     */
    public final TableField<EpSystemUserRoleRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");
    /**
     * The column <code>ep.ep_system_user_role.update_at</code>. 更新时间
     */
    public final TableField<EpSystemUserRoleRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");
    /**
     * The column <code>ep.ep_system_user_role.del_flag</code>. 删除标志
     */
    public final TableField<EpSystemUserRoleRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标志");
    /**
     * The column <code>ep.ep_system_user_role.version</code>.
     */
    public final TableField<EpSystemUserRoleRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_system_user_role</code> table reference
     */
    public EpSystemUserRole() {
        this("ep_system_user_role", null);
    }

    /**
     * Create an aliased <code>ep.ep_system_user_role</code> table reference
     */
    public EpSystemUserRole(String alias) {
        this(alias, EP_SYSTEM_USER_ROLE);
    }

    private EpSystemUserRole(String alias, Table<EpSystemUserRoleRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpSystemUserRole(String alias, Table<EpSystemUserRoleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "用户-角色");
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpSystemUserRoleRecord> getRecordType() {
        return EpSystemUserRoleRecord.class;
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
    public Identity<EpSystemUserRoleRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_SYSTEM_USER_ROLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpSystemUserRoleRecord> getPrimaryKey() {
        return Keys.KEY_EP_SYSTEM_USER_ROLE_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpSystemUserRoleRecord>> getKeys() {
        return Arrays.<UniqueKey<EpSystemUserRoleRecord>>asList(Keys.KEY_EP_SYSTEM_USER_ROLE_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpSystemUserRoleRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpSystemUserRole as(String alias) {
        return new EpSystemUserRole(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpSystemUserRole rename(String name) {
        return new EpSystemUserRole(name, null);
    }
}
