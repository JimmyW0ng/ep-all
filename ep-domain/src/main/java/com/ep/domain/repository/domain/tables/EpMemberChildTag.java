/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.tables.records.EpMemberChildTagRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * 孩子标签记录表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpMemberChildTag extends TableImpl<EpMemberChildTagRecord> {

    private static final long serialVersionUID = -75521083;

    /**
     * The reference instance of <code>ep.ep_member_child_tag</code>
     */
    public static final EpMemberChildTag EP_MEMBER_CHILD_TAG = new EpMemberChildTag();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpMemberChildTagRecord> getRecordType() {
        return EpMemberChildTagRecord.class;
    }

    /**
     * The column <code>ep.ep_member_child_tag.id</code>. 主键
     */
    public final TableField<EpMemberChildTagRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_member_child_tag.child_id</code>. 孩子id
     */
    public final TableField<EpMemberChildTagRecord, Long> CHILD_ID = createField("child_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "孩子id");

    /**
     * The column <code>ep.ep_member_child_tag.class_schedule_id</code>. 行程_id
     */
    public final TableField<EpMemberChildTagRecord, Long> CLASS_SCHEDULE_ID = createField("class_schedule_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "行程_id");

    /**
     * The column <code>ep.ep_member_child_tag.tag_id</code>. 标签id
     */
    public final TableField<EpMemberChildTagRecord, Long> TAG_ID = createField("tag_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "标签id");

    /**
     * The column <code>ep.ep_member_child_tag.create_at</code>. 创建时间
     */
    public final TableField<EpMemberChildTagRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_member_child_tag.update_at</code>. 更新时间
     */
    public final TableField<EpMemberChildTagRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_member_child_tag.remark</code>. 备注
     */
    public final TableField<EpMemberChildTagRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");

    /**
     * The column <code>ep.ep_member_child_tag.del_flag</code>. 删除标记
     */
    public final TableField<EpMemberChildTagRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标记");

    /**
     * The column <code>ep.ep_member_child_tag.version</code>.
     */
    public final TableField<EpMemberChildTagRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_member_child_tag</code> table reference
     */
    public EpMemberChildTag() {
        this("ep_member_child_tag", null);
    }

    /**
     * Create an aliased <code>ep.ep_member_child_tag</code> table reference
     */
    public EpMemberChildTag(String alias) {
        this(alias, EP_MEMBER_CHILD_TAG);
    }

    private EpMemberChildTag(String alias, Table<EpMemberChildTagRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpMemberChildTag(String alias, Table<EpMemberChildTagRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "孩子标签记录表");
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
    public Identity<EpMemberChildTagRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_MEMBER_CHILD_TAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpMemberChildTagRecord> getPrimaryKey() {
        return Keys.KEY_EP_MEMBER_CHILD_TAG_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpMemberChildTagRecord>> getKeys() {
        return Arrays.<UniqueKey<EpMemberChildTagRecord>>asList(Keys.KEY_EP_MEMBER_CHILD_TAG_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpMemberChildTagRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildTag as(String alias) {
        return new EpMemberChildTag(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpMemberChildTag rename(String name) {
        return new EpMemberChildTag(name, null);
    }
}
