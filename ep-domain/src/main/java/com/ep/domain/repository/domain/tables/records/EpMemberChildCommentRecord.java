/*
 * This file is generated by jOOQ.
*/
package com.ep.domain.repository.domain.tables.records;


import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.tables.EpMemberChildComment;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record16;
import org.jooq.Row16;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;


/**
 * 孩子上课评论表
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.6"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class EpMemberChildCommentRecord extends UpdatableRecordImpl<EpMemberChildCommentRecord> implements Record16<Long, Long, Long, Long, Long, Long, Long, EpMemberChildCommentType, String, Long, Long, Timestamp, Timestamp, String, Boolean, Long> {

    private static final long serialVersionUID = 1545392794;

    /**
     * Setter for <code>ep.ep_member_child_comment.id</code>. 主键
     */
    public EpMemberChildCommentRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Create a detached EpMemberChildCommentRecord
     */
    public EpMemberChildCommentRecord() {
        super(EpMemberChildComment.EP_MEMBER_CHILD_COMMENT);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.p_id</code>. 父级id（发起评论没有父级id，回复评论存放被回复的评论记录id）
     */
    public EpMemberChildCommentRecord setPId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.p_id</code>. 父级id（发起评论没有父级id，回复评论存放被回复的评论记录id）
     */
    public Long getPId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.child_id</code>. 孩子id
     */
    public EpMemberChildCommentRecord setChildId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Create a detached, initialised EpMemberChildCommentRecord
     */
    public EpMemberChildCommentRecord(Long id, Long pId, Long childId, Long ognId, Long courseId, Long classId, Long classCatelogId, EpMemberChildCommentType type, String content, Long launchMemberId, Long replyMemberId, Timestamp createAt, Timestamp updateAt, String remark, Boolean delFlag, Long version) {
        super(EpMemberChildComment.EP_MEMBER_CHILD_COMMENT);

        set(0, id);
        set(1, pId);
        set(2, childId);
        set(3, ognId);
        set(4, courseId);
        set(5, classId);
        set(6, classCatelogId);
        set(7, type);
        set(8, content);
        set(9, launchMemberId);
        set(10, replyMemberId);
        set(11, createAt);
        set(12, updateAt);
        set(13, remark);
        set(14, delFlag);
        set(15, version);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.ogn_id</code>. 机构id
     */
    public EpMemberChildCommentRecord setOgnId(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.id</code>. 主键
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.course_id</code>. 课程id
     */
    public EpMemberChildCommentRecord setCourseId(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.child_id</code>. 孩子id
     */
    public Long getChildId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.class_id</code>. 班次id
     */
    public EpMemberChildCommentRecord setClassId(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.ogn_id</code>. 机构id
     */
    public Long getOgnId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.class_catelog_id</code>. 课程内容目录id
     */
    public EpMemberChildCommentRecord setClassCatelogId(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.course_id</code>. 课程id
     */
    public Long getCourseId() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.type</code>. 评论类型: 评论；回复
     */
    public EpMemberChildCommentRecord setType(EpMemberChildCommentType value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.class_id</code>. 班次id
     */
    public Long getClassId() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.content</code>. 评论内容
     */
    public EpMemberChildCommentRecord setContent(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.class_catelog_id</code>. 课程内容目录id
     */
    public Long getClassCatelogId() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.launch_member_id</code>. 发起评论者id
     */
    public EpMemberChildCommentRecord setLaunchMemberId(Long value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.type</code>. 评论类型: 评论；回复
     */
    public EpMemberChildCommentType getType() {
        return (EpMemberChildCommentType) get(7);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.reply_member_id</code>. 回复评论者id
     */
    public EpMemberChildCommentRecord setReplyMemberId(Long value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.content</code>. 评论内容
     */
    public String getContent() {
        return (String) get(8);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.create_at</code>. 创建时间
     */
    public EpMemberChildCommentRecord setCreateAt(Timestamp value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.launch_member_id</code>. 发起评论者id
     */
    public Long getLaunchMemberId() {
        return (Long) get(9);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.update_at</code>. 更新时间
     */
    public EpMemberChildCommentRecord setUpdateAt(Timestamp value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.reply_member_id</code>. 回复评论者id
     */
    public Long getReplyMemberId() {
        return (Long) get(10);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.remark</code>. 备注
     */
    public EpMemberChildCommentRecord setRemark(String value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.create_at</code>. 创建时间
     */
    public Timestamp getCreateAt() {
        return (Timestamp) get(11);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.del_flag</code>. 删除标记
     */
    public EpMemberChildCommentRecord setDelFlag(Boolean value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.update_at</code>. 更新时间
     */
    public Timestamp getUpdateAt() {
        return (Timestamp) get(12);
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.remark</code>. 备注
     */
    public String getRemark() {
        return (String) get(13);
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.del_flag</code>. 删除标记
     */
    public Boolean getDelFlag() {
        return (Boolean) get(14);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record16 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row16<Long, Long, Long, Long, Long, Long, Long, EpMemberChildCommentType, String, Long, Long, Timestamp, Timestamp, String, Boolean, Long> fieldsRow() {
        return (Row16) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row16<Long, Long, Long, Long, Long, Long, Long, EpMemberChildCommentType, String, Long, Long, Timestamp, Timestamp, String, Boolean, Long> valuesRow() {
        return (Row16) super.valuesRow();
    }

    /**
     * Getter for <code>ep.ep_member_child_comment.version</code>.
     */
    public Long getVersion() {
        return (Long) get(15);
    }

    /**
     * Setter for <code>ep.ep_member_child_comment.version</code>.
     */
    public EpMemberChildCommentRecord setVersion(Long value) {
        set(15, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.CHILD_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.OGN_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.COURSE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.CLASS_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field7() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.CLASS_CATELOG_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<EpMemberChildCommentType> field8() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.CONTENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field10() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.LAUNCH_MEMBER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field11() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.REPLY_MEMBER_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field12() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.CREATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field13() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.UPDATE_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field14() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.REMARK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field15() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.DEL_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field16() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return EpMemberChildComment.EP_MEMBER_CHILD_COMMENT.P_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getChildId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getOgnId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getCourseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getClassId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value7() {
        return getClassCatelogId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentType value8() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value10() {
        return getLaunchMemberId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value11() {
        return getReplyMemberId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value12() {
        return getCreateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value13() {
        return getUpdateAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value14() {
        return getRemark();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getPId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value2(Long value) {
        setPId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value3(Long value) {
        setChildId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value4(Long value) {
        setOgnId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value5(Long value) {
        setCourseId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value6(Long value) {
        setClassId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value7(Long value) {
        setClassCatelogId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value8(EpMemberChildCommentType value) {
        setType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value9(String value) {
        setContent(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value10(Long value) {
        setLaunchMemberId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value11(Long value) {
        setReplyMemberId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value12(Timestamp value) {
        setCreateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value13(Timestamp value) {
        setUpdateAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value14(String value) {
        setRemark(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value15(Boolean value) {
        setDelFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord value16(Long value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EpMemberChildCommentRecord values(Long value1, Long value2, Long value3, Long value4, Long value5, Long value6, Long value7, EpMemberChildCommentType value8, String value9, Long value10, Long value11, Timestamp value12, Timestamp value13, String value14, Boolean value15, Long value16) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value15() {
        return getDelFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value16() {
        return getVersion();
    }
}
