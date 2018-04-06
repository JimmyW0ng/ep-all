/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.tables.records.EpOrganConfigRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 机构配置表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganConfig extends TableImpl<EpOrganConfigRecord> {

    private static final long serialVersionUID = -286903838;

    /**
     * The reference instance of <code>ep.ep_organ_config</code>
     */
    public static final EpOrganConfig EP_ORGAN_CONFIG = new EpOrganConfig();
    /**
     * The column <code>ep.ep_organ_config.private_flag</code>. 机构资源是否私有
     */
    public final TableField<EpOrganConfigRecord, Boolean> PRIVATE_FLAG = createField("private_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "机构资源是否私有");

    /**
     * The column <code>ep.ep_organ_config.id</code>.
     */
    public final TableField<EpOrganConfigRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>ep.ep_organ_config.ogn_id</code>. 机构id
     */
    public final TableField<EpOrganConfigRecord, Long> OGN_ID = createField("ogn_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "机构id");

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpOrganConfigRecord> getRecordType() {
        return EpOrganConfigRecord.class;
    }

    /**
     * The column <code>ep.ep_organ_config.support_tag</code>. 是否支持标签功能
     */
    public final TableField<EpOrganConfigRecord, Boolean> SUPPORT_TAG = createField("support_tag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "是否支持标签功能");

    /**
     * The column <code>ep.ep_organ_config.create_at</code>. 创建时间
     */
    public final TableField<EpOrganConfigRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_organ_config.update_at</code>. 更新时间
     */
    public final TableField<EpOrganConfigRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_organ_config.remark</code>. 备注
     */
    public final TableField<EpOrganConfigRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");

    /**
     * The column <code>ep.ep_organ_config.del_flag</code>. 删除标记
     */
    public final TableField<EpOrganConfigRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标记");

    /**
     * The column <code>ep.ep_organ_config.version</code>.
     */
    public final TableField<EpOrganConfigRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_organ_config</code> table reference
     */
    public EpOrganConfig() {
        this("ep_organ_config", null);
    }

    /**
     * Create an aliased <code>ep.ep_organ_config</code> table reference
     */
    public EpOrganConfig(String alias) {
        this(alias, EP_ORGAN_CONFIG);
    }

    private EpOrganConfig(String alias, Table<EpOrganConfigRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpOrganConfig(String alias, Table<EpOrganConfigRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "机构配置表");
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
    public Identity<EpOrganConfigRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_ORGAN_CONFIG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpOrganConfigRecord> getPrimaryKey() {
        return Keys.KEY_EP_ORGAN_CONFIG_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpOrganConfigRecord>> getKeys() {
        return Arrays.<UniqueKey<EpOrganConfigRecord>>asList(Keys.KEY_EP_ORGAN_CONFIG_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpOrganConfigRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganConfig as(String alias) {
        return new EpOrganConfig(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpOrganConfig rename(String name) {
        return new EpOrganConfig(name, null);
    }
}
