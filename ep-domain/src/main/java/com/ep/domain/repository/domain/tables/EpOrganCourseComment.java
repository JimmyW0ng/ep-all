/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables;


import com.ep.domain.repository.domain.Ep;
import com.ep.domain.repository.domain.Keys;
import com.ep.domain.repository.domain.tables.records.EpOrganCourseCommentRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * 机构课程评分表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpOrganCourseComment extends TableImpl<EpOrganCourseCommentRecord> {

    private static final long serialVersionUID = -1682395281;

    /**
     * The reference instance of <code>ep.ep_organ_course_comment</code>
     */
    public static final EpOrganCourseComment EP_ORGAN_COURSE_COMMENT = new EpOrganCourseComment();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpOrganCourseCommentRecord> getRecordType() {
        return EpOrganCourseCommentRecord.class;
    }

    /**
     * The column <code>ep.ep_organ_course_comment.id</code>. 主键
     */
    public final TableField<EpOrganCourseCommentRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "主键");

    /**
     * The column <code>ep.ep_organ_course_comment.ogn_id</code>. 机构id
     */
    public final TableField<EpOrganCourseCommentRecord, Long> OGN_ID = createField("ogn_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "机构id");

    /**
     * The column <code>ep.ep_organ_course_comment.course_id</code>. 课程id
     */
    public final TableField<EpOrganCourseCommentRecord, Long> COURSE_ID = createField("course_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "课程id");

    /**
     * The column <code>ep.ep_organ_course_comment.score</code>. 评分（五分制）
     */
    public final TableField<EpOrganCourseCommentRecord, Byte> SCORE = createField("score", org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "评分（五分制）");

    /**
     * The column <code>ep.ep_organ_course_comment.member_id</code>. 评论者id
     */
    public final TableField<EpOrganCourseCommentRecord, Long> MEMBER_ID = createField("member_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "评论者id");

    /**
     * The column <code>ep.ep_organ_course_comment.content</code>. 评论类型: 评论；回复
     */
    public final TableField<EpOrganCourseCommentRecord, String> CONTENT = createField("content", org.jooq.impl.SQLDataType.CLOB, this, "评论类型: 评论；回复");

    /**
     * The column <code>ep.ep_organ_course_comment.order_id</code>. 关联订单id
     */
    public final TableField<EpOrganCourseCommentRecord, Long> ORDER_ID = createField("order_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "关联订单id");

    /**
     * The column <code>ep.ep_organ_course_comment.create_at</code>. 创建时间
     */
    public final TableField<EpOrganCourseCommentRecord, Timestamp> CREATE_AT = createField("create_at", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "创建时间");

    /**
     * The column <code>ep.ep_organ_course_comment.update_at</code>. 更新时间
     */
    public final TableField<EpOrganCourseCommentRecord, Timestamp> UPDATE_AT = createField("update_at", org.jooq.impl.SQLDataType.TIMESTAMP, this, "更新时间");

    /**
     * The column <code>ep.ep_organ_course_comment.remark</code>. 备注
     */
    public final TableField<EpOrganCourseCommentRecord, String> REMARK = createField("remark", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "备注");

    /**
     * The column <code>ep.ep_organ_course_comment.del_flag</code>. 删除标记
     */
    public final TableField<EpOrganCourseCommentRecord, Boolean> DEL_FLAG = createField("del_flag", org.jooq.impl.SQLDataType.BIT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("b'0'", org.jooq.impl.SQLDataType.BIT)), this, "删除标记");

    /**
     * The column <code>ep.ep_organ_course_comment.version</code>.
     */
    public final TableField<EpOrganCourseCommentRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * Create a <code>ep.ep_organ_course_comment</code> table reference
     */
    public EpOrganCourseComment() {
        this("ep_organ_course_comment", null);
    }

    /**
     * Create an aliased <code>ep.ep_organ_course_comment</code> table reference
     */
    public EpOrganCourseComment(String alias) {
        this(alias, EP_ORGAN_COURSE_COMMENT);
    }

    private EpOrganCourseComment(String alias, Table<EpOrganCourseCommentRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpOrganCourseComment(String alias, Table<EpOrganCourseCommentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "机构课程评分表");
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
    public Identity<EpOrganCourseCommentRecord, Long> getIdentity() {
        return Keys.IDENTITY_EP_ORGAN_COURSE_COMMENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<EpOrganCourseCommentRecord> getPrimaryKey() {
        return Keys.KEY_EP_ORGAN_COURSE_COMMENT_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EpOrganCourseCommentRecord>> getKeys() {
        return Arrays.<UniqueKey<EpOrganCourseCommentRecord>>asList(Keys.KEY_EP_ORGAN_COURSE_COMMENT_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableField<EpOrganCourseCommentRecord, Long> getRecordVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpOrganCourseComment as(String alias) {
        return new EpOrganCourseComment(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpOrganCourseComment rename(String name) {
        return new EpOrganCourseComment(name, null);
    }
}
