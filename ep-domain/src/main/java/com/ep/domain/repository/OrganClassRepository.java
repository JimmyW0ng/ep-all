package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS;

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

}

