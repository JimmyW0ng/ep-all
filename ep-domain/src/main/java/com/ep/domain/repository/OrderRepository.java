package com.ep.domain.repository;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.enums.ChildClassStatusEnum;
import com.ep.domain.pojo.bo.MemberChildClassBo;
import com.ep.domain.pojo.bo.MemberChildScheduleBo;
import com.ep.domain.pojo.bo.MemberCourseOrderInitBo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.tables.records.EpOrderRecord;
import com.google.common.collect.Lists;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

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
                .and(EP_ORDER.STATUS.in(EpOrderStatus.save, EpOrderStatus.success))
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
    public List<EpOrderPo> findSuccessByChildId(Long childId) {
        return dslContext.selectFrom(EP_ORDER)
                .where(EP_ORDER.CHILD_ID.eq(childId))
                .and(EP_ORDER.STATUS.in(EpOrderStatus.success))
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
        fieldList.add(EP_ORGAN_CLASS.COURSE_NUM);
        fieldList.add(EP_ORGAN_CLASS_CHILD.HONOR_NUM);
        fieldList.add(EP_ORGAN_CLASS_CHILD.SCHEDULE_COMMENT_NUM);
        fieldList.add(EP_ORGAN_CLASS_CHILD.COURSE_COMMENT_FLAG);
        fieldList.add(EP_ORGAN_CLASS_CHILD.LAST_CATELOG_INDEX);
        fieldList.add(EP_CONSTANT_CATALOG.LABEL);
        List<MemberChildClassBo> data = dslContext.select(fieldList)
                .from(EP_ORDER)
                .leftJoin(EP_ORGAN).on(EP_ORDER.OGN_ID.eq(EP_ORGAN.ID))
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORDER.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .leftJoin(EP_ORGAN_CLASS_CHILD).on(EP_ORDER.ID.eq(EP_ORGAN_CLASS_CHILD.ORDER_ID))
                .leftJoin(EP_CONSTANT_CATALOG).on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                .where(condition)
                .orderBy(EP_ORGAN.CREATE_AT.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(MemberChildClassBo.class);
        return new PageImpl(data, pageable, count);
    }

    /**
     * 分页查询孩子的行程
     *
     * @param pageable
     * @param childId
     * @return
     */
    public Page<MemberChildScheduleBo> findChildSchedulePage(Pageable pageable, Long childId) {
        // 封装条件
        Timestamp time = DateTools.zerolizedTime(DateTools.getCurrentDateTime());
        Condition condition = EP_ORDER.CHILD_ID.eq(childId)
                .and(EP_ORDER.STATUS.eq(EpOrderStatus.opening))
                .and(EP_ORDER.DEL_FLAG.eq(false))
                .and(EP_ORGAN_CLASS_CATELOG.START_TIME.greaterOrEqual(time))
                .and(EP_ORGAN_CLASS_CATELOG.DEL_FLAG.eq(false));
        Long count = dslContext.selectCount()
                .from(EP_ORDER)
                .leftJoin(EP_ORGAN_CLASS_CATELOG).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS_CATELOG.CLASS_ID))
                .where(condition)
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORDER.fields());
        fieldList.add(EP_ORGAN.OGN_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_CLASS.COURSE_NUM);
        fieldList.add(EP_ORGAN_CLASS_CATELOG.CATELOG_INDEX);
        fieldList.add(EP_ORGAN_CLASS_CATELOG.START_TIME);
        fieldList.add(EP_CONSTANT_CATALOG.LABEL);
        List<MemberChildScheduleBo> data = dslContext.select(fieldList)
                                                     .from(EP_ORDER)
                                                     .leftJoin(EP_ORGAN).on(EP_ORDER.OGN_ID.eq(EP_ORGAN.ID))
                                                     .leftJoin(EP_ORGAN_COURSE).on(EP_ORDER.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                                                     .leftJoin(EP_ORGAN_CLASS).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                                                     .leftJoin(EP_ORGAN_CLASS_CATELOG).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS_CATELOG.CLASS_ID))
                                                     .leftJoin(EP_CONSTANT_CATALOG).on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                                                     .where(condition)
                                                     .orderBy(EP_ORGAN_CLASS_CATELOG.START_TIME.asc())
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
}

