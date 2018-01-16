/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.tables.records.EpMemberChildSignRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 孩子签名表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpMemberChildSign extends TableImpl<EpMemberChildSignRecord> {

    private static final long serialVersionUID = 441367084;

    /**
     * The reference instance of <code>ep.ep_member_child_sign</code>
     */
    public static final EpMemberChildSign EP_MEMBER_CHILD_SIGN = new EpMemberChildSign();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpMemberChildSignRecord> getRecordType() {
        return EpMemberChildSignRecord.class;
    }

    /**
     * The column <code>ep.ep_member_child_sign.id</code>. 主键
     */
    public final TableField<EpMemberChildSignRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_member_child_sign.child_id</code>. 孩子id
     */
    public final TableField<EpMemberChildSignRecord, Long> CHILD_ID = createField("child_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "孩子id");

    /**
     * The column <code>ep.ep_member_child_sign.content</code>. 签名内容
     */
    public final TableField<EpMemberChildSignRecord, String> CONTENT = createField("content", org.jooq.impl.SQLDataType.VARCHAR.length(3000).nullable(false), this, "签名内容");

    /**
     * The column <code>ep.ep_member_child_sign.create_at</code>. 创建时间
     */
    public final TableField<EpMemberChildSignRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_member_child_sign.update_at</code>. 更新时间
     */
    public final TableField<EpMemberChildSignRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_member_child_sign.remark</code>. 备注
     */
    public final TableField<EpMemberChildSignRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");

    /**
     * The column <code>ep.ep_member_child_sign.del_flag</code>. 删除标记
     */
    public final TableField<EpMemberChildSignRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标记");

    /**
     * The column <code>ep.ep_member_child_sign.version</code>.
     */
    public final TableField<EpMemberChildSignRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_member_child_sign</code> table reference
     */
    public EpMemberChildSign() {
        this("ep_member_child_sign", null);
    }

    /**
     * Create an aliased <code>ep.ep_member_child_sign</code> table reference
     */
    public EpMemberChildSign(String alias) {
        this(alias, EP_MEMBER_CHILD_SIGN);
    }

    private EpMemberChildSign(String alias, Table<EpMemberChildSignRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpMemberChildSign(String alias, Table<EpMemberChildSignRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "孩子签名表");
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
    public Identity<EpMemberChildSignRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_MEMBER_CHILD_SIGN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpMemberChildSignRecord> getPrimaryKey() {
        return Keys.KEY_EP_MEMBER_CHILD_SIGN_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpMemberChildSignRecord>> getKeys() {
        return Arrays.<UniqueKey<EpMemberChildSignRecord>>asList(Keys.KEY_EP_MEMBER_CHILD_SIGN_PRIMARY, Keys.KEY_EP_MEMBER_CHILD_SIGN_UNIQUE_CHILD_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpMemberChildSignRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildSign as(String alias) {
        return new EpMemberChildSign(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpMemberChildSign rename(String name) {
        return new EpMemberChildSign(name, null);
    }
}
