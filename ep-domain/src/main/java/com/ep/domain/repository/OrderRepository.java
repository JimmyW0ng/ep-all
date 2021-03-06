package com.ep.domain.repository;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.wechat.WechatTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.enums.ChildClassStatusEnum;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.OrderChildStatisticsDto;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpWechatPayBillDetailPo;
import com.ep.domain.repository.domain.enums.*;
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
                                                .and(EP_ORDER.STATUS.in(EpOrderStatus.opening, EpOrderStatus.end))
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
     * 商户后台订单获取分页
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
     * 商户后台获取预约管理分页
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrderBo> findOrderBespeakByPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_ORDER)
                .leftJoin(EP_MEMBER).on(EP_MEMBER.ID.eq(EP_ORDER.MEMBER_ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORDER.CHILD_ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_ORDER.COURSE_ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS.ID.eq(EP_ORDER.CLASS_ID))
                .leftJoin(EP_ORGAN_CLASS_CHILD).on(EP_ORGAN_CLASS_CHILD.ORDER_ID.eq(EP_ORDER.ID))
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
        fieldList.add(EP_ORGAN_CLASS_CHILD.BESPEAKED_SCHEDULE_NUM);

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORDER)
                .leftJoin(EP_MEMBER).on(EP_MEMBER.ID.eq(EP_ORDER.MEMBER_ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORDER.CHILD_ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_ORDER.COURSE_ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS.ID.eq(EP_ORDER.CLASS_ID))
                .leftJoin(EP_ORGAN_CLASS_CHILD).on(EP_ORGAN_CLASS_CHILD.ORDER_ID.eq(EP_ORDER.ID))
                .where(condition);

        List<OrderBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrderBo.class);
        PageImpl<OrderBo> pPage = new PageImpl<OrderBo>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 订单支付成功
     *
     * @param id
     */
    public int orderPaidById(Long id, Timestamp payConfirmTime) {
        return dslContext.update(EP_ORDER)
                         .set(EP_ORDER.PAY_TYPE, EpOrderPayType.wechat_pay)
                         .set(EP_ORDER.PAY_STATUS, EpOrderPayStatus.paid)
                         .set(EP_ORDER.PAY_CONFIRM_TIME, payConfirmTime)
                         .where(EP_ORDER.PAY_STATUS.in(EpOrderPayStatus.wait_pay, EpOrderPayStatus.paid))
                         .and(EP_ORDER.ID.eq(id))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .execute();
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
                         .and(EP_ORDER.PAY_STATUS.isNull().or(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid)))
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
     * 根据班次和订单状态统计订单数
     *
     * @param classId
     * @param orderStatuses
     * @return
     */
    public int countByClassIdAndOrderStatus(Long classId, EpOrderStatus... orderStatuses) {
        return dslContext.selectCount().from(EP_ORDER)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.STATUS.in(orderStatuses))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchOneInto(Integer.class);
    }

    /**
     * 根据班次和支付类型和支付状态统计订单数
     *
     * @param orderPayType
     * @param orderPayStatus
     * @return
     */
    public int countByClassIdAndPayTypeAndPayStatus(Long classId, EpOrderPayType orderPayType, EpOrderPayStatus orderPayStatus
            , Timestamp startTime, Timestamp endTime) {
        if (null == startTime) {
            return dslContext.selectCount().from(EP_ORDER)
                    .where(EP_ORDER.CLASS_ID.eq(classId))
                    .and(EP_ORDER.PAY_TYPE.eq(orderPayType))
                    .and(EP_ORDER.PAY_STATUS.eq(orderPayStatus))
                    .and(EP_ORDER.PAY_CONFIRM_TIME.lessThan(endTime))
                    .and(EP_ORDER.DEL_FLAG.eq(false))
                    .fetchOneInto(Integer.class);
        } else {
            return dslContext.selectCount().from(EP_ORDER)
                    .where(EP_ORDER.CLASS_ID.eq(classId))
                    .and(EP_ORDER.PAY_TYPE.eq(orderPayType))
                    .and(EP_ORDER.PAY_STATUS.eq(orderPayStatus))
                    .and(EP_ORDER.PAY_CONFIRM_TIME.greaterOrEqual(startTime))
                    .and(EP_ORDER.PAY_CONFIRM_TIME.lessThan(endTime))
                    .and(EP_ORDER.DEL_FLAG.eq(false))
                    .fetchOneInto(Integer.class);
        }

    }

    /**
     * 线下支付已支付订单数
     *
     * @param classId
     * @return
     */
    public long countOfflinePaidOrders(Long classId) {
        return dslContext.select(EP_ORDER.ID.count()).from(EP_ORDER)
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.offline))
                         .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .fetchOneInto(Long.class);
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
        List<OrderChildStatisticsDto> list = record.orderBy(EP_ORDER.CHILD_ID.desc())
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
     * 根据搜索条件导出excel
     *
     * @param pageable
     * @param condition
     * @return
     */
    public List<OrderExcelBo> indexExportExcel(List<Field<?>> fieldList, Pageable pageable, Collection<? extends Condition> condition) {
        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORDER)
                .leftJoin(EP_MEMBER).on(EP_MEMBER.ID.eq(EP_ORDER.MEMBER_ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_MEMBER_CHILD.ID.eq(EP_ORDER.CHILD_ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_ORDER.COURSE_ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS.ID.eq(EP_ORDER.CLASS_ID))
                .where(condition);
        List<OrderExcelBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .fetchInto(OrderExcelBo.class);
        return list;
    }

    /**
     * 批量拒绝
     *
     * @param ids
     * @param batchRefuseRemark
     * @param ognId
     * @return
     */
    public int batchRefuseByIds(List<Long> ids, String batchRefuseRemark, Long ognId) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.STATUS, EpOrderStatus.refuse)
                .set(EP_ORDER.REMARK, batchRefuseRemark)
                .where(EP_ORDER.STATUS.eq(EpOrderStatus.save))
                .and(EP_ORDER.ID.in(ids))
                .and(EP_ORDER.OGN_ID.eq(ognId))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 未处理订单数
     *
     * @param ognId
     * @return
     */
    public long countSaveOrder(Long ognId) {
        return dslContext.selectCount().from(EP_ORDER)
                         .where(EP_ORDER.OGN_ID.eq(ognId))
                .and(EP_ORDER.STATUS.eq(EpOrderStatus.save))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .fetchOneInto(Long.class);
    }

    /**
     * 统计最近几个月每月报名数
     * @param ognId
     * @param advanceNum
     * @return
     */
    public long countAdvanceMonthOrder(Long ognId, int advanceNum) {
        String sql = "SELECT count(*) FROM ep_order " +
                "WHERE ep_order.ogn_id=" + ognId +
                " and ep_order.del_flag=false " +
                " and PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format( ep_order.create_at, '%Y%m' ) ) =" + advanceNum;
        return dslContext.fetch(sql).getValues(0, Long.class).get(0);
    }

    /**
     * 统计最近几个月每月报名成功数
     *
     * @param ognId
     * @param advanceNum
     * @return
     */
    public long countAdvanceMonthSuccessOrder(Long ognId, int advanceNum) {
        String sql = "SELECT count(*) FROM ep_order " +
                "WHERE ep_order.ogn_id=" + ognId +
                " and ep_order.del_flag=false " +
                "and ep_order.status='success' " +
                "and PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format( ep_order.create_at, '%Y%m' ) ) =" + advanceNum;
        return dslContext.fetch(sql).getValues(0, Long.class).get(0);
    }

    /**
     * 统计机构所有订单数
     *
     * @param ognId
     * @return
     */
    public long countAllOrders(Long ognId) {
        return dslContext.selectCount().from(EP_ORDER)
                .where(EP_ORDER.OGN_ID.eq(ognId))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }

    /**
     * 统计机构最近几天内的订单数
     *
     * @param ognId
     * @param days
     * @return
     */
    public long countOrdersRecently(Long ognId, int days) {
        String sql = "select count(*) from ep_order where" +
                " ep_order.ogn_id=" + ognId +
                " and ep_order.del_flag=false " +
                " and DATE_SUB(CURDATE(),INTERVAL " +
                days + " DAY) <= DATE(ep_order.create_at)";
        return dslContext.fetch(sql).getValues(0, Long.class).get(0);
    }

    /**
     * 统计机构最近几天内的订单销售额
     *
     * @param ognId
     * @return
     */
    public BigDecimal sumOrderPrize(Long ognId, int days) {
        String sql = "select sum(ep_order.prize) from ep_order where" +
                " ep_order.ogn_id=" + ognId +
                " and ep_order.del_flag=false " +
                " and ep_order.status='success' " +
                " and DATE_SUB(CURDATE(),INTERVAL " +
                days + " DAY) <= DATE(ep_order.create_at)";
        return dslContext.fetch(sql).getValues(0, BigDecimal.class).get(0);
    }

    /**
     * 确认线下支付已完成（支付状态为wait_pay或refund_finish）
     *
     * @param orderId
     * @param payConfirmTime
     * @return
     */
    public int offlinePaidByOrderId(Long orderId, EpOrderPayStatus payStatus, Timestamp payConfirmTime) {
        if (payStatus.equals(EpOrderPayStatus.wait_pay)) {
            return dslContext.update(EP_ORDER)
                    .set(EP_ORDER.PAY_TYPE, EpOrderPayType.offline)
                    .set(EP_ORDER.PAY_STATUS, EpOrderPayStatus.paid)
                    .set(EP_ORDER.PAY_CONFIRM_TIME, payConfirmTime)
                    .where(EP_ORDER.ID.eq(orderId))
                    .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.wait_pay))
                    .and(EP_ORDER.DEL_FLAG.eq(false))
                    .execute();
        } else if (payStatus.equals(EpOrderPayStatus.refund_finish)) {
            return dslContext.update(EP_ORDER)
                    .set(EP_ORDER.PAY_TYPE, EpOrderPayType.offline)
                    .set(EP_ORDER.PAY_STATUS, EpOrderPayStatus.paid)
                    .set(EP_ORDER.PAY_CONFIRM_TIME, payConfirmTime)
                    .where(EP_ORDER.ID.eq(orderId))
                    .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.refund_finish))
                    .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                    .and(EP_ORDER.DEL_FLAG.eq(false))
                    .execute();
        } else {
            return BizConstant.DB_NUM_ZERO;
        }

    }



    /**
     * 支付退单成功
     *
     * @param orderId
     */
    public int orderRefundFinishedById(Long orderId) {
        return dslContext.update(EP_ORDER)
                         .set(EP_ORDER.PAY_STATUS, EpOrderPayStatus.refund_finish)
                         .where(EP_ORDER.ID.eq(orderId))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 统计班次内微信支付成功订单数
     *
     * @param classId
     * @return
     */
    public int countWechatPaidOrderByClassId(Long classId) {
        return dslContext.selectCount().from(EP_ORDER)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                .fetchOneInto(Integer.class);
    }

    /**
     *  统计班次微信支付未提现订单总数
     *
     * @param classId
     * @param endTime
     * @return
     */
    public int countWaitWithdrawOrderByClassId(Long classId, Timestamp endTime) {
        return dslContext.selectCount().from(EP_ORDER)
                         .innerJoin(EP_WECHAT_PAY_BILL_DETAIL)
                         .on(EP_ORDER.ID.eq(EP_WECHAT_PAY_BILL_DETAIL.ORDER_ID))
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and("unix_timestamp(`ep`.`ep_wechat_pay_bill_detail`.`transaction_time`)<unix_timestamp(" + "'" + endTime.toString() + "'" + ")")
                         .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq(WechatTools.TRADE_STATE_SUCCESS))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                         .fetchOneInto(Integer.class);
    }

    /**
     * 统计班次微信支付未提现订单总金额
     * @param classId
     * @param endTime
     * @return
     */
    public BigDecimal sumWaitWithdrawOrderByClassId(Long classId, Timestamp endTime) {
        return dslContext.select(DSL.ifnull(DSL.sum(EP_WECHAT_PAY_BILL_DETAIL.TOTAL_FEE), 0)).from(EP_ORDER)
                         .innerJoin(EP_WECHAT_PAY_BILL_DETAIL)
                         .on(EP_ORDER.ID.eq(EP_WECHAT_PAY_BILL_DETAIL.ORDER_ID))
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and("unix_timestamp(`ep`.`ep_wechat_pay_bill_detail`.`transaction_time`)<unix_timestamp(" + "'" + endTime.toString() + "'" + ")")
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq(WechatTools.TRADE_STATE_SUCCESS))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                         .fetchOneInto(BigDecimal.class);
    }

    /**
     * 统计班次微信支付未提现订单手续费
     * @param classId
     * @param endTime
     * @return
     */
    public BigDecimal sumWaitWithdrawPoundageByClassId(Long classId, Timestamp endTime) {
        return dslContext.select(DSL.ifnull(DSL.sum(EP_WECHAT_PAY_BILL_DETAIL.POUNDAGE), 0)).from(EP_ORDER)
                         .innerJoin(EP_WECHAT_PAY_BILL_DETAIL)
                         .on(EP_ORDER.ID.eq(EP_WECHAT_PAY_BILL_DETAIL.ORDER_ID))
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and("unix_timestamp(`ep`.`ep_wechat_pay_bill_detail`.`transaction_time`)<unix_timestamp(" + "'" + endTime.toString() + "'" + ")")
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq(WechatTools.TRADE_STATE_SUCCESS))
                         .fetchOneInto(BigDecimal.class);
    }

    /**
     * 统计班次微信支付订单总金额
     * @param classId
     * @param endTime
     * @return
     */
    public BigDecimal sumWechatPaidOrderTotalFeeByClassId(Long classId, Timestamp endTime) {
        return dslContext.select(DSL.ifnull(DSL.sum(EP_WECHAT_PAY_BILL_DETAIL.TOTAL_FEE), 0)).from(EP_ORDER)
                         .innerJoin(EP_WECHAT_PAY_BILL_DETAIL)
                         .on(EP_ORDER.ID.eq(EP_WECHAT_PAY_BILL_DETAIL.ORDER_ID))
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and("unix_timestamp(`ep`.`ep_wechat_pay_bill_detail`.`transaction_time`)<unix_timestamp(" + "'" + endTime.toString() + "'" + ")")
                         .and(EP_ORDER.PAY_STATUS.in(EpOrderPayStatus.paid, EpOrderPayStatus.refund_apply, EpOrderPayStatus.withdraw_apply, EpOrderPayStatus.withdraw_finish))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq(WechatTools.TRADE_STATE_SUCCESS))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                         .fetchOneInto(BigDecimal.class);
    }


    /**
     * 统计班次微信支付订单总手续费
     * @param classId
     * @param endTime
     * @return
     */
    public BigDecimal sumWechatPoundageByClassId(Long classId, Timestamp endTime) {
        return dslContext.select(DSL.ifnull(DSL.sum(EP_WECHAT_PAY_BILL_DETAIL.POUNDAGE), 0))
                         .from(EP_ORDER)
                         .innerJoin(EP_WECHAT_PAY_BILL_DETAIL)
                         .on(EP_ORDER.ID.eq(EP_WECHAT_PAY_BILL_DETAIL.ORDER_ID))
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.PAY_STATUS.in(EpOrderPayStatus.paid, EpOrderPayStatus.refund_apply, EpOrderPayStatus.withdraw_apply, EpOrderPayStatus.withdraw_finish))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and("unix_timestamp(`ep`.`ep_wechat_pay_bill_detail`.`transaction_time`)<unix_timestamp(" + "'" + endTime.toString() + "'" + ")")
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq(WechatTools.TRADE_STATE_SUCCESS))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                         .fetchOneInto(BigDecimal.class);
    }

    /**
     * 线下支付已支付订单金额
     * @param classId
     * @return
     */
    public BigDecimal sumOfflinePaidOrderTotalFee(Long classId) {
        return dslContext.select(DSL.ifnull(DSL.sum(EP_ORDER.PRIZE), 0))
                .from(EP_ORDER)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.offline))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchOneInto(BigDecimal.class);
    }

    /**
     * 根据订单id完成提现（更新提现标记）
     * @param orderIds
     * @return
     */
    public int finishPayWithdrawByOrderIds(Long classId, List<Long> orderIds) {
        return dslContext.update(EP_ORDER)
                         .set(EP_ORDER.PAY_STATUS, EpOrderPayStatus.withdraw_finish)
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.withdraw_apply))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .and(EP_ORDER.ID.in(orderIds))
                         .execute();
    }

    /**
     * 统计班次微信支付订单总数
     *
     * @param classId
     * @param endTime
     * @return
     */
    public long countWechatPayOrders(Long classId, Timestamp endTime) {
        return dslContext.select(EP_ORDER.ID.count()).from(EP_ORDER)
                         .innerJoin(EP_WECHAT_PAY_BILL_DETAIL).on(EP_ORDER.ID.eq(EP_WECHAT_PAY_BILL_DETAIL.ORDER_ID))
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and("unix_timestamp(`ep`.`ep_wechat_pay_bill_detail`.`transaction_time`)<unix_timestamp(" + "'" + endTime.toString() + "'" + ")")
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and(EP_ORDER.PAY_STATUS.in(EpOrderPayStatus.paid, EpOrderPayStatus.refund_apply, EpOrderPayStatus.withdraw_apply, EpOrderPayStatus.withdraw_finish))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq(WechatTools.TRADE_STATE_SUCCESS))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                         .fetchOneInto(Long.class);
    }

    /**
     * 查询微信支付的订单集合
     *
     * @param classId
     * @param endTime
     * @return
     */
    public List<EpOrderPo> findWechatPaidOrders(Long classId, Timestamp endTime) {
        return dslContext.select(EP_ORDER.fields()).from(EP_ORDER)
                         .innerJoin(EP_WECHAT_PAY_BILL_DETAIL).on(EP_ORDER.ID.eq(EP_WECHAT_PAY_BILL_DETAIL.ORDER_ID))
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and("unix_timestamp(`ep`.`ep_wechat_pay_bill_detail`.`transaction_time`)<unix_timestamp(" + "'" + endTime.toString() + "'" + ")")
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq(WechatTools.TRADE_STATE_SUCCESS))
                         .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                         .fetchInto(EpOrderPo.class);
    }

    /**
     * 查询微信支付的订单资金详情集合
     *
     * @param classId
     * @param endTime
     * @return
     */
    public List<EpWechatPayBillDetailPo> findWechatPaidOrderBillDetail(Long classId, Timestamp endTime) {
        return dslContext.select(EP_WECHAT_PAY_BILL_DETAIL.fields()).from(EP_WECHAT_PAY_BILL_DETAIL)
                .innerJoin(EP_ORDER).on(EP_ORDER.ID.eq(EP_WECHAT_PAY_BILL_DETAIL.ORDER_ID))
                .where(EP_WECHAT_PAY_BILL_DETAIL.CLASS_ID.eq(classId))
                .and("unix_timestamp(`ep`.`ep_wechat_pay_bill_detail`.`transaction_time`)<unix_timestamp(" + "'" + endTime.toString() + "'" + ")")
                .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq(WechatTools.TRADE_STATE_SUCCESS))
                .and(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false))
                .fetchInto(EpWechatPayBillDetailPo.class);
    }

    /**
     * 申请退款更新订单支付状态为refund_apply
     *
     * @param orderId
     * @return
     */
    public int refundApplyOrder(Long orderId) {
        return dslContext.update(EP_ORDER)
                         .set(EP_ORDER.PAY_STATUS, EpOrderPayStatus.refund_apply)
                         .where(EP_ORDER.ID.eq(orderId))
                         .and(EP_ORDER.STATUS.eq(EpOrderStatus.save))
                         .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 拒绝退款申请更新订单支付状态为paid
     *
     * @param orderId
     * @return
     */
    public int refundRefuseOrder(Long orderId) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.PAY_STATUS, EpOrderPayStatus.paid)
                .where(EP_ORDER.ID.eq(orderId))
                .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.refund_apply))
                .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据班次id获取提现申请中的订单id
     *
     * @param classId
     * @return
     */
    public List<Long> findIdsWithdrawApply(Long classId) {
        return dslContext.select(EP_ORDER.ID).from(EP_ORDER)
                .where(EP_ORDER.CLASS_ID.eq(classId))
                .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.withdraw_apply))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .fetchInto(Long.class);
    }

    /**
     * 重复支付的订单
     *
     * @param classId
     * @param endTime
     * @return
     */
    public List<Long> findDuplicatePaidOrder(Long classId, Timestamp endTime) {
        return dslContext.select(EP_ORDER.ID)
                         .from(EP_ORDER)
                         .innerJoin(EP_WECHAT_PAY_BILL_DETAIL)
                         .on(EP_ORDER.ID.eq(EP_WECHAT_PAY_BILL_DETAIL.ORDER_ID).and(EP_WECHAT_PAY_BILL_DETAIL.TRADE_STATE.eq(WechatTools.TRADE_STATE_SUCCESS)))
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and("unix_timestamp(`ep`.`ep_wechat_pay_bill_detail`.`transaction_time`)<unix_timestamp(" + "'" + endTime.toString() + "'" + ")")
                .groupBy(EP_ORDER.ID).having(DSL.count(EP_WECHAT_PAY_BILL_DETAIL.OUT_TRADE_NO).gt(BizConstant.DB_NUM_ONE))
                         .fetchInto(Long.class);
    }

    /**
     * 订单申请提现
     *
     * @param id
     * @return
     */
    public int withdrawApplyOrderById(Long id) {
        return dslContext.update(EP_ORDER)
                .set(EP_ORDER.PAY_STATUS, EpOrderPayStatus.withdraw_apply)
                .where(EP_ORDER.ID.eq(id))
                .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid))
                .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 拒绝提现更新订单状态为paid
     *
     * @param classId
     * @return
     */
    public int refuseWithdrawByClassId(Long classId, List<Long> orderIds) {
        return dslContext.update(EP_ORDER)
                         .set(EP_ORDER.PAY_STATUS, EpOrderPayStatus.paid)
                         .where(EP_ORDER.ID.in(orderIds))
                         .and(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.withdraw_apply))
                         .and(EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 校验是否还有未处理、成功、开始、结束的订单
     *
     * @param classId
     * @return
     */
    public Integer countNormalStatusByClassId(Long classId) {
        return dslContext.select(EP_ORDER.ID.count())
                         .from(EP_ORDER)
                         .where(EP_ORDER.CLASS_ID.eq(classId))
                         .and(EP_ORDER.STATUS.in(EpOrderStatus.save, EpOrderStatus.success, EpOrderStatus.opening, EpOrderStatus.end))
                         .and(EP_ORDER.DEL_FLAG.eq(false))
                         .fetchOneInto(Integer.class);
    }
}

