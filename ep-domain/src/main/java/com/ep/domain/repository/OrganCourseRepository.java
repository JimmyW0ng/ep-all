package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganCourseBo;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.repository.domain.tables.records.EpOrganCourseRecord;
import com.google.common.collect.Lists;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
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
                        EpOrganCourseCourseStatus.online,
                        EpOrganCourseCourseStatus.offline))
                .fetchOneInto(OrganCourseBo.class);
    }

    /**
     * 按机构查询课程列表
     *
     * @param ognId
     * @return
     */
    public List<OrganCourseBo> getByOgnId(Long ognId) {
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_COURSE.fields());
        fieldList.add(EP_CONSTANT_CATALOG.LABEL);
        fieldList.add(EP_FILE.FILE_URL.as("mainPicUrl"));
        return dslContext.select(fieldList).from(EP_ORGAN_COURSE)
                .leftJoin(EP_CONSTANT_CATALOG)
                .on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .leftJoin(EP_FILE)
                .on(EP_ORGAN_COURSE.ID.eq(EP_FILE.SOURCE_ID))
                .and(EP_FILE.BIZ_TYPE_CODE.eq(BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC))
                .and(EP_FILE.DEL_FLAG.eq(false))
                .where(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .and(EP_ORGAN_COURSE.COURSE_STATUS.in(EpOrganCourseCourseStatus.online, EpOrganCourseCourseStatus.online))
                .fetchInto(OrganCourseBo.class);
    }

    /**
     * 根据机构id和课程id查询
     *
     * @param ognId
     * @param courseId
     * @return
     */
    public EpOrganCoursePo getByOgnIdAndCourseId(Long ognId, Long courseId) {
        return dslContext.selectFrom(EP_ORGAN_COURSE)
                .where(EP_ORGAN_COURSE.ID.eq(courseId))
                .and(EP_ORGAN_COURSE.OGN_ID.eq(ognId))
                .and(EP_ORGAN_COURSE.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganCoursePo.class);
    }

    /**
     * 按机构查询课程列表
     *
     * @param pageable
     * @param conditions
     * @return
     */
    public Page<OrganCourseBo> getCoursesByOgnWithPage(Pageable pageable, Collection<Condition> conditions) {
        long count = dslContext.selectCount().from(EP_ORGAN_COURSE)
                .leftJoin(EP_CONSTANT_CATALOG)
                .on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .where(conditions)
                .fetchOneInto(Long.class);

        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_COURSE.fields());
        fieldList.add(EP_CONSTANT_CATALOG.LABEL);

        List<OrganCourseBo> pList = dslContext.select(fieldList).from(EP_ORGAN_COURSE)
                .leftJoin(EP_CONSTANT_CATALOG)
                .on(EP_ORGAN_COURSE.COURSE_CATALOG_ID.eq(EP_CONSTANT_CATALOG.ID))
                .and(EP_CONSTANT_CATALOG.DEL_FLAG.eq(false))
                .where(conditions)
                .orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganCourseBo.class);
        return new PageImpl<>(pList, pageable, count);
    }
}
