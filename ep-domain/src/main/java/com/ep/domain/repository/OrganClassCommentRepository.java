package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.pojo.po.EpOrganClassCommentPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassCommentRecord;
import com.google.common.collect.Lists;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description:机构课程评分表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganClassCommentRepository extends AbstractCRUDRepository<EpOrganClassCommentRecord, Long, EpOrganClassCommentPo> {

    @Autowired
    public OrganClassCommentRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_COMMENT, EP_ORGAN_CLASS_COMMENT.ID, EpOrganClassCommentPo.class);
    }

    public Optional<EpOrganClassCommentPo> findById(Long id) {
        EpOrganClassCommentPo data = dslContext.selectFrom(EP_ORGAN_CLASS_COMMENT)
                .where(EP_ORGAN_CLASS_COMMENT.ID.eq(id))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganClassCommentPo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 获取课程班次精选评论
     *
     * @param courseId
     * @return
     */
    public List<OrganClassCommentBo> getChosenByCourseId(Long courseId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_COMMENT.fields());
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_ORGAN_CLASS_CHILD.HONOR_NUM);
        return dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_COMMENT)
                .leftJoin(EP_MEMBER_CHILD)
                .on(EP_ORGAN_CLASS_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .leftJoin(EP_ORGAN_CLASS)
                .on(EP_ORGAN_CLASS_COMMENT.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .leftJoin(EP_ORGAN_CLASS_CHILD)
                .on(EP_ORGAN_CLASS_COMMENT.ORDER_ID.eq(EP_ORGAN_CLASS_CHILD.ORDER_ID))
                .where(EP_ORGAN_CLASS_COMMENT.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS_COMMENT.CHOSEN_FLAG.eq(true))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_CLASS_COMMENT.CREATE_AT.desc())
                .fetchInto(OrganClassCommentBo.class);
    }

    /**
     * 统计课程评价总数
     *
     * @param courseId
     * @return
     */
    public Long countByCourseId(Long courseId) {
        return dslContext.selectCount()
                .from(EP_ORGAN_CLASS_COMMENT)
                .where(EP_ORGAN_CLASS_COMMENT.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }

    /**
     * 分页查询课程全部评论
     *
     * @param pageable
     * @param courseId
     * @return
     */
    public Page<OrganClassCommentBo> findCourseCommentForPage(Pageable pageable, Long courseId) {
        long count = dslContext.selectCount().from(EP_ORGAN_CLASS_COMMENT)
                .where(EP_ORGAN_CLASS_COMMENT.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_COMMENT.fields());
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_ORGAN_CLASS_CHILD.HONOR_NUM);
        List<OrganClassCommentBo> pList = dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_COMMENT)
                .leftJoin(EP_MEMBER_CHILD)
                .on(EP_ORGAN_CLASS_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .leftJoin(EP_ORGAN_CLASS)
                .on(EP_ORGAN_CLASS_COMMENT.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .leftJoin(EP_ORGAN_CLASS_CHILD)
                .on(EP_ORGAN_CLASS_COMMENT.ORDER_ID.eq(EP_ORGAN_CLASS_CHILD.ORDER_ID))
                .where(EP_ORGAN_CLASS_COMMENT.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_CLASS_COMMENT.CREATE_AT.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganClassCommentBo.class);
        return new PageImpl<>(pList, pageable, count);
    }

    /**
     * 根据订单获取记录
     *
     * @param orderId
     * @return
     */
    public Optional<EpOrganClassCommentPo> getByOrderId(Long orderId) {
        EpOrganClassCommentPo commentPo = dslContext.selectFrom(EP_ORGAN_CLASS_COMMENT)
                .where(EP_ORGAN_CLASS_COMMENT.ORDER_ID.eq(orderId))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganClassCommentPo.class);
        return Optional.ofNullable(commentPo);
    }

    /**
     * 统计机构平均分（取整）
     *
     * @param ognId
     * @return
     */
    public Byte getAvgScoreByOgnId(Long ognId) {
        return dslContext.select(DSL.floor(DSL.sum(EP_ORGAN_CLASS_COMMENT.SCORE).divide(DSL.count(EP_ORGAN_CLASS_COMMENT.ID))))
                .from(EP_ORGAN_CLASS_COMMENT)
                .where(EP_ORGAN_CLASS_COMMENT.OGN_ID.eq(ognId))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .fetchOneInto(Byte.class);
    }

    /**
     * 后台机构课程班次评价分页列表
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganClassCommentBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_ORGAN_CLASS_COMMENT)
                .leftJoin(EP_ORGAN).on(EP_ORGAN_CLASS_COMMENT.OGN_ID.eq(EP_ORGAN.ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_CLASS_COMMENT.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS_COMMENT.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_ORGAN_CLASS_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_COMMENT.fields());
        fieldList.add(EP_ORGAN.OGN_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_COMMENT)
                .leftJoin(EP_ORGAN).on(EP_ORGAN_CLASS_COMMENT.OGN_ID.eq(EP_ORGAN.ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_CLASS_COMMENT.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS_COMMENT.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_ORGAN_CLASS_COMMENT.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .where(condition);

        List<OrganClassCommentBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganClassCommentBo.class);
        PageImpl<OrganClassCommentBo> pPage = new PageImpl<OrganClassCommentBo>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 班次评论设为精选
     *
     * @param id
     * @return
     */
    public int chosenById(Long id) {
        return dslContext.update(EP_ORGAN_CLASS_COMMENT)
                .set(EP_ORGAN_CLASS_COMMENT.CHOSEN_FLAG, true)
                .where(EP_ORGAN_CLASS_COMMENT.ID.eq(id))
                .and(EP_ORGAN_CLASS_COMMENT.CHOSEN_FLAG.eq(false))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 班次评论取消精选
     *
     * @param id
     * @return
     */
    public int unchosenById(Long id) {
        return dslContext.update(EP_ORGAN_CLASS_COMMENT)
                .set(EP_ORGAN_CLASS_COMMENT.CHOSEN_FLAG, false)
                .where(EP_ORGAN_CLASS_COMMENT.ID.eq(id))
                .and(EP_ORGAN_CLASS_COMMENT.CHOSEN_FLAG.eq(true))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据机构统计总评论数
     *
     * @param ognId
     * @return
     */
    public Long countByOgnId(Long ognId) {
        return dslContext.selectCount()
                .from(EP_ORGAN_CLASS_COMMENT)
                .where(EP_ORGAN_CLASS_COMMENT.OGN_ID.eq(ognId))
                .and(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }
}

