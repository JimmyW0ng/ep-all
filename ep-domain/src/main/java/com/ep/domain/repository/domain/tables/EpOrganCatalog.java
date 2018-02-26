/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.tables.records.EpOrganCatalogRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 机构类目表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpOrganCatalog extends TableImpl<EpOrganCatalogRecord> {

    private static final long serialVersionUID = -1644902211;

    /**
     * The reference instance of <code>ep.ep_organ_catalog</code>
     */
    public static final EpOrganCatalog EP_ORGAN_CATALOG = new EpOrganCatalog();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpOrganCatalogRecord> getRecordType() {
        return EpOrganCatalogRecord.class;
    }

    /**
     * The column <code>ep.ep_organ_catalog.id</code>. 主键
     */
    public final TableField<EpOrganCatalogRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_organ_catalog.ogn_id</code>. 机构id
     */
    public final TableField<EpOrganCatalogRecord, Long> OGN_ID = createField("ogn_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "机构id");

    /**
     * The column <code>ep.ep_organ_catalog.course_catalog_id</code>. 课程类目id
     */
    public final TableField<EpOrganCatalogRecord, Long> COURSE_CATALOG_ID = createField("course_catalog_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "课程类目id");

    /**
     * The column <code>ep.ep_organ_catalog.create_at</code>. 创建时间
     */
    public final TableField<EpOrganCatalogRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_organ_catalog.update_at</code>. 更新时间
     */
    public final TableField<EpOrganCatalogRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_organ_catalog.remark</code>. 备注
     */
    public final TableField<EpOrganCatalogRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");

    /**
     * The column <code>ep.ep_organ_catalog.del_flag</code>. 删除标记
     */
    public final TableField<EpOrganCatalogRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标记");

    /**
     * The column <code>ep.ep_organ_catalog.version</code>.
     */
    public final TableField<EpOrganCatalogRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_organ_catalog</code> table reference
     */
    public EpOrganCatalog() {
        this("ep_organ_catalog", null);
    }

    /**
     * Create an aliased <code>ep.ep_organ_catalog</code> table reference
     */
    public EpOrganCatalog(String alias) {
        this(alias, EP_ORGAN_CATALOG);
    }

    private EpOrganCatalog(String alias, Table<EpOrganCatalogRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpOrganCatalog(String alias, Table<EpOrganCatalogRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "机构类目表");
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
    public Identity<EpOrganCatalogRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_ORGAN_CATALOG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpOrganCatalogRecord> getPrimaryKey() {
        return Keys.KEY_EP_ORGAN_CATALOG_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpOrganCatalogRecord>> getKeys() {
        return Arrays.<UniqueKey<EpOrganCatalogRecord>>asList(Keys.KEY_EP_ORGAN_CATALOG_PRIMARY, Keys.KEY_EP_ORGAN_CATALOG_UNIQUE_OGN_ID_COURSE_CATALOG_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpOrganCatalogRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCatalog as(String alias) {
        return new EpOrganCatalog(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpOrganCatalog rename(String name) {
        return new EpOrganCatalog(name, null);
    }
}
