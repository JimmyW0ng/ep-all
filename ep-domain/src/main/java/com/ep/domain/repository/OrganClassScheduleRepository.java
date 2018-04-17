package com.ep.domain.repository;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.OrganClassScheduleDto;
import com.ep.domain.pojo.po.EpOrganClassSchedulePo;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.enums.EpOrganClassScheduleStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import com.ep.domain.repository.domain.tables.EpMemberChildComment;
import com.ep.domain.repository.domain.tables.records.EpOrganClassScheduleRecord;
import com.google.common.collect.Lists;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 班次行程表Repository
 * @Author: CC.F
 * @Date: 9:53 2018/2/12
 */
@Repository
public class OrganClassScheduleRepository extends AbstractCRUDRepository<EpOrganClassScheduleRecord, Long, EpOrganClassSchedulePo> {

    @Autowired
    public OrganClassScheduleRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_SCHEDULE, EP_ORGAN_CLASS_SCHEDULE.ID, EpOrganClassSchedulePo.class);
    }

    public Optional<EpOrganClassSchedulePo> findById(Long id) {
        EpOrganClassSchedulePo data = dslContext.selectFrom(EP_ORGAN_CLASS_SCHEDULE)
                .where(EP_ORGAN_CLASS_SCHEDULE.ID.eq(id))
                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganClassSchedulePo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 根据订单获取行程明细
     *
     * @param orderId
     * @return
     */
    public List<OrganClassCatalogBo> findDetailByOrderId(Long orderId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD_COMMENT.as("co").CONTENT.as("comment"));
        fieldList.add(EP_MEMBER_CHILD_COMMENT.as("co").ID.as("commentId"));
        fieldList.add(EP_MEMBER_CHILD_COMMENT.as("co").CREATE_AT.as("commentTime"));
        fieldList.add(EP_MEMBER_CHILD_COMMENT.as("re").CONTENT.as("replay"));
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_TITLE);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_DESC);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.START_TIME);
        return dslContext.select(fieldList)
                         .from(EP_ORGAN_CLASS_SCHEDULE)
                         .leftJoin(EP_MEMBER_CHILD_COMMENT.as("co"))
                         .on(EP_ORGAN_CLASS_SCHEDULE.ID.eq(EP_MEMBER_CHILD_COMMENT.as("co").CLASS_SCHEDULE_ID)
                                                       .and(EP_MEMBER_CHILD_COMMENT.as("co").TYPE.eq(EpMemberChildCommentType.launch))
                                                       .and(EP_MEMBER_CHILD_COMMENT.as("co").DEL_FLAG.eq(false)))
                         .leftJoin(EP_MEMBER_CHILD_COMMENT.as("re"))
                         .on(EP_ORGAN_CLASS_SCHEDULE.ID.eq(EP_MEMBER_CHILD_COMMENT.as("re").CLASS_SCHEDULE_ID)
                                                       .and(EP_MEMBER_CHILD_COMMENT.as("re").TYPE.eq(EpMemberChildCommentType.reply))
                                                       .and(EP_MEMBER_CHILD_COMMENT.as("re").DEL_FLAG.eq(false)))
                         .where(EP_ORGAN_CLASS_SCHEDULE.ORDER_ID.eq(orderId))
                         .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                         .orderBy(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX.asc())
                         .fetchInto(OrganClassCatalogBo.class);
    }

    /**
     * 商户后台获取随堂评价分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganClassScheduleDto> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        EpMemberChildComment memberChildCommentCopy = EP_MEMBER_CHILD_COMMENT.as("member_child_comment_copy");
        long totalCount = dslContext.selectCount()
                .from(EP_ORGAN_CLASS_SCHEDULE)
                .leftJoin(EP_MEMBER_CHILD_COMMENT)
                .on(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID)
                                                    .and(EP_MEMBER_CHILD_COMMENT.CLASS_SCHEDULE_ID.eq(EP_ORGAN_CLASS_SCHEDULE.ID)))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID))

                .leftJoin(memberChildCommentCopy).on(memberChildCommentCopy.P_ID.eq(EP_MEMBER_CHILD_COMMENT.ID))
                .where(condition)
                .fetchOne(0, Long.class);

        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.ORDER_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.START_TIME);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.STATUS);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CREATE_AT);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        fieldList.add(EP_MEMBER_CHILD_COMMENT.ID.as("launchId"));
        fieldList.add(EP_MEMBER_CHILD_COMMENT.TYPE);
        fieldList.add(EP_MEMBER_CHILD_COMMENT.CONTENT.as("contentLaunch"));

        fieldList.add(memberChildCommentCopy.ID.as("replyId"));
        fieldList.add(memberChildCommentCopy.CONTENT.as("contentReply"));
        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_SCHEDULE)
                .leftJoin(EP_MEMBER_CHILD_COMMENT)
                .on(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID)
                                                    .and(EP_MEMBER_CHILD_COMMENT.CLASS_SCHEDULE_ID.eq(EP_ORGAN_CLASS_SCHEDULE.ID)))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID))

                .leftJoin(memberChildCommentCopy).on(memberChildCommentCopy.P_ID.eq(EP_MEMBER_CHILD_COMMENT.ID))
                .where(condition);

        List<OrganClassScheduleDto> list = record.orderBy(EP_ORGAN_CLASS_SCHEDULE.ID.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganClassScheduleDto.class);
        PageImpl<OrganClassScheduleDto> pPage = new PageImpl<OrganClassScheduleDto>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 根据搜索条件导出excel
     *
     * @param fieldList
     * @param pageable
     * @param condition
     * @return
     */
    public List<ClassScheduleExcelBo> indexExportExcel(List<Field<?>> fieldList, Pageable pageable, Collection<? extends Condition> condition) {
        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_SCHEDULE)
                .leftJoin(EP_MEMBER_CHILD_COMMENT)
                .on(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID)
                        .and(EP_MEMBER_CHILD_COMMENT.CLASS_SCHEDULE_ID.eq(EP_ORGAN_CLASS_SCHEDULE.ID)))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID))
                .where(condition);
        List<ClassScheduleExcelBo> list = record.orderBy(EP_ORGAN_CLASS_SCHEDULE.ID.asc())
                .fetchInto(ClassScheduleExcelBo.class);
        return list;
    }

    /**
     * 根据课程负责人获取今日班次
     *
     * @param ognAccountId
     * @return
     */
    public List<OrganAccountClassBo> findClassByOgnAccountId(Long ognAccountId, Timestamp startTime, Timestamp endTime) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_SCHEDULE.START_TIME);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_ORGAN_CLASS.TYPE);
        fieldList.add(EP_ORGAN_CLASS.COURSE_ID);
        fieldList.add(EP_ORGAN_CLASS.COURSE_NUM);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(DSL.max(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX).as("catalogIndex"));
        fieldList.add(DSL.max(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID).as("classCatalogId"));
        fieldList.add(DSL.count(EP_ORGAN_CLASS_SCHEDULE.ID).as("childNum"));
        fieldList.add(DSL.sum(EP_ORGAN_CLASS_SCHEDULE.EVALUATE_FLAG.cast(Byte.class)).as("childEvaluatedNum"));
        return dslContext.select(fieldList)
                         .from(EP_ORGAN_CLASS_SCHEDULE)
                         .innerJoin(EP_ORGAN_CLASS)
                         .on(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                         .and(EP_ORGAN_CLASS.OGN_ACCOUNT_ID.eq(ognAccountId))
                         .and(EP_ORGAN_CLASS.STATUS.in(EpOrganClassStatus.online,
                                 EpOrganClassStatus.opening,
                                 EpOrganClassStatus.end))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .leftJoin(EP_ORGAN_COURSE)
                         .on(EP_ORGAN_CLASS.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                         .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                         .where(EP_ORGAN_CLASS_SCHEDULE.START_TIME.between(startTime, endTime))
                         .and(EP_ORGAN_CLASS_SCHEDULE.STATUS.in(EpOrganClassScheduleStatus.wait,
                                 EpOrganClassScheduleStatus.normal,
                                 EpOrganClassScheduleStatus.late,
                                 EpOrganClassScheduleStatus.absent))
                         .groupBy(EP_ORGAN_CLASS_SCHEDULE.START_TIME, EP_ORGAN_CLASS_SCHEDULE.CLASS_ID)
                         .orderBy(BizConstant.DB_NUM_ONE)
                         .fetchInto(OrganAccountClassBo.class);
    }

    /**
     * 根据班次和开始时间获取所有孩子评价信息
     *
     * @param classId
     * @param startTime
     * @return
     */
    public List<OrganClassCatalogCommentBo> findbyClassIdAndStartTime(Long classId, Timestamp startTime) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_SCHEDULE.ID.as("classScheduleId"));
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_TITLE);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_DESC);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.EVALUATE_FLAG);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_MEMBER_CHILD_COMMENT.CONTENT.as("comment"));
        return dslContext.select(fieldList)
                         .from(EP_ORGAN_CLASS_SCHEDULE)
                         .leftJoin(EP_MEMBER_CHILD)
                         .on(EP_MEMBER_CHILD.ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID))
                         .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                         .leftJoin(EP_MEMBER_CHILD_COMMENT)
                         .on(EP_MEMBER_CHILD_COMMENT.CLASS_SCHEDULE_ID.eq(EP_ORGAN_CLASS_SCHEDULE.ID))
                         .and(EP_MEMBER_CHILD_COMMENT.CHILD_ID.eq(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID))
                         .and(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch))
                         .and(EP_MEMBER_CHILD_COMMENT.DEL_FLAG.eq(false))
                         .where(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(classId))
                         .and(EP_ORGAN_CLASS_SCHEDULE.START_TIME.eq(startTime))
                         .and(EP_ORGAN_CLASS_SCHEDULE.STATUS.in(EpOrganClassScheduleStatus.wait,
                                 EpOrganClassScheduleStatus.normal,
                                 EpOrganClassScheduleStatus.late))
                         .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                         .orderBy(EP_ORGAN_CLASS_SCHEDULE.ORDER_ID)
                         .fetchInto(OrganClassCatalogCommentBo.class);
    }

    /**
     * 机构端-获取正常类型（固定课时）的班次全部课时
     *
     * @param classId
     * @return
     */
    public List<ClassNormalAllScheduleBo> findNomalClassScheduleByClassId(Long classId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.START_TIME);
        fieldList.add(DSL.max(EP_ORGAN_CLASS_SCHEDULE.CATALOG_TITLE).as("catalogTitle"));
        fieldList.add(DSL.max(EP_ORGAN_CLASS_SCHEDULE.CATALOG_DESC).as("catalogDesc"));
        fieldList.add(DSL.count(EP_ORGAN_CLASS_SCHEDULE.ID).as("childNum"));
        fieldList.add(DSL.sum(EP_ORGAN_CLASS_SCHEDULE.EVALUATE_FLAG.cast(Byte.class)).as("childEvaluatedNum"));
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_ORGAN_CLASS.TYPE);
        fieldList.add(EP_ORGAN_CLASS.COURSE_NUM);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(DSL.max(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID).as("classCatalogId"));
        fieldList.add(DSL.count(EP_ORGAN_CLASS_SCHEDULE.ID).as("childNum"));
        return dslContext.select(fieldList)
                         .from(EP_ORGAN_CLASS_SCHEDULE)
                         .innerJoin(EP_ORGAN_CLASS)
                         .on(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                         .and(EP_ORGAN_CLASS.TYPE.eq(EpOrganClassType.normal))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .leftJoin(EP_ORGAN_COURSE)
                         .on(EP_ORGAN_CLASS.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                         .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                         .where(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(classId))
                         .and(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID.isNotNull())
                         .groupBy(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID, EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX, EP_ORGAN_CLASS_SCHEDULE.START_TIME)
                         .orderBy(BizConstant.DB_NUM_ONE)
                         .fetchInto(ClassNormalAllScheduleBo.class);
    }

    /**
     * 机构端-获取预约类型的班次全部课时
     *
     * @param classId
     * @param childId
     * @return
     */
    public List<ClassChildBespeakBo> findBespeakClassScheduleByClassId(Long classId, Long childId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.ID.as("classScheduleId"));
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.START_TIME);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_TITLE);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_DESC);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.EVALUATE_FLAG);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_ORGAN_CLASS.COURSE_NUM);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        return dslContext.select(fieldList)
                         .from(EP_ORGAN_CLASS_SCHEDULE)
                         .innerJoin(EP_ORGAN_CLASS)
                         .on(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                         .and(EP_ORGAN_CLASS.TYPE.eq(EpOrganClassType.bespeak))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .leftJoin(EP_ORGAN_COURSE)
                         .on(EP_ORGAN_CLASS.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                         .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                         .where(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(classId))
                         .and(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID.eq(childId))
                         .and(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID.isNull())
                         .orderBy(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX)
                         .fetchInto(ClassChildBespeakBo.class);
    }

    /**
     * 根据id更新行程
     *
     * @param po
     * @return
     */
    public int updateClassSchedule(EpOrganClassSchedulePo po) {
        return dslContext.update(EP_ORGAN_CLASS_SCHEDULE)
                .set(EP_ORGAN_CLASS_SCHEDULE.CATALOG_TITLE, po.getCatalogTitle())
                .set(EP_ORGAN_CLASS_SCHEDULE.START_TIME, po.getStartTime())
                .set(EP_ORGAN_CLASS_SCHEDULE.DURATION, po.getDuration())
                .set(EP_ORGAN_CLASS_SCHEDULE.CATALOG_DESC, po.getCatalogDesc())
                .where(EP_ORGAN_CLASS_SCHEDULE.ID.eq(po.getId()))
                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据订单id获取订单行程Bo
     *
     * @param orderId
     * @return
     */
    public List<OrganClassScheduleBo> findBoByOrderId(Long orderId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS_SCHEDULE.fields());
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        return dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS_SCHEDULE)
                .leftJoin(EP_MEMBER_CHILD).on(EP_ORGAN_CLASS_SCHEDULE.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .where(EP_ORGAN_CLASS_SCHEDULE.ORDER_ID.eq(orderId))
                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX.asc())
                .fetchInto(OrganClassScheduleBo.class);
    }

    /**
     * 根据订单id获取订单行程修改Bo
     *
     * @param orderId
     * @return
     */
    public List<OrganClassBespeakScheduleBo> findBespeakScheduleBoByOrderId(Long orderId) {
        return dslContext.selectFrom(EP_ORGAN_CLASS_SCHEDULE)
                .where(EP_ORGAN_CLASS_SCHEDULE.ORDER_ID.eq(orderId))
                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX.asc())
                .fetchInto(OrganClassBespeakScheduleBo.class);
    }


    /**
     * 新增随堂评价标识
     *
     * @param classScheduleId
     * @return
     */
    public int evaluate(Long classScheduleId) {
        return dslContext.update(EP_ORGAN_CLASS_SCHEDULE)
                         .set(EP_ORGAN_CLASS_SCHEDULE.EVALUATE_FLAG, true)
                         .where(EP_ORGAN_CLASS_SCHEDULE.ID.eq(classScheduleId))
                         .and(EP_ORGAN_CLASS_SCHEDULE.EVALUATE_FLAG.eq(false))
                         .and(EP_ORGAN_CLASS_SCHEDULE.STATUS.in(EpOrganClassScheduleStatus.wait,
                                 EpOrganClassScheduleStatus.normal,
                                 EpOrganClassScheduleStatus.late))
                         .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 撤销随堂评价标识
     *
     * @param classScheduleId
     * @return
     */
    public int cancelEvaluate(Long classScheduleId) {
        return dslContext.update(EP_ORGAN_CLASS_SCHEDULE)
                         .set(EP_ORGAN_CLASS_SCHEDULE.EVALUATE_FLAG, false)
                         .where(EP_ORGAN_CLASS_SCHEDULE.ID.eq(classScheduleId))
                         .and(EP_ORGAN_CLASS_SCHEDULE.EVALUATE_FLAG.eq(true))
                         .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 根据订单结束关闭预约行程
     *
     * @param orderId
     * @return
     */
    public int closeByOrderEnd(Long orderId) {
        return dslContext.update(EP_ORGAN_CLASS_SCHEDULE)
                .set(EP_ORGAN_CLASS_SCHEDULE.STATUS, EpOrganClassScheduleStatus.close)
                .where(EP_ORGAN_CLASS_SCHEDULE.ORDER_ID.eq(orderId))
                .and(EP_ORGAN_CLASS_SCHEDULE.STATUS.eq(EpOrganClassScheduleStatus.normal))
                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 紧急修改课程时修改班次目录
     *
     * @param bo
     * @return
     */
    public int rectifyByRectifyCatalog(RectifyOrganClassCatalogBo bo) {
        return dslContext.update(EP_ORGAN_CLASS_SCHEDULE)
                .set(EP_ORGAN_CLASS_SCHEDULE.CATALOG_TITLE, bo.getCatalogTitle())
                .set(EP_ORGAN_CLASS_SCHEDULE.CATALOG_DESC, bo.getCatalogDesc())
                .set(EP_ORGAN_CLASS_SCHEDULE.START_TIME, bo.getStartTime())
                .set(EP_ORGAN_CLASS_SCHEDULE.DURATION, bo.getDuration())
                .where(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID.eq(bo.getId()))
                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据班次id提前结束行程
     *
     * @param classId
     * @return
     */
    public int endByClassId(Long classId) {
        return dslContext.update(EP_ORGAN_CLASS_SCHEDULE)
                         .set(EP_ORGAN_CLASS_SCHEDULE.STATUS, EpOrganClassScheduleStatus.close)
                         .where(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(classId))
                         .and(EP_ORGAN_CLASS_SCHEDULE.START_TIME.greaterThan(DateTools.getCurrentDateTime()))
                         .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 根据id变更考勤
     *
     * @param id
     * @param status
     * @return
     */
    public int updateClassScheduleStatusById(Long id, EpOrganClassScheduleStatus status) {
        return dslContext.update(EP_ORGAN_CLASS_SCHEDULE)
                .set(EP_ORGAN_CLASS_SCHEDULE.STATUS, status)
                .where(EP_ORGAN_CLASS_SCHEDULE.ID.eq(id))
                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据classId统计预约班次下未结束的预约
     *
     * @param classId
     * @return
     */
    public int countUnendBesprakClassScheduleByClassId(Long classId) {
        return dslContext.selectCount().from(EP_ORGAN_CLASS_SCHEDULE)
                .where(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_SCHEDULE.START_TIME.greaterThan(DSL.currentTimestamp()))
                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                .fetchOneInto(Integer.class);
    }

    /**
     * 根据订单id和目录id，结束该订单下行程从选择目录开始后的所有行程
     *
     * @param orderId
     * @param firstClassCatalogId
     * @return
     */
    public int closeByOrderIdAndFirstClassCatalogId(Long orderId, Long firstClassCatalogId) {
        return dslContext.update(EP_ORGAN_CLASS_SCHEDULE)
                .set(EP_ORGAN_CLASS_SCHEDULE.STATUS, EpOrganClassScheduleStatus.close)
                .where(EP_ORGAN_CLASS_SCHEDULE.ORDER_ID.eq(orderId))
                .and(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID.greaterOrEqual(firstClassCatalogId))
                .and(EP_ORGAN_CLASS_SCHEDULE.START_TIME.greaterThan(DSL.currentTimestamp()))
                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false))
                .execute();
    }
}
