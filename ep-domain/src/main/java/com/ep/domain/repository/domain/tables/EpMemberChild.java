/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.enums.EpMemberChildChildSex;
import com.ep.domain.repository.domain.tables.records.EpMemberChildRecord;
import org.jooq.*;
import org.jooq.impl.DateAsTimestampBinding;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 孩子信息表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpMemberChild extends TableImpl<EpMemberChildRecord> {

    private static final long serialVersionUID = 1676651717;

    /**
     * The reference instance of <code>ep.ep_member_child</code>
     */
    public static final EpMemberChild EP_MEMBER_CHILD = new EpMemberChild();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpMemberChildRecord> getRecordType() {
        return EpMemberChildRecord.class;
    }

    /**
     * The column <code>ep.ep_member_child.id</code>. 主键
     */
    public final TableField<EpMemberChildRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_member_child.member_id</code>. 会员id
     */
    public final TableField<EpMemberChildRecord, Long> MEMBER_ID = createField("member_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "会员id");

    /**
     * The column <code>ep.ep_member_child.child_nick_name</code>. 昵称
     */
    public final TableField<EpMemberChildRecord, String> CHILD_NICK_NAME = createField("child_nick_name", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false).defaultValue(org.jooq.impl.DSL.inline("", org.jooq.impl.SQLDataType.VARCHAR)), this, "昵称");

    /**
     * The column <code>ep.ep_member_child.child_true_name</code>. 真实姓名
     */
    public final TableField<EpMemberChildRecord, String> CHILD_TRUE_NAME = createField("child_true_name", org.jooq.impl.SQLDataType.VARCHAR.length(64), this, "真实姓名");

    /**
     * The column <code>ep.ep_member_child.child_sex</code>. 性别
     */
    public final TableField<EpMemberChildRecord, EpMemberChildChildSex> CHILD_SEX = createField("child_sex", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(com.ep.domain.repository.domain.enums.EpMemberChildChildSex.class), this, "性别");

    /**
     * The column <code>ep.ep_member_child.child_birthday</code>. 生日
     */
    public final TableField<EpMemberChildRecord, Timestamp> CHILD_BIRTHDAY = createField("child_birthday", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "生日", new DateAsTimestampBinding());

    /**
     * The column <code>ep.ep_member_child.child_identity</code>. 身份证号
     */
    public final TableField<EpMemberChildRecord, String> CHILD_IDENTITY = createField("child_identity", org.jooq.impl.SQLDataType.VARCHAR.length(20), this, "身份证号");

    /**
     * The column <code>ep.ep_member_child.current_school</code>. 当前就读学校
     */
    public final TableField<EpMemberChildRecord, String> CURRENT_SCHOOL = createField("current_school", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "当前就读学校");

    /**
     * The column <code>ep.ep_member_child.current_class</code>. 所在班级
     */
    public final TableField<EpMemberChildRecord, String> CURRENT_CLASS = createField("current_class", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "所在班级");

    /**
     * The column <code>ep.ep_member_child.relationship</code>. 关系
     */
    public final TableField<EpMemberChildRecord, String> RELATIONSHIP = createField("relationship", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "关系");

    /**
     * The column <code>ep.ep_member_child.create_at</code>. 创建时间
     */
    public final TableField<EpMemberChildRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_member_child.update_at</code>. 更新时间
     */
    public final TableField<EpMemberChildRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_member_child.remark</code>. 备注
     */
    public final TableField<EpMemberChildRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");

    /**
     * The column <code>ep.ep_member_child.del_flag</code>. 删除标记
     */
    public final TableField<EpMemberChildRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标记");

    /**
     * The column <code>ep.ep_member_child.version</code>.
     */
    public final TableField<EpMemberChildRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_member_child</code> table reference
     */
    public EpMemberChild() {
        this("ep_member_child", null);
    }

    /**
     * Create an aliased <code>ep.ep_member_child</code> table reference
     */
    public EpMemberChild(String alias) {
        this(alias, EP_MEMBER_CHILD);
    }

    private EpMemberChild(String alias, Table<EpMemberChildRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpMemberChild(String alias, Table<EpMemberChildRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "孩子信息表");
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
    public Identity<EpMemberChildRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_MEMBER_CHILD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpMemberChildRecord> getPrimaryKey() {
        return Keys.KEY_EP_MEMBER_CHILD_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpMemberChildRecord>> getKeys() {
        return Arrays.<UniqueKey<EpMemberChildRecord>>asList(Keys.KEY_EP_MEMBER_CHILD_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpMemberChildRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChild as(String alias) {
        return new EpMemberChild(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpMemberChild rename(String name) {
        return new EpMemberChild(name, null);
    }
}
