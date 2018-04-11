package com.ep.domain.repository;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.enums.ChildClassStatusEnum;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.OrderChildStatisticsDto;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassScheduleStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import com.ep.domain.repository.domain.tables.records.EpOrderRecord;
import com.google.common.collect.Lists;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description:订单表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrderRepository extends AbstractCRUDRepository<EpOrderRecord, Long, EpOrderPo> {

    @Autowired
    public OrderRepository(DSLContext dslContext) {
        super(dslContext, EP_ORDER, EP_ORDER.ID, EpOrderPo.class);
    }

    /**
     * 根据孩子id和班次id查询订单
     *
     * @param childId
     * @param classId
     * @return
     */
    public List<EpOrderPo> findByChildIdAndClassId(Long childId, Long classId) {
        return dslContext.selectFrom(EP_ORDER)
                         .where(EP_ORDER.CHILD_ID.eq(childId))
                         .and(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.STATUS.in(EpOrderStatus.save, EpOrderStatus.success, EpOrderStatus.opening, EpOrderStatus.refuse))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .orderBy(EP_ORDER.ID.desc())
                         .fetchInto(EpOrderPo.class);
    }

    /**
     * 根据孩子查询成功的
     *
     * @param childId
     * @return
     */
    public List<EpOrderPo> findByChildId(Long childId) {
        return dslContext.selectFrom(EP_ORDER)
                .where(EP_ORDER.CHILD_ID.eq(childId))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .orderBy(EP_ORDER.ID.desc())
                .fetchInto(EpOrderPo.class);
    }

    /**
     * 分页查询孩子的订单和课程班次
     *
     * @param pageable
     * @param childId
     * @param statusEnum
     * @return
     */
    public Page<MemberChildClassBo> findChildClassPage(Pageable pageable, Long childId, ChildClassStatusEnum statusEnum) {
        // 封装条件
        Condition condition = EP_ORDER.CHILD_ID.eq(childId).and(EP_ORDER.DEL_FLAG.eq(false));
        if (statusEnum.equals(ChildClassStatusEnum.ENTERING)) {
            // 报名中课程班次
            condition = condition.and(EP_ORDER.STATUS.in(EpOrderStatus.save, EpOrderStatus.success));
        } else if (statusEnum.equals(ChildClassStatusEnum.OPENING)) {
            // 学习中
            condition = condition.and(EP_ORDER.STATUS.eq(EpOrderStatus.opening));
        } else if (statusEnum.equals(ChildClassStatusEnum.END)) {
            // 已结束
            condition = condition.and(EP_ORDER.STATUS.in(EpOrderStatus.end, EpOrderStatus.refuse, EpOrderStatus.cancel));
        }
        Long count = dslContext.selectCount()
                .from(EP_ORDER)
                .where(condition)
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORDER.fields());
        fieldList.add(EP_ORGAN.OGN_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_TYPE);
        fieldList.add(EP_ORGAN_CLASS.COURSE_NUM);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_ORGAN_CLASS_CHILD.HONOR_NUM);
        fieldList.add(EP_ORGAN_CLASS_CHILD.SCHEDULE_COMMENT_NUM);
        fieldList.add(EP_ORGAN_CLASS_CHILD.COURSE_COMMENT_FLAG);
        fieldList.add(EP_CONSTANT_CATALOG.LABEL);
        List<MemberChildClassBo> data = dslContext.select(fieldList)
                                                  .from(EP_ORDER)
                                                  .leftJoin(EP_ORGAN).on(EP_ORDER.OGN_ID.eq(EP_ORGAN.ID))
                                                  .leftJoin(EP_ORGAN_COURSE).on(EP_ORDER.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                                                  .leftJoin(EP_ORGAN_CLASS).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                                                  .leftJoin(EP_ORGAN_CLASS_CHILD).on(EP_ORDER.ID.eq(EP_ORGAN_CLASS_CHILD.ORDER_ID))
                                                  .leftJoin(EP_CONSTANT_CATALOG).on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                                                  .where(condition)
                                                  .orderBy(EP_ORDER.CREATE_AT.desc())
                                                  .limit(pageable.getPageSize())
                                                  .offset(pageable.getOffset())
                                                  .fetchInto(MemberChildClassBo.class);
        return new PageImpl(data, pageable, count);
    }

    /**
     * 分页查询孩子的行程
     *
     * @param pageable
     * @param memberId
     * @return
     */
    public Page<MemberChildScheduleBo> findChildSchedulePage(Pageable pageable, Long memberId) {
        // 封装条件
        Timestamp time = DateTools.zerolizedTime(DateTools.getCurrentDateTime());
        Condition condition = EP_ORDER.MEMBER_ID.eq(memberId)
                                                .and(EP_ORDER.STATUS.eq(EpOrderStatus.opening))
                                                .and(EP_ORDER.DEL_FLAG.eq(false))
                                                .and(EP_ORGAN_CLASS_SCHEDULE.START_TIME.greaterOrEqual(time))
                                                .and(EP_ORGAN_CLASS_SCHEDULE.STATUS.in(EpOrganClassScheduleStatus.wait,
                                                        EpOrganClassScheduleStatus.normal,
                                                        EpOrganClassScheduleStatus.late,
                                                        EpOrganClassScheduleStatus.absent))
                                                .and(EP_ORGAN_CLASS_SCHEDULE.DEL_FLAG.eq(false));
        Long count = dslContext.select(DSL.count(EP_ORDER.ID))
                               .from(EP_ORDER)
                               .leftJoin(EP_ORGAN_CLASS_SCHEDULE).on(EP_ORDER.ID.eq(EP_ORGAN_CLASS_SCHEDULE.ORDER_ID))
                               .where(condition)
                               .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORDER.fields());
        fieldList.add(EP_ORGAN.OGN_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_ORGAN_CLASS.COURSE_NUM);
        fieldList.add(EP_ORGAN_CLASS.PHONE);
        fieldList.add(EP_ORGAN_CLASS.ADDRESS);
        fieldList.add(EP_ORGAN_CLASS.ADDRESS_LAT);
        fieldList.add(EP_ORGAN_CLASS.ADDRESS_LNG);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.CATALOG_INDEX);
        fieldList.add(EP_ORGAN_CLASS_SCHEDULE.START_TIME);
        fieldList.add(EP_CONSTANT_CATALOG.LABEL);
        List<MemberChildScheduleBo> data = dslContext.select(fieldList)
                                                     .from(EP_ORDER)
                                                     .leftJoin(EP_ORGAN).on(EP_ORDER.OGN_ID.eq(EP_ORGAN.ID))
                                                     .leftJoin(EP_ORGAN_COURSE).on(EP_ORDER.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                                                     .leftJoin(EP_ORGAN_CLASS).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                                                     .leftJoin(EP_ORGAN_CLASS_SCHEDULE).on(EP_ORDER.ID.eq(EP_ORGAN_CLASS_SCHEDULE.ORDER_ID))
                                                     .leftJoin(EP_CONSTANT_CATALOG).on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                                                     .where(condition)
                                                     .orderBy(EP_ORGAN_CLASS_SCHEDULE.START_TIME.asc(), EP_ORDER.CHILD_ID.asc())
                                                     .limit(pageable.getPageSize())
                                                     .offset(pageable.getOffset())
                                                     .fetchInto(MemberChildScheduleBo.class);
        return new PageImpl(data, pageable, count);
    }

    /**
     * 获取家长的所有孩子和报名数据
     *
     * @param memberId
     * @param courseId
     * @return
     */
    public List<MemberCourseOrderInitBo> findChildrenAndOrders(Long memberId, Long courseId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_MEMBER_CHILD.ID.as("childId"));
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(DSL.groupConcat(EP_ORDER.CLASS_ID).as("joinedClasses"));
        return dslContext.select(fieldList)
                .from(EP_MEMBER_CHILD)
                .leftJoin(EP_ORDER)
                .on(EP_MEMBER_CHILD.ID.eq(EP_ORDER.CHILD_ID))
                .and(EP_ORDER.COURSE_ID.eq(courseId))
                .and(EP_ORDER.STATUS.notEqual(EpOrderStatus.cancel))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .where(EP_MEMBER_CHILD.MEMBER_ID.eq(memberId))
                .and(EP_MEMBER_CHILD.DEL_FLAG.eq(false))
                .groupBy(EP_MEMBER_CHILD.ID)
                .orderBy(EP_MEMBER_CHILD.SHOW_AT.desc())
                .fetchInto(MemberCourseOrderInitBo.class);
    }

    /**
     * 根据机构统计订单数
     *
     * @param ognId
     * @return
     */
    public Long countByOgnId(Long ognId) {
        return dslContext.selectCount()
                .from(EP_ORDER)
                .where(EP_ORDER.OGN_ID.eq(ognId))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }

    /**
     * 根据孩子统计订单数
     *
     * @param childId
     * @return
     */
    public Long countByChildId(Long childId) {
        return dslContext.selectCount()
                .from(EP_ORDER)
                .where(EP_ORDER.CHILD_ID.eq(childId))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }

    /**
     * 商户后台获取分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrderBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_ORDER)
                .leftJoin(EP_MEMBER).on(EP_MEMBER.ID.eq(EP_ORDER.MEMBER_ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORDER.CHILD_ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_ORDER.COURSE_ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS.ID.eq(EP_ORDER.CLASS_ID))
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORDER.fields());
        fieldList.add(EP_MEMBER.MOBILE);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);
        fieldList.add(EP_ORGAN_CLASS.COURSE_NUM);
        fieldList.add(EP_ORGAN_CLASS.TYPE.as("classType"));
        fieldList.add(EP_ORGAN_CLASS.STATUS.as("classStatus"));

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORDER)
                .leftJoin(EP_MEMBER).on(EP_MEMBER.ID.eq(EP_ORDER.MEMBER_ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORDER.CHILD_ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_ORDER.COURSE_ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS.ID.eq(EP_ORDER.CLASS_ID))
                .where(condition);

        List<OrderBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrderBo.class);
        PageImpl<OrderBo> pPage = new PageImpl<OrderBo>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 订单报名成功
     *
     * @param id
     */
    public int orderSuccessById(Long id) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.success)
                .set(EP_ORDER.REMARK, DSL.castNull(EP_ORDER.REMARK))
                .where(EP_ORDER.STATUS.eq(EpOrderStatus.save))
                .and(EP_ORDER.ID.eq(id))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 批量订单报名成功
     *
     * @param ids
     */
    public int orderSuccessByIds(List<Long> ids) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.success)
                .set(EP_ORDER.REMARK, "")
                .where(EP_ORDER.STATUS.eq(EpOrderStatus.save))
                .and(EP_ORDER.ID.in(ids))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 订单拒绝
     *
     * @param id
     */
    public int orderRefuseById(Long id, String remark) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.refuse)
                .set(EP_ORDER.REMARK, remark)
                .where(EP_ORDER.STATUS.eq(EpOrderStatus.save))
                .and(EP_ORDER.ID.eq(id))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id获取EpOrderPo
     *
     * @param id
     * @return
     */
    public Optional<EpOrderPo> findById(Long id) {
        EpOrderPo data = dslContext.selectFrom(EP_ORDER)
                .where(EP_ORDER.ID.eq(id))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrderPo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 根据id取消报名成功/拒绝的订单
     *
     * @param id
     */
    public int orderCancelById(Long id, EpOrderStatus status) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.save)
                .where(EP_ORDER.STATUS.eq(status))
                .and(EP_ORDER.ID.eq(id))
                .and(EP_ORDER.STATUS.eq(EpOrderStatus.success).or(EP_ORDER.STATUS.eq(EpOrderStatus.refuse)))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据班次id更新订单状态为已开班
     *
     * @param classId
     */
    public int openByClassId(Long classId) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.opening)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.STATUS.eq(EpOrderStatus.success))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据班次id更新订单状态为已结束
     *
     * @param classId
     */
    public int endByClassId(Long classId) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.end)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.STATUS.eq(EpOrderStatus.opening))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 获取已经开班的订单
     *
     * @param classId
     * @return
     */
    public List<EpOrderPo> findSuccessOrdersByClassId(Long classId) {
        return dslContext.selectFrom(EP_ORDER)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.STATUS.eq(EpOrderStatus.success))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchInto(EpOrderPo.class);
    }

    /**
     * 根据班次获取未确认的订单
     *
     * @param classId
     * @return
     */
    public List<EpOrderPo> findSavedOrdersByClassId(Long classId) {
        return dslContext.selectFrom(EP_ORDER)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.STATUS.eq(EpOrderStatus.save))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchInto(EpOrderPo.class);
    }

    /**
     * 根据班次和订单状态获取数据
     *
     * @param classId
     * @return
     */
    public List<EpOrderPo> findByClassIdAndOrderStatus(Long classId, EpOrderStatus... orderStatuses) {
        return dslContext.selectFrom(EP_ORDER)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.STATUS.in(orderStatuses))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchInto(EpOrderPo.class);
    }

    /**
     * 获取该班次下订单详情
     *
     * @param ognId
     * @param classId
     * @return
     */
    public List<OrderBo> findOrdersByClassId(Long ognId, Long classId) {
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_ORDER.ID);
        fieldList.add(EP_ORDER.CHILD_ID);
        fieldList.add(EP_ORDER.STATUS);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        return dslContext.select(fieldList).from(EP_ORDER)
                         .leftJoin(EP_MEMBER_CHILD)
                         .on(EP_ORDER.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                         .where(EP_ORDER.OGN_ID.eq(ognId))
                         .and(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .fetchInto(OrderBo.class);
    }

    /**
     * 根据id 提交预约
     *
     * @param id
     * @return
     */
    public int orderBespeakById(Long id) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.opening)
                .where(EP_ORDER.STATUS.eq(EpOrderStatus.success))
                .and(EP_ORDER.ID.eq(id))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id获取不同类型订单Bo
     *
     * @param id
     * @return
     */
    public Optional<OrderTypeBo> getOrderTypeBoById(Long id) {
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_ORDER.ID);
        fieldList.add(EP_ORDER.CLASS_ID);
        fieldList.add(EP_ORDER.CHILD_ID);
        fieldList.add(EP_ORGAN_CLASS.TYPE);
        fieldList.add(EP_ORDER.STATUS);
        OrderTypeBo data = dslContext.select(fieldList)
                .from(EP_ORDER)
                .leftJoin(EP_ORGAN_CLASS)
                .on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .where(EP_ORDER.ID.eq(id))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchOneInto(OrderTypeBo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 根据id结束订单
     *
     * @param id
     * @return
     */
    public int endById(Long id, BigDecimal refundAmount) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.end)
                .set(EP_ORDER.REFUND_AMOUNT, refundAmount)
                .where(EP_ORDER.ID.eq(id))
                .and(EP_ORDER.STATUS.eq(EpOrderStatus.opening))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 商户后台获取订单学员统计
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrderChildStatisticsDto> statisticsChild(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.select(EP_ORDER.CHILD_ID.countDistinct())
                .from(EP_ORDER)
                .leftJoin(EP_MEMBER).on(EP_ORDER.MEMBER_ID.eq(EP_MEMBER.ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_ORDER.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .where(condition)
                .fetchOne(0, Long.class);

        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_ORDER.CHILD_ID);
        fieldList.add(EP_ORDER.MEMBER_ID);
        fieldList.add(EP_MEMBER.MOBILE);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        fieldList.add(DSL.countDistinct(EP_ORDER.CLASS_ID).filterWhere(EP_ORDER.STATUS.in(EpOrderStatus.opening, EpOrderStatus.end)).as("enteredClassNum"));
        fieldList.add(DSL.countDistinct(EP_ORDER.CLASS_ID)
                .filterWhere(EP_ORGAN_CLASS.TYPE.eq(EpOrganClassType.bespeak).and(EP_ORDER.STATUS.in(EpOrderStatus.opening, EpOrderStatus.end))
                ).as("enteredBespeakNum"));

        SelectHavingStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORDER)
                .leftJoin(EP_MEMBER).on(EP_ORDER.MEMBER_ID.eq(EP_MEMBER.ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_ORDER.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .where(condition)
                .groupBy(EP_ORDER.CHILD_ID, EP_ORDER.MEMBER_ID);

        List<OrderChildStatisticsDto> list = record.orderBy(EP_ORDER.CHILD_ID.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrderChildStatisticsDto.class);
        PageImpl<OrderChildStatisticsDto> pPage = new PageImpl<OrderChildStatisticsDto>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 孩子已参加的班次
     *
     * @param childId
     * @return
     */
    public List<OrganClassBo> findEnteredClassByChildId(Long ognId, Long childId, EpOrganClassType classType) {

        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS.fields());
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        if (null == classType) {
            return dslContext.select(fieldList)
                    .from(EP_ORDER)
                    .leftJoin(EP_ORGAN_CLASS).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                    .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_ORGAN_CLASS.COURSE_ID))
                    .where(EP_ORDER.CHILD_ID.eq(childId))
                    .and(EP_ORDER.OGN_ID.eq(ognId))
                    .and(EP_ORDER.STATUS.in(EpOrderStatus.opening, EpOrderStatus.end))
                    .and(EP_ORGAN_CLASS.ID.isNotNull())
                    .and(EP_ORGAN_COURSE.ID.isNotNull())
                    .and(EP_ORDER.DEL_FLAG.eq(false))
                    .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                    .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                    .fetchInto(OrganClassBo.class);
        } else {
            return dslContext.select(fieldList)
                    .from(EP_ORDER)
                    .leftJoin(EP_ORGAN_CLASS).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                    .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_ORGAN_CLASS.COURSE_ID))
                    .where(EP_ORDER.CHILD_ID.eq(childId))
                    .and(EP_ORDER.OGN_ID.eq(ognId))
                    .and(EP_ORDER.STATUS.in(EpOrderStatus.opening, EpOrderStatus.end))
                    .and(EP_ORGAN_CLASS.ID.isNotNull())
                    .and(EP_ORGAN_CLASS.TYPE.eq(classType))
                    .and(EP_ORGAN_COURSE.ID.isNotNull())
                    .and(EP_ORDER.DEL_FLAG.eq(false))
                    .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                    .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                    .fetchInto(OrganClassBo.class);
        }

    }

    /**
     * 根据班次id提前结束订单
     *
     * @param classId
     * @return
     */
    public int advancedEndByClassId(Long classId) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.end)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }
}

