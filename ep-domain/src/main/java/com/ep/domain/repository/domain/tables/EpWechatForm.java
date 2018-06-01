/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.enums.EpWechatFormBizType;
import com.ep.domain.repository.domain.tables.records.EpWechatFormRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 微信form提交关联表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpWechatForm extends TableImpl<EpWechatFormRecord> {

    private static final long serialVersionUID = -287871341;

    /**
     * The reference instance of <code>ep.ep_wechat_form</code>
     */
    public static final EpWechatForm EP_WECHAT_FORM = new EpWechatForm();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpWechatFormRecord> getRecordType() {
        return EpWechatFormRecord.class;
    }

    /**
     * The column <code>ep.ep_wechat_form.id</code>.
     */
    public final TableField<EpWechatFormRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>ep.ep_wechat_form.form_id</code>. 小程序formid
     */
    public final TableField<EpWechatFormRecord, String> FORM_ID = createField("form_id", org.jooq.impl.SQLDataType.VARCHAR.length(50).nullable(false), this, "小程序formid");

    /**
     * The column <code>ep.ep_wechat_form.touser</code>. 微信openid
     */
    public final TableField<EpWechatFormRecord, String> TOUSER = createField("touser", org.jooq.impl.SQLDataType.VARCHAR.length(50).nullable(false), this, "微信openid");

    /**
     * The column <code>ep.ep_wechat_form.source_id</code>. 业务id
     */
    public final TableField<EpWechatFormRecord, Long> SOURCE_ID = createField("source_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "业务id");

    /**
     * The column <code>ep.ep_wechat_form.biz_type</code>. 订单;
     */
    public final TableField<EpWechatFormRecord, EpWechatFormBizType> BIZ_TYPE = createField("biz_type", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(com.ep.domain.repository.domain.enums.EpWechatFormBizType.class), this, "订单;");

    /**
     * The column <code>ep.ep_wechat_form.expire_time</code>. 过期时间
     */
    public final TableField<EpWechatFormRecord, Timestamp> EXPIRE_TIME = createField("expire_time", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "过期时间");

    /**
     * The column <code>ep.ep_wechat_form.create_at</code>. 创建时间
     */
    public final TableField<EpWechatFormRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_wechat_form.update_at</code>. 更新时间
     */
    public final TableField<EpWechatFormRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_wechat_form.remark</code>. 备注
     */
    public final TableField<EpWechatFormRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");

    /**
     * The column <code>ep.ep_wechat_form.del_flag</code>. 删除标记
     */
    public final TableField<EpWechatFormRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标记");

    /**
     * The column <code>ep.ep_wechat_form.version</code>. 版本号
     */
    public final TableField<EpWechatFormRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "版本号");

    /**
     * Create a <code>ep.ep_wechat_form</code> table reference
     */
    public EpWechatForm() {
        this("ep_wechat_form", null);
    }

    /**
     * Create an aliased <code>ep.ep_wechat_form</code> table reference
     */
    public EpWechatForm(String alias) {
        this(alias, EP_WECHAT_FORM);
    }

    private EpWechatForm(String alias, Table<EpWechatFormRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpWechatForm(String alias, Table<EpWechatFormRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "微信form提交关联表");
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
    public Identity<EpWechatFormRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_WECHAT_FORM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpWechatFormRecord> getPrimaryKey() {
        return Keys.KEY_EP_WECHAT_FORM_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpWechatFormRecord>> getKeys() {
        return Arrays.<UniqueKey<EpWechatFormRecord>>asList(Keys.KEY_EP_WECHAT_FORM_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpWechatFormRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpWechatForm as(String alias) {
        return new EpWechatForm(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpWechatForm rename(String name) {
        return new EpWechatForm(name, null);
    }
}
