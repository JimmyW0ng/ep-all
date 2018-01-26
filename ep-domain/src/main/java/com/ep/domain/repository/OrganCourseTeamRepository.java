package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganCourseTeamPo;
import com.ep.domain.repository.domain.tables.records.EpOrganCourseTeamRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_COURSE_TEAM;

/**
 * @Description:机构课程团队信息表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganCourseTeamRepository extends AbstractCRUDRepository<EpOrganCourseTeamRecord, Long, EpOrganCourseTeamPo> {

    @Autowired
    public OrganCourseTeamRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_COURSE_TEAM, EP_ORGAN_COURSE_TEAM.ID, EpOrganCourseTeamPo.class);
    }

}

