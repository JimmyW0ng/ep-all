package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganAccountClassBo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

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
     * 下单（没有报名限制）
     *
     * @param classId
     * @return
     */
    public int orderWithNoLimit(Long classId) {
        return dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.ORDERED_NUM, EP_ORGAN_CLASS.ORDERED_NUM.add(BizConstant.DB_NUM_ONE))
                .where(EP_ORGAN_CLASS.ID.eq(classId))
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
                .and(EP_ORGAN_CLASS.ORDERED_NUM.lessThan(BizConstant.ORDER_BEYOND_NUM))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据课程负责人获取今日班次
     *
     * @param ognAccountId
     * @return
     */
    public List<OrganAccountClassBo> findClassByOgnAccountId(Long ognAccountId, Timestamp startTime, Timestamp endTime) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_CLASS.fields());
        fieldList.add(EP_ORGAN.OGN_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_CLASS_CATELOG.ID.as("classCatelogId"));
        fieldList.add(EP_ORGAN_CLASS_CATELOG.CHILD_EVALUATED_NUM);
        return dslContext.select(fieldList)
                .from(EP_ORGAN_CLASS)
                .leftJoin(EP_ORGAN)
                .on(EP_ORGAN_CLASS.OGN_ID.eq(EP_ORGAN.ID))
                .leftJoin(EP_ORGAN_COURSE)
                .on(EP_ORGAN_CLASS.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .leftJoin(EP_ORGAN_CLASS_CATELOG)
                .on(EP_ORGAN_CLASS.ID.eq(EP_ORGAN_CLASS_CATELOG.CLASS_ID))
                .and(EP_ORGAN_CLASS_CATELOG.DEL_FLAG.eq(false))
                .where(EP_ORGAN_CLASS.OGN_ACCOUNT_ID.eq(ognAccountId))
                .and(EP_ORGAN_CLASS_CATELOG.START_TIME.between(startTime, endTime))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_CLASS_CATELOG.START_TIME.asc())
                .fetchInto(OrganAccountClassBo.class);
    }

    /**
     * 更新班次EpOrganClassPo
     * @param po
     */
    public void updateOrganClassPo(EpOrganClassPo po){
        dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.CLASS_NAME,po.getClassName())
                .set(EP_ORGAN_CLASS.OGN_ACCOUNT_ID,po.getOgnAccountId())
                .set(EP_ORGAN_CLASS.CLASS_PRIZE,po.getClassPrize())
                .set(EP_ORGAN_CLASS.DISCOUNT_AMOUNT,po.getDiscountAmount())
                .set(EP_ORGAN_CLASS.ENTER_LIMIT_FLAG,po.getEnterLimitFlag())
                .set(EP_ORGAN_CLASS.ENTER_REQUIRE_NUM,po.getEnteredNum())
                .set(EP_ORGAN_CLASS.COURSE_NUM,po.getCourseNum())
                .set(EP_ORGAN_CLASS.UPDATE_AT, DSL.currentTimestamp())
                .where(EP_ORGAN_CLASS.ID.eq(po.getId()))
                .execute();
    }

    /**
     * 根据课程courseId获取班级ClassId
     * @param courseId
     * @return
     */
    public List<Long> findClassIdsByCourseId(Long courseId){
        return dslContext.select(EP_ORGAN_CLASS.ID).from(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchInto(Long.class);
    }

    /**
     * 根据ids批量逻辑删除记录
     * @param ids
     */
    public void deleteByIds(List<Long> ids){
        dslContext.update(EP_ORGAN_CLASS)
                .set(EP_ORGAN_CLASS.DEL_FLAG,true)
                .where(EP_ORGAN_CLASS.ID.in(ids))
                .execute();
    }

    /**
     * 根据课程courseId批量物理删除记录
     * @param courseId
     */
    public void deletePhysicByCourseId(Long courseId){
        dslContext.delete(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .execute();
    }
}

