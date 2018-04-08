package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganAccountAllClassBo;
import com.ep.domain.pojo.bo.OrganClassBo;
import com.ep.domain.pojo.bo.OrganClassEnterBo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import com.ep.domain.repository.domain.tables.records.EpOrganClassRecord;
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
 * @Description:机构课程班次表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganClassRepository extends AbstractCRUDRepository<EpOrganClassRecord, Long, EpOrganClassPo> {

    @Autowired
    public OrganClassRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS, EP_ORGAN_CLASS.ID, EpOrganClassPo.class);
    }

    /**
     * 获取课程班次
     *
     * @param courseId
     * @return
     */
    public List<EpOrganClassPo> getByCourseId(Long courseId) {
        return dslContext.selectFrom(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_CLASS.SORT.desc(), EP_ORGAN_CLASS.ID.asc())
                .fetchInto(EpOrganClassPo.class);
    }

    /**
     * 获取课程班次报名相关信息
     *
     * @param courseId
     * @return
     */
    public List<OrganClassEnterBo> getClassEnterInfoByCourseId(Long courseId) {
        return dslContext.selectFrom(EP_ORGAN_CLASS)
                         .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .orderBy(EP_ORGAN_CLASS.SORT.desc(), EP_ORGAN_CLASS.ID.asc())
                         .fetchInto(OrganClassEnterBo.class);
    }

    /**
     * 获取课程和班次状态获取
     *
     * @param courseId
     * @return
     */
    public List<EpOrganClassPo> getByCourseIdAndStatus(Long courseId, EpOrganClassStatus... status) {
        return dslContext.selectFrom(EP_ORGAN_CLASS)
                         .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                         .and(EP_ORGAN_CLASS.STATUS.in(status))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .orderBy(EP_ORGAN_CLASS.SORT.desc(), EP_ORGAN_CLASS.ID.asc())
                         .fetchInto(EpOrganClassPo.class);
    }

    /**
     * 下单（没有报名限制）
     *
     * @param classId
     * @return
     */
    public int orderWithNoLimit(Long classId) {
        return dslContext.update(EP_ORGAN_CLASS)
                         .set(EP_ORGAN_CLASS.ORDERED_NUM, EP_ORGAN_CLASS.ORDERED_NUM.add(BizConstant.DB_NUM_ONE))
                         .where(EP_ORGAN_CLASS.ID.eq(classId))
                         .and(EP_ORGAN_CLASS.ORDERED_NUM.lessOrEqual(BizConstant.ORDER_BEYOND_NUM))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 下单（有报名限制）
     *
     * @param classId
     * @return
     */
    public int orderWithLimit(Long classId) {
        return dslContext.update(EP_ORGAN_CLASS)
                         .set(EP_ORGAN_CLASS.ORDERED_NUM, EP_ORGAN_CLASS.ORDERED_NUM.add(BizConstant.DB_NUM_ONE))
                         .where(EP_ORGAN_CLASS.ID.eq(classId))
                         .and(EP_ORGAN_CLASS.ENTER_LIMIT_FLAG.eq(true))
                         .and(EP_ORGAN_CLASS.ENTERED_NUM.lessThan(EP_ORGAN_CLASS.ENTER_REQUIRE_NUM))
                         .and(EP_ORGAN_CLASS.ORDERED_NUM.lessOrEqual(BizConstant.ORDER_BEYOND_NUM))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 根据课程负责人获取全部班次-分页
     *
     * @param pageable
     * @param ognAccountId
     * @return
     */
    public Page<OrganAccountAllClassBo> findAllClassByOrganAccountForPage(Pageable pageable, Long ognAccountId) {
        Long count = dslContext.selectCount()
                .from(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.OGN_ACCOUNT_ID.eq(ognAccountId))
                .and(EP_ORGAN_CLASS.STATUS.in(EpOrganClassStatus.online,
                        EpOrganClassStatus.opening,
                        EpOrganClassStatus.end))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS.fields());
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        List<OrganAccountAllClassBo> data = dslContext.select(fieldList)
                                                      .from(EP_ORGAN_CLASS)
                                                      .leftJoin(EP_ORGAN_COURSE)
                                                      .on(EP_ORGAN_CLASS.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                                                      .where(EP_ORGAN_CLASS.OGN_ACCOUNT_ID.eq(ognAccountId))
                                                      .and(EP_ORGAN_CLASS.STATUS.in(EpOrganClassStatus.online,
                        EpOrganClassStatus.opening,
                        EpOrganClassStatus.end))
                                                      .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                                                      .orderBy(EP_ORGAN_CLASS.STATUS.sortAsc(EpOrganClassStatus.online,
                        EpOrganClassStatus.opening,
                        EpOrganClassStatus.end),
                        EP_ORGAN_COURSE.ONLINE_TIME.desc())
                                                      .fetchInto(OrganAccountAllClassBo.class);
        return new PageImpl(data, pageable, count);
    }

    /**
     * 更新班次EpOrganClassPo
     *
     * @param po
     */
    public void updateOrganClassPo(EpOrganClassPo po) {
        dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.CLASS_NAME, po.getClassName())
                .set(EP_ORGAN_CLASS.OGN_ACCOUNT_ID, po.getOgnAccountId())
                .set(EP_ORGAN_CLASS.CLASS_PRIZE, po.getClassPrize())
                .set(EP_ORGAN_CLASS.DISCOUNT_AMOUNT, po.getDiscountAmount())
                .set(EP_ORGAN_CLASS.ENTER_LIMIT_FLAG, po.getEnterLimitFlag())
                .set(EP_ORGAN_CLASS.ENTER_REQUIRE_NUM, po.getEnteredNum())
                .set(EP_ORGAN_CLASS.COURSE_NUM, po.getCourseNum())
                .set(EP_ORGAN_CLASS.UPDATE_AT, DSL.currentTimestamp())
                .where(EP_ORGAN_CLASS.ID.eq(po.getId()))
                .execute();
    }

    /**
     * 根据课程courseId获取班级ClassId
     *
     * @param courseId
     * @return
     */
    public List<Long> findClassIdsByCourseId(Long courseId) {
        return dslContext.select(EP_ORGAN_CLASS.ID).from(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchInto(Long.class);
    }

    /**
     * 根据ids批量逻辑删除记录
     *
     * @param ids
     */
    public void deleteByIds(List<Long> ids) {
        dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.DEL_FLAG, true)
                .where(EP_ORGAN_CLASS.ID.in(ids))
                .execute();
    }

    /**
     * 根据课程courseId批量物理删除记录
     *
     * @param courseId
     */
    public void deletePhysicByCourseId(Long courseId) {
        dslContext.delete(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .execute();
    }

    /**
     * 根据课程courseId批量逻辑删除记录
     *
     * @param courseId
     */
    public List<Long> deleteLogicByCourseId(Long courseId) {
       List<Long> list = dslContext.select(EP_ORGAN_CLASS.ID).from(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchInto(Long.class);
        dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.DEL_FLAG,true)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
        return list;
    }

    /**
     * 报名成功更新entered_num字段
     *
     * @param classId
     * @param orderCount
     */
    public int updateEnteredNumWithEnterLimit(Long classId, int orderCount) {
        return dslContext.update(EP_ORGAN_CLASS)
                         .set(EP_ORGAN_CLASS.ENTERED_NUM, EP_ORGAN_CLASS.ENTERED_NUM.add(orderCount))
                         .where(EP_ORGAN_CLASS.ID.eq(classId))
                         .and(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.online))
                         .and(EP_ORGAN_CLASS.ENTER_LIMIT_FLAG.eq(true))
                         .and(EP_ORGAN_CLASS.ENTERED_NUM.lessThan(EP_ORGAN_CLASS.ENTER_REQUIRE_NUM))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 报名成功更新entered_num字段
     *
     * @param classId
     * @param orderCount
     */
    public int updateEnteredNumWithEnterNotLimit(Long classId, int orderCount) {
        return dslContext.update(EP_ORGAN_CLASS)
                         .set(EP_ORGAN_CLASS.ENTERED_NUM, EP_ORGAN_CLASS.ENTERED_NUM.add(orderCount))
                         .where(EP_ORGAN_CLASS.ID.eq(classId))
                         .and(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.online))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 根据id和count，在报名取消时更新班级已报名人数
     * @param classId
     * @param count
     */
    public int enteredNumByOrderCancel(Long classId, int count) {
        return dslContext.update(EP_ORGAN_CLASS)
                         .set(EP_ORGAN_CLASS.ENTERED_NUM,EP_ORGAN_CLASS.ENTERED_NUM.subtract(count))
                         .where(EP_ORGAN_CLASS.ID.eq(classId))
                         .and(EP_ORGAN_CLASS.ENTERED_NUM.greaterThan(BizConstant.DB_NUM_ZERO))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .execute();
    }

    public Page<OrganClassBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition){
        long totalCount = dslContext.selectCount()
                .from(EP_ORGAN_CLASS)
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_CLASS.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS.fields());
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS)
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_CLASS.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .where(condition);

        List<OrganClassBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganClassBo.class);
        PageImpl<OrganClassBo> page = new PageImpl<OrganClassBo>(list, pageable, totalCount);
        return page;
    }

    /**
     * 根据ids班次上线
     * @param ids
     */
    public void onlineByIds(List<Long> ids){
        dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.STATUS, EpOrganClassStatus.online)
                .where(EP_ORGAN_CLASS.ID.in(ids))
                .and(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.save))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id班次上线
     * @param id
     */
    public void onlineById(Long id){
        dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.STATUS, EpOrganClassStatus.online)
                .where(EP_ORGAN_CLASS.ID.eq(id))
                .and(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.save))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据courseId班次上线
     * @param courseId
     */
    public void onlineByCourseId(Long courseId){
        dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.STATUS, EpOrganClassStatus.online)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.save))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id开课
     *
     * @param id
     * @return
     */
    public int openById(Long id) {
        return dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.STATUS, EpOrganClassStatus.opening)
                .where(EP_ORGAN_CLASS.ID.eq(id))
                .and(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.online))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id结束班次
     *
     * @param id
     * @return
     */
    public int endById(Long id) {
        return dslContext.update(EP_ORGAN_CLASS)
                         .set(EP_ORGAN_CLASS.STATUS, EpOrganClassStatus.end)
                         .where(EP_ORGAN_CLASS.ID.eq(id))
                .and(EP_ORGAN_CLASS.STATUS.in(EpOrganClassStatus.online, EpOrganClassStatus.opening))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 机构下线，该机构下的班次结束
     *
     * @param ognId
     */
    public void updateClassByOfflineOgn(Long ognId) {
        dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.STATUS, EpOrganClassStatus.end)
                .where(EP_ORGAN_CLASS.OGN_ID.eq(ognId))
                .and(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.save))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据班次负责人(教师)id和班次状态获取班次集合
     *
     * @param ognAccountId
     * @param classStatus
     * @return
     */
    public List<EpOrganAccountPo> findByOgnAccountIdAndClassStatus(Long ognAccountId, EpOrganClassStatus[] classStatus) {
        return dslContext.selectFrom(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.OGN_ACCOUNT_ID.eq(ognAccountId))
                .and(EP_ORGAN_CLASS.STATUS.in(classStatus))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchInto(EpOrganAccountPo.class);
    }

    /**
     * 根据班次负责人(教师)id和班次状态获取班次条数
     *
     * @param ognAccountId
     * @param classStatus
     * @return
     */
    public long countByOgnAccountIdAndClassStatus(Long ognAccountId, EpOrganClassStatus[] classStatus) {
        return dslContext.selectCount().from(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.OGN_ACCOUNT_ID.eq(ognAccountId))
                .and(EP_ORGAN_CLASS.STATUS.in(classStatus))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }

    /**
     * 根据id和班次状态获取班次记录
     *
     * @param id
     * @param status
     * @return
     */
    public EpOrganClassPo getByIdAndStatusLock(Long id, EpOrganClassStatus status) {
        return dslContext.selectFrom(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.ID.eq(id))
                .and(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.online))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .forUpdate().fetchOneInto(EpOrganClassPo.class);
    }

    /**
     * 获取班级成员昵称
     *
     * @param classId
     * @return
     */
    public List<String> findClassChildNickNameByClassId(Long ognId, Long classId) {
        return dslContext.select(EP_MEMBER_CHILD.CHILD_NICK_NAME).from(EP_ORGAN_CLASS_CHILD)
                         .innerJoin(EP_ORGAN_CLASS)
                         .on(EP_ORGAN_CLASS.ID.eq(EP_ORGAN_CLASS_CHILD.CLASS_ID))
                         .and(EP_ORGAN_CLASS.OGN_ID.eq(ognId))
                         .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                         .leftJoin(EP_MEMBER_CHILD)
                         .on(EP_ORGAN_CLASS_CHILD.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                         .where(EP_ORGAN_CLASS_CHILD.CLASS_ID.eq(classId))
                         .and(EP_ORGAN_CLASS_CHILD.DEL_FLAG.eq(false))
                         .and(EP_MEMBER_CHILD.ID.isNotNull())
                         .fetchInto(String.class);
    }


    /**
     * 根据id获取记录
     *
     * @param id
     * @return
     */
    public Optional<EpOrganClassPo> findById(Long id) {
        EpOrganClassPo data = dslContext.selectFrom(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.ID.eq(id))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganClassPo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 根据课程id和状态获取记录
     *
     * @param courseId
     * @return
     */
    public List<EpOrganClassPo> findByCourseIdAndStatus(Long courseId, EpOrganClassStatus[] statuses) {
        return dslContext.selectFrom(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.STATUS.in(statuses))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassPo.class);
    }

    /**
     * 获取opening/end的正常班次和normal/end的预约班次,提供给随堂评价页面
     */
    public List<EpOrganClassPo> findProceedClassByCourseId(Long courseId) {
        return dslContext.selectFrom(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.STATUS.in(EpOrganClassStatus.opening, EpOrganClassStatus.end).and(EP_ORGAN_CLASS.TYPE.eq(EpOrganClassType.normal))
                        .or(EP_ORGAN_CLASS.STATUS.in(EpOrganClassStatus.online, EpOrganClassStatus.end).and(EP_ORGAN_CLASS.TYPE.eq(EpOrganClassType.bespeak))))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassPo.class);
    }

    /**
     * 根据id提前结束班次
     *
     * @param id
     * @return
     */
    public int advancedEndById(Long id) {
        return dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.STATUS, EpOrganClassStatus.end)
                .where(EP_ORGAN_CLASS.ID.eq(id))
                .and(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.online))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
    }

}

