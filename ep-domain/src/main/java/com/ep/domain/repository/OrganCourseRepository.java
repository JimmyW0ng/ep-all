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
import java.util.Optional;

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
     * 根据id获取记录
     *
     * @param id
     * @return
     */
    public Optional<EpOrganCoursePo> findById(Long id) {
        EpOrganCoursePo data = dslContext.selectFrom(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.ID.eq(id))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganCoursePo.class);
        return Optional.ofNullable(data);
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
                .and(EP_ORGAN_COURSE.COURSE_STATUS.in(EpOrganCourseCourseStatus.online,
                        EpOrganCourseCourseStatus.offline))
                .fetchOneInto(Long.class);
        if (count == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, count);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_COURSE.ID);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_CATALOG_ID);
        fieldList.add(EP_ORGAN_COURSE.COURSE_TYPE);
        fieldList.add(EP_ORGAN_COURSE.COURSE_INTRODUCE);
        fieldList.add(EP_ORGAN_COURSE.PRIZE_MIN);
        fieldList.add(EP_ORGAN_COURSE.VIP_FLAG);
        fieldList.add(EP_ORGAN_COURSE.COURSE_STATUS);
        fieldList.add(EP_ORGAN_COURSE.ONLINE_TIME);
        fieldList.add(EP_ORGAN_COURSE.ENTER_TIME_START);
        fieldList.add(EP_ORGAN_COURSE.TOTAL_PARTICIPATE);
        fieldList.add(EP_ORGAN.VIP_NAME);
        fieldList.add(EP_CONSTANT_CATALOG.LABEL);
        List<OrganCourseBo> pList = dslContext.select(fieldList).from(EP_ORGAN_COURSE)
                .leftJoin(EP_ORGAN)
                .on(EP_ORGAN_COURSE.OGN_ID.eq(EP_ORGAN.ID))
                .and(EP_ORGAN.DEL_FLAG.eq(false))
                .leftJoin(EP_CONSTANT_CATALOG)
                .on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.in(EpOrganCourseCourseStatus.online,
                        EpOrganCourseCourseStatus.offline))
                .orderBy(EP_ORGAN_COURSE.COURSE_STATUS.sortAsc(EpOrganCourseCourseStatus.online,
                        EpOrganCourseCourseStatus.offline), EP_ORGAN_COURSE.ONLINE_TIME.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganCourseBo.class);
        return new PageImpl<>(pList, pageable, count);
    }

    /**
     * 后台机构课程分页列表
     *
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
     *
     * @param po
     */
    public void updateByIdLock(EpOrganCoursePo po) {
        dslContext.selectFrom(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.ID.eq(po.getId()))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false)).forUpdate();
        dslContext.update(EP_ORGAN_COURSE)
                .set(EP_ORGAN_COURSE.COURSE_NAME, po.getCourseName())
                .set(EP_ORGAN_COURSE.COURSE_TYPE, po.getCourseType())
                .set(EP_ORGAN_COURSE.COURSE_INTRODUCE, po.getCourseIntroduce())
                .set(EP_ORGAN_COURSE.COURSE_CONTENT, po.getCourseContent())
                .set(EP_ORGAN_COURSE.PRIZE_MIN, po.getPrizeMin())
                .set(EP_ORGAN_COURSE.ONLINE_TIME, po.getOnlineTime())
                .set(EP_ORGAN_COURSE.ENTER_TIME_START, po.getEnterTimeStart())
                .set(EP_ORGAN_COURSE.ENTER_TIME_END, po.getEnterTimeEnd())
                .where(EP_ORGAN_COURSE.ID.eq(po.getId()))
                .execute();
    }

    /**
     * 根据id紧急修改课程对象EpOrganCoursePo
     *
     * @param po
     */
    public void rectifyByIdLock(EpOrganCoursePo po) {
        dslContext.selectFrom(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.ID.eq(po.getId()))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false)).forUpdate();
        dslContext.update(EP_ORGAN_COURSE)
