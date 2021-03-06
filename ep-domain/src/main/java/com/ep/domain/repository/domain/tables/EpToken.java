/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.enums.EpTokenType;
import com.ep.domain.repository.domain.tables.records.EpTokenRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * token表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpToken extends TableImpl<EpTokenRecord> {

    private static final long serialVersionUID = -77944272;

    /**
     * The reference instance of <code>ep.ep_token</code>
     */
    public static final EpToken EP_TOKEN = new EpToken();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpTokenRecord> getRecordType() {
        return EpTokenRecord.class;
    }

    /**
     * The column <code>ep.ep_token.id</code>. 主键
     */
    public final TableField<EpTokenRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_token.mobile</code>. 手机号
     */
    public final TableField<EpTokenRecord, Long> MOBILE = createField("mobile", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "手机号");

    /**
     * The column <code>ep.ep_token.type</code>. 类型：会员；机构账户
     */
    public final TableField<EpTokenRecord, EpTokenType> TYPE = createField("type", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(com.ep.domain.repository.domain.enums.EpTokenType.class), this, "类型：会员；机构账户");

    /**
     * The column <code>ep.ep_token.role</code>. 角色
     */
    public final TableField<EpTokenRecord, String> ROLE = createField("role", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "角色");

    /**
     * The column <code>ep.ep_token.last_access_ip</code>. 最近一次的访问ip
     */
    public final TableField<EpTokenRecord, String> LAST_ACCESS_IP = createField("last_access_ip", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "最近一次的访问ip");

    /**
     * The column <code>ep.ep_token.ogn_id</code>. 机构id
     */
    public final TableField<EpTokenRecord, Long> OGN_ID = createField("ogn_id", org.jooq.impl.SQLDataType.BIGINT, this, "机构id");

    /**
     * The column <code>ep.ep_token.expire_time</code>. 过期时间
     */
    public final TableField<EpTokenRecord, Timestamp> EXPIRE_TIME = createField("expire_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "过期时间");

    /**
     * The column <code>ep.ep_token.code</code>. token串
     */
    public final TableField<EpTokenRecord, String> CODE = createField("code", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "token串");

    /**
     * The column <code>ep.ep_token.create_at</code>. 创建时间
     */
    public final TableField<EpTokenRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_token.update_at</code>. 更新时间
     */
    public final TableField<EpTokenRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_token.remark</code>. 备注
     */
    public final TableField<EpTokenRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");

    /**
     * The column <code>ep.ep_token.del_flag</code>. 删除标记
     */
    public final TableField<EpTokenRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标记");

    /**
     * The column <code>ep.ep_token.version</code>.
     */
    public final TableField<EpTokenRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_token</code> table reference
     */
    public EpToken() {
        this("ep_token", null);
    }

    /**
     * Create an aliased <code>ep.ep_token</code> table reference
     */
    public EpToken(String alias) {
        this(alias, EP_TOKEN);
    }

    private EpToken(String alias, Table<EpTokenRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpToken(String alias, Table<EpTokenRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "token表");
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
    public Identity<EpTokenRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_TOKEN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpTokenRecord> getPrimaryKey() {
        return Keys.KEY_EP_TOKEN_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpTokenRecord>> getKeys() {
        return Arrays.<UniqueKey<EpTokenRecord>>asList(Keys.KEY_EP_TOKEN_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpTokenRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpToken as(String alias) {
        return new EpToken(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpToken rename(String name) {
        return new EpToken(name, null);
    }
}
