package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganClassCatelogPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassCatelogRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS_CATELOG;
import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS;

/**
 * @Description: 班次课程内容目录表Repository
 * @Author: CC.F
 * @Date: 9:53 2018/2/12
 */
@Repository
public class OrganClassCatelogRepository extends AbstractCRUDRepository<EpOrganClassCatelogRecord, Long, EpOrganClassCatelogPo> {

    @Autowired
    public OrganClassCatelogRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_CATELOG, EP_ORGAN_CLASS_CATELOG.ID, EpOrganClassCatelogPo.class);
    }

    public List<EpOrganClassCatelogPo> findByCourseId(Long courseId){
        List<Long> classId=dslContext.select(EP_ORGAN_CLASS.ID)
                .from(EP_ORGAN_CLASS)
                .where(EP_ORGAN_CLASS.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_CLASS.DEL_FLAG.eq(false))
                .fetchInto(Long.class);
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATELOG)
                .where(EP_ORGAN_CLASS_CATELOG.CLASS_ID.in(classId))
                .and(EP_ORGAN_CLASS_CATELOG.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassCatelogPo.class);
    }

    public List<EpOrganClassCatelogPo> findByClassId(Long classId){
        return dslContext.selectFrom(EP_ORGAN_CLASS_CATELOG)
                .where(EP_ORGAN_CLASS_CATELOG.CLASS_ID.eq(classId))
                .and(EP_ORGAN_CLASS_CATELOG.DEL_FLAG.eq(false))
                .fetchInto(EpOrganClassCatelogPo.class);
    }
}