//                .set(EP_ORGAN_COURSE.COURSE_NAME, po.getCourseName())
//                .set(EP_ORGAN_COURSE.COURSE_TYPE, po.getCourseType())
                .set(EP_ORGAN_COURSE.COURSE_INTRODUCE, po.getCourseIntroduce())
                .set(EP_ORGAN_COURSE.COURSE_CONTENT, po.getCourseContent())
                .set(EP_ORGAN_COURSE.ONLINE_TIME, po.getOnlineTime())
                .set(EP_ORGAN_COURSE.ENTER_TIME_START, po.getEnterTimeStart())
                .set(EP_ORGAN_COURSE.ENTER_TIME_END, po.getEnterTimeEnd())
                .where(EP_ORGAN_COURSE.ID.eq(po.getId()))
                .execute();
    }

    /**
     * 根据ognId获取课程目录ID的集合
     *
     * @param ognId
     * @return
     */
    public List<Long> findCourseCatalogIdByOgnId(Long ognId) {
        return dslContext.select(EP_ORGAN_COURSE.COURSE_CATALOG_ID)
                .from(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .groupBy(EP_ORGAN_COURSE.COURSE_CATALOG_ID)
                .fetchInto(Long.class);
    }

    /**
     * 根据id逻辑删除记录
     *
     * @param id
     */
    public void deleteLogicById(Long id) {
        dslContext.update(EP_ORGAN_COURSE)
                .set(EP_ORGAN_COURSE.DEL_FLAG, true)
                .where(EP_ORGAN_COURSE.ID.eq(id))
                .execute();
    }

    /**
     * 根据id课程上线
     *
     * @param id
     */
    public int onlineById(Long id) {
        return dslContext.update(EP_ORGAN_COURSE)
                .set(EP_ORGAN_COURSE.COURSE_STATUS, EpOrganCourseCourseStatus.online)
                .where(EP_ORGAN_COURSE.ID.eq(id))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.eq(EpOrganCourseCourseStatus.save))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id课程下线
     *
     * @param id
     */
    public int offlineById(Long id) {
        return dslContext.update(EP_ORGAN_COURSE)
                .set(EP_ORGAN_COURSE.COURSE_STATUS, EpOrganCourseCourseStatus.offline)
                .where(EP_ORGAN_COURSE.ID.eq(id))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.eq(EpOrganCourseCourseStatus.online))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据机构id获取记录
     *
     * @param ognId
     * @return
     */
    public List<EpOrganCoursePo> findByOgnId(Long ognId) {
        return dslContext.selectFrom(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .fetchInto(EpOrganCoursePo.class);
    }


    /**
     * 根据机构id和状态获取记录
     *
     * @param ognId
     * @return
     */
    public List<EpOrganCoursePo> findByOgnIdAndStatus(Long ognId, EpOrganCourseCourseStatus status) {
        return dslContext.selectFrom(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.eq(status))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .fetchInto(EpOrganCoursePo.class);
    }

    /**
     * 机构下线，该机构下的课程下线
     *
     * @param ognId
     */
    public void updateCourseByOfflineOgn(Long ognId) {
        dslContext.update(EP_ORGAN_COURSE)
                .set(EP_ORGAN_COURSE.COURSE_STATUS, EpOrganCourseCourseStatus.offline)
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.eq(EpOrganCourseCourseStatus.save))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据课程类目id统计同一机构下相同类目的课程数
     *
     * @param courseCatalogId
     * @param ognId
     * @return
     */
    public long countByCourseCatalogIdAndOgnId(Long courseCatalogId, Long ognId) {
        return dslContext.selectCount().from(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(courseCatalogId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }

    /**
     * 课程总参加人数扣减
     *
     * @param courseId
     * @param count
     */
    public int totalParticipateCancel(Long courseId, int count) {
        return dslContext.update(EP_ORGAN_COURSE)
                .set(EP_ORGAN_COURSE.TOTAL_PARTICIPATE, EP_ORGAN_COURSE.TOTAL_PARTICIPATE.subtract(count))
                .where(EP_ORGAN_COURSE.ID.eq(courseId))
                .and(EP_ORGAN_COURSE.TOTAL_PARTICIPATE.greaterThan(BizConstant.DB_NUM_ZERO))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 增加课程总参与人数
     *
     * @param courseId
     * @param count
     */
    public int addTotalParticipate(Long courseId, int count) {
        return dslContext.update(EP_ORGAN_COURSE)
                .set(EP_ORGAN_COURSE.TOTAL_PARTICIPATE, EP_ORGAN_COURSE.TOTAL_PARTICIPATE.add(count))
                .where(EP_ORGAN_COURSE.ID.eq(courseId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据课程目录ID统计记录数
     *
     * @param catalogIds
     * @return
     */
    public long countByCatalogIds(Long[] catalogIds) {
        return dslContext.select(DSL.count(EP_ORGAN_COURSE.ID)).from(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.COURSE_CATALOG_ID.in(catalogIds))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class);
    }

    /**
     * 是否存在指定课程类目(状态必须为online)
     *
     * @param ognId
     * @param courseCatalogId
     * @return
     */
    public boolean existCatalog(Long ognId, Long courseCatalogId) {
        return dslContext.select(DSL.count(EP_ORGAN_COURSE.ID)).from(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(courseCatalogId))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.eq(EpOrganCourseCourseStatus.online))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .fetchOneInto(Long.class) > BizConstant.LONG_ZERO;
    }
}

