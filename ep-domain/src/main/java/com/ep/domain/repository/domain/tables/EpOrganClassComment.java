/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.tables.records.EpOrganClassCommentRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 机构课程班次评分表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganClassComment extends TableImpl<EpOrganClassCommentRecord> {

    private static final long serialVersionUID = -1959366091;

    /**
     * The reference instance of <code>ep.ep_organ_class_comment</code>
     */
    public static final EpOrganClassComment EP_ORGAN_CLASS_COMMENT = new EpOrganClassComment();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpOrganClassCommentRecord> getRecordType() {
        return EpOrganClassCommentRecord.class;
    }

    /**
     * The column <code>ep.ep_organ_class_comment.id</code>. 主键
     */
    public final TableField<EpOrganClassCommentRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_organ_class_comment.ogn_id</code>. 机构id
     */
    public final TableField<EpOrganClassCommentRecord, Long> OGN_ID = createField("ogn_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "机构id");

    /**
     * The column <code>ep.ep_organ_class_comment.course_id</code>. 课程id
     */
    public final TableField<EpOrganClassCommentRecord, Long> COURSE_ID = createField("course_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "课程id");

    /**
     * The column <code>ep.ep_organ_class_comment.class_id</code>. 班次id
     */
    public final TableField<EpOrganClassCommentRecord, Long> CLASS_ID = createField("class_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "班次id");

    /**
     * The column <code>ep.ep_organ_class_comment.score</code>. 评分（五分制）
     */
    public final TableField<EpOrganClassCommentRecord, Byte> SCORE = createField("score", org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "评分（五分制）");

    /**
     * The column <code>ep.ep_organ_class_comment.child_id</code>. 评论者id
     */
    public final TableField<EpOrganClassCommentRecord, Long> CHILD_ID = createField("child_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "评论者id");

    /**
     * The column <code>ep.ep_organ_class_comment.content</code>. 评论类型: 评论；回复
     */
    public final TableField<EpOrganClassCommentRecord, String> CONTENT = createField("content", org.jooq.impl.SQLDataType.CLOB, this, "评论类型: 评论；回复");

    /**
     * The column <code>ep.ep_organ_class_comment.order_id</code>. 关联订单id
     */
    public final TableField<EpOrganClassCommentRecord, Long> ORDER_ID = createField("order_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "关联订单id");

    /**
     * The column <code>ep.ep_organ_class_comment.chosen_flag</code>. 精选标记
     */
    public final TableField<EpOrganClassCommentRecord, Boolean> CHOSEN_FLAG = createField("chosen_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "精选标记");

    /**
     * The column <code>ep.ep_organ_class_comment.create_at</code>. 创建时间
     */
    public final TableField<EpOrganClassCommentRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_organ_class_comment.update_at</code>. 更新时间
     */
    public final TableField<EpOrganClassCommentRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_organ_class_comment.remark</code>. 备注
     */
    public final TableField<EpOrganClassCommentRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");

    /**
     * The column <code>ep.ep_organ_class_comment.del_flag</code>. 删除标记
     */
    public final TableField<EpOrganClassCommentRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标记");

    /**
     * The column <code>ep.ep_organ_class_comment.version</code>.
     */
    public final TableField<EpOrganClassCommentRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_organ_class_comment</code> table reference
     */
    public EpOrganClassComment() {
        this("ep_organ_class_comment", null);
    }

    /**
     * Create an aliased <code>ep.ep_organ_class_comment</code> table reference
     */
    public EpOrganClassComment(String alias) {
        this(alias, EP_ORGAN_CLASS_COMMENT);
    }

    private EpOrganClassComment(String alias, Table<EpOrganClassCommentRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpOrganClassComment(String alias, Table<EpOrganClassCommentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "机构课程班次评分表");
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
    public Identity<EpOrganClassCommentRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_ORGAN_CLASS_COMMENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpOrganClassCommentRecord> getPrimaryKey() {
        return Keys.KEY_EP_ORGAN_CLASS_COMMENT_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpOrganClassCommentRecord>> getKeys() {
        return Arrays.<UniqueKey<EpOrganClassCommentRecord>>asList(Keys.KEY_EP_ORGAN_CLASS_COMMENT_PRIMARY, Keys.KEY_EP_ORGAN_CLASS_COMMENT_UNIQUE_ORDER_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpOrganClassCommentRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganClassComment as(String alias) {
        return new EpOrganClassComment(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpOrganClassComment rename(String name) {
        return new EpOrganClassComment(name, null);
    }
}
