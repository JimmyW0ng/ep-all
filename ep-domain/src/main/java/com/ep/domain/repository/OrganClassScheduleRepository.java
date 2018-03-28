package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganClassSchedulePo;
import com.ep.domain.repository.domain.tables.records.EpOrganClassScheduleRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS_SCHEDULE;

/**
 * @Description: 班次行程表Repository
 * @Author: CC.F
 * @Date: 9:53 2018/2/12
 */
@Repository
public class OrganClassScheduleRepository extends AbstractCRUDRepository<EpOrganClassScheduleRecord, Long, EpOrganClassSchedulePo> {

    @Autowired
    public OrganClassScheduleRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_CLASS_SCHEDULE, EP_ORGAN_CLASS_SCHEDULE.ID, EpOrganClassSchedulePo.class);
    }

}
