package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganCourseBo;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.repository.domain.tables.records.EpOrganCourseRecord;
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

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description:机构课程表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganCourseRepository extends AbstractCRUDRepository<EpOrganCourseRecord, Long, EpOrganCoursePo> {

    @Autowired
    public OrganCourseRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_COURSE, EP_ORGAN_COURSE.ID, EpOrganCoursePo.class);
    }

    /**
     * 课程详情
     *
     * @param courseId
     * @return
     */
    public OrganCourseBo getDetailById(Long courseId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_COURSE.fields());
        fieldList.add(EP_CONSTANT_CATALOG.LABEL);
        return dslContext.select(fieldList).from(EP_ORGAN_COURSE)
                .leftJoin(EP_CONSTANT_CATALOG)
                .on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .where(EP_ORGAN_COURSE.ID.eq(courseId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.in(EpOrganCourseCourseStatus.online,
                        EpOrganCourseCourseStatus.opening,
                        EpOrganCourseCourseStatus.offline))
                .fetchOneInto(OrganCourseBo.class);
    }

    /**
     * 查询机构课程列表-分页
     *
     * @param pageable
     * @param ognId
     * @return
     */
    public Page<OrganCourseBo> findCourseByOgnIdForPage(Pageable pageable, Long ognId) {
        long count = dslContext.selectCount().from(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.in(EpOrganCourseCourseStatus.opening,
                        EpOrganCourseCourseStatus.online,
                        EpOrganCourseCourseStatus.offline))
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_COURSE.fields());
        fieldList.add(EP_CONSTANT_CATALOG.LABEL);
        List<OrganCourseBo> pList = dslContext.select(fieldList).from(EP_ORGAN_COURSE)
                .leftJoin(EP_CONSTANT_CATALOG)
                .on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.in(EpOrganCourseCourseStatus.opening,
                        EpOrganCourseCourseStatus.online,
                        EpOrganCourseCourseStatus.offline))
                .orderBy(EP_ORGAN_COURSE.COURSE_STATUS.sortAsc(EpOrganCourseCourseStatus.opening,
                        EpOrganCourseCourseStatus.online,
                        EpOrganCourseCourseStatus.offline))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganCourseBo.class);
        return new PageImpl<>(pList, pageable, count);
    }

    /**
     * 后台机构课程分页列表
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganCourseBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_ORGAN_COURSE)
                .leftJoin(EP_ORGAN).on(EP_ORGAN_COURSE.OGN_ID.eq(EP_ORGAN.ID))
                .leftJoin(EP_CONSTANT_CATALOG).on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_COURSE.fields());
        fieldList.add(EP_ORGAN.OGN_NAME.as("ognName"));
        fieldList.add(EP_CONSTANT_CATALOG.LABEL.as("courseCatalogName"));

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORGAN_COURSE)
                .leftJoin(EP_ORGAN).on(EP_ORGAN_COURSE.OGN_ID.eq(EP_ORGAN.ID))
                .leftJoin(EP_CONSTANT_CATALOG).on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                .where(condition);

        List<OrganCourseBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganCourseBo.class);
        PageImpl<OrganCourseBo> pPage = new PageImpl<OrganCourseBo>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 根据id更新课程对象EpOrganCoursePo
     * @param po
     */
    public void updateByIdLock(EpOrganCoursePo po){
        dslContext.selectFrom(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.ID.eq(po.getId()))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false)).forUpdate();
        dslContext.update(EP_ORGAN_COURSE)
                .set(EP_ORGAN_COURSE.COURSE_NAME,po.getCourseName())
                .set(EP_ORGAN_COURSE.COURSE_TYPE,po.getCourseType())
                .set(EP_ORGAN_COURSE.COURSE_INTRODUCE,po.getCourseIntroduce())
                .set(EP_ORGAN_COURSE.COURSE_CONTENT,po.getCourseContent())
                .set(EP_ORGAN_COURSE.COURSE_NOTE,po.getCourseNote())
                .set(EP_ORGAN_COURSE.PRIZE_MIN,po.getPrizeMin())
                .set(EP_ORGAN_COURSE.COURSE_ADDRESS,po.getCourseAddress())
                .set(EP_ORGAN_COURSE.ONLINE_TIME,po.getOnlineTime())
                .set(EP_ORGAN_COURSE.ENTER_TIME_START,po.getEnterTimeStart())
                .set(EP_ORGAN_COURSE.ENTER_TIME_END,po.getEnterTimeEnd())
                .set(EP_ORGAN_COURSE.UPDATE_AT, DSL.currentTimestamp())
                .where(EP_ORGAN_COURSE.ID.eq(po.getId()))
                .execute();

    }

}

