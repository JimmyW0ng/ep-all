/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.tables.records.EpOrganClassCatelogRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 班次课程内容目录表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganClassCatelog extends TableImpl<EpOrganClassCatelogRecord> {

    private static final long serialVersionUID = -1502677985;

    /**
     * The reference instance of <code>ep.ep_organ_class_catelog</code>
     */
    public static final EpOrganClassCatelog EP_ORGAN_CLASS_CATELOG = new EpOrganClassCatelog();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpOrganClassCatelogRecord> getRecordType() {
        return EpOrganClassCatelogRecord.class;
    }

    /**
     * The column <code>ep.ep_organ_class_catelog.id</code>. 主键
     */
    public final TableField<EpOrganClassCatelogRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_organ_class_catelog.class_id</code>. 班次id
     */
    public final TableField<EpOrganClassCatelogRecord, Long> CLASS_ID = createField("class_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "班次id");

    /**
     * The column <code>ep.ep_organ_class_catelog.catelog_title</code>. 目录标题
     */
    public final TableField<EpOrganClassCatelogRecord, String> CATELOG_TITLE = createField("catelog_title", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "目录标题");

    /**
     * The column <code>ep.ep_organ_class_catelog.catelog_desc</code>. 目录描述
     */
    public final TableField<EpOrganClassCatelogRecord, String> CATELOG_DESC = createField("catelog_desc", org.jooq.impl.SQLDataType.VARCHAR.length(3000), this, "目录描述");

    /**
     * The column <code>ep.ep_organ_class_catelog.catelog_index</code>. 目录索引（第几个课时）
     */
    public final TableField<EpOrganClassCatelogRecord, Integer> CATELOG_INDEX = createField("catelog_index", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "目录索引（第几个课时）");

    /**
     * The column <code>ep.ep_organ_class_catelog.start_time</code>. 开始时间
     */
    public final TableField<EpOrganClassCatelogRecord, Timestamp> START_TIME = createField("start_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "开始时间");

    /**
     * The column <code>ep.ep_organ_class_catelog.end_time</code>. 结束时间
     */
    public final TableField<EpOrganClassCatelogRecord, Timestamp> END_TIME = createField("end_time", org.jooq.impl.SQLDataType.TIMESTAMP, this, "结束时间");

    /**
     * The column <code>ep.ep_organ_class_catelog.remark</code>. 备注信息
     */
    public final TableField<EpOrganClassCatelogRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注信息");

    /**
     * The column <code>ep.ep_organ_class_catelog.create_at</code>. 创建时间
     */
    public final TableField<EpOrganClassCatelogRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_organ_class_catelog.update_at</code>. 更新时间
     */
    public final TableField<EpOrganClassCatelogRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_organ_class_catelog.del_flag</code>. 删除标志
     */
    public final TableField<EpOrganClassCatelogRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标志");

    /**
     * The column <code>ep.ep_organ_class_catelog.version</code>.
     */
    public final TableField<EpOrganClassCatelogRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_organ_class_catelog</code> table reference
     */
    public EpOrganClassCatelog() {
        this("ep_organ_class_catelog", null);
    }

    /**
     * Create an aliased <code>ep.ep_organ_class_catelog</code> table reference
     */
    public EpOrganClassCatelog(String alias) {
        this(alias, EP_ORGAN_CLASS_CATELOG);
    }

    private EpOrganClassCatelog(String alias, Table<EpOrganClassCatelogRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpOrganClassCatelog(String alias, Table<EpOrganClassCatelogRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "班次课程内容目录表");
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
    public Identity<EpOrganClassCatelogRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_ORGAN_CLASS_CATELOG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpOrganClassCatelogRecord> getPrimaryKey() {
        return Keys.KEY_EP_ORGAN_CLASS_CATELOG_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpOrganClassCatelogRecord>> getKeys() {
        return Arrays.<UniqueKey<EpOrganClassCatelogRecord>>asList(Keys.KEY_EP_ORGAN_CLASS_CATELOG_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpOrganClassCatelogRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassCatelog as(String alias) {
        return new EpOrganClassCatelog(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpOrganClassCatelog rename(String name) {
        return new EpOrganClassCatelog(name, null);
    }
}
