/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.enums.EpMemberSex;
import com.ep.domain.repository.domain.enums.EpMemberStatus;
import com.ep.domain.repository.domain.tables.records.EpMemberRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 会员信息表
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EpMember extends TableImpl<EpMemberRecord> {

    /**
     * The reference instance of <code>ep.ep_member</code>
     */
    public static final EpMember EP_MEMBER = new EpMember();
    private static final long serialVersionUID = 803922272;
    /**
     * The column <code>ep.ep_member.id</code>. 主键
     */
    public final TableField<EpMemberRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");
    /**
     * The column <code>ep.ep_member.mobile</code>. 手机号
     */
    public final TableField<EpMemberRecord, Long> MOBILE = createField("mobile", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "手机号");
    /**
     * The column <code>ep.ep_member.nick_name</code>. 昵称
     */
    public final TableField<EpMemberRecord, String> NICK_NAME = createField("nick_name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "昵称");
    /**
     * The column <code>ep.ep_member.true_name</code>. 真实姓名
     */
    public final TableField<EpMemberRecord, String> TRUE_NAME = createField("true_name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "真实姓名");
    /**
     * The column <code>ep.ep_member.sex</code>. 性别
     */
    public final TableField<EpMemberRecord, EpMemberSex> SEX = createField("sex", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(com.ep.domain.repository.domain.enums.EpMemberSex.class), this, "性别");
    /**
     * The column <code>ep.ep_member.email</code>. 邮箱
     */
    public final TableField<EpMemberRecord, String> EMAIL = createField("email", org.jooq.impl.SQLDataType.VARCHAR.length(64), this, "邮箱");
    /**
     * The column <code>ep.ep_member.status</code>. 状态：正常；已冻结；已注销；
     */
    public final TableField<EpMemberRecord, EpMemberStatus> STATUS = createField("status", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(com.ep.domain.repository.domain.enums.EpMemberStatus.class), this, "状态：正常；已冻结；已注销；");
    /**
     * The column <code>ep.ep_member.create_at</code>. 创建时间
     */
    public final TableField<EpMemberRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");
    /**
     * The column <code>ep.ep_member.update_at</code>. 更新时间
     */
    public final TableField<EpMemberRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");
    /**
     * The column <code>ep.ep_member.remark</code>. 备注
     */
    public final TableField<EpMemberRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");
    /**
     * The column <code>ep.ep_member.del_flag</code>. 删除标志
     */
    public final TableField<EpMemberRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标志");
    /**
     * The column <code>ep.ep_member.version</code>.
     */
    public final TableField<EpMemberRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_member</code> table reference
     */
    public EpMember() {
        this("ep_member", null);
    }

    /**
     * Create an aliased <code>ep.ep_member</code> table reference
     */
    public EpMember(String alias) {
        this(alias, EP_MEMBER);
    }

    private EpMember(String alias, Table<EpMemberRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpMember(String alias, Table<EpMemberRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "会员信息表");
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpMemberRecord> getRecordType() {
        return EpMemberRecord.class;
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
    public Identity<EpMemberRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_MEMBER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpMemberRecord> getPrimaryKey() {
        return Keys.KEY_EP_MEMBER_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpMemberRecord>> getKeys() {
        return Arrays.<UniqueKey<EpMemberRecord>>asList(Keys.KEY_EP_MEMBER_PRIMARY, Keys.KEY_EP_MEMBER_UNIQUE_MOBILE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpMemberRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMember as(String alias) {
        return new EpMember(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpMember rename(String name) {
        return new EpMember(name, null);
    }
}
