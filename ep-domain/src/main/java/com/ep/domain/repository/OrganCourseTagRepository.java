package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganCourseTagPo;
import com.ep.domain.repository.domain.tables.records.EpOrganCourseTagRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_COURSE_TAG;


/**
 * @Description: 课程标签表Repository
 * @Author: CC.F
 * @Date: 16:28 2018/2/12
 */
@Repository
public class OrganCourseTagRepository extends AbstractCRUDRepository<EpOrganCourseTagRecord, Long, EpOrganCourseTagPo> {

    @Autowired
    public OrganCourseTagRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_COURSE_TAG, EP_ORGAN_COURSE_TAG.ID, EpOrganCourseTagPo.class);
    }
}
