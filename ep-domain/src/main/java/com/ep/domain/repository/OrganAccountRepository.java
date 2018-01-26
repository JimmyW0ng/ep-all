package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.repository.domain.tables.records.EpOrganAccountRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_ACCOUNT;
import static com.ep.domain.repository.domain.Tables.EP_ORGAN_COURSE_TEAM;

/**
 * @Description:机构后台账户表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class OrganAccountRepository extends AbstractCRUDRepository<EpOrganAccountRecord, Long, EpOrganAccountPo> {

    @Autowired
    public OrganAccountRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_ACCOUNT, EP_ORGAN_ACCOUNT.ID, EpOrganAccountPo.class);
    }

    /**
     * 根据手机号获取机构账户
     *
     * @param mobile
     * @return
     */
    public List<EpOrganAccountPo> getByMobile(Long mobile) {
        return dslContext.selectFrom(EP_ORGAN_ACCOUNT)
                .where(EP_ORGAN_ACCOUNT.REFER_MOBILE.eq(mobile))
                .and(EP_ORGAN_ACCOUNT.DEL_FLAG.eq(false))
                .fetchInto(EpOrganAccountPo.class);
    }


    /**
     * 获取课程班次
     *
     * @param courseId
     * @return
     */
    public List<EpOrganAccountPo> getByCourseId(Long courseId) {
        return dslContext.select(EP_ORGAN_ACCOUNT.fields())
                .from(EP_ORGAN_ACCOUNT)
                .innerJoin(EP_ORGAN_COURSE_TEAM)
                .on(EP_ORGAN_ACCOUNT.ID.eq(EP_ORGAN_COURSE_TEAM.OGN_ACCOUNT_ID))
                .and(EP_ORGAN_COURSE_TEAM.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_COURSE_TEAM.DEL_FLAG.eq(false))
                .where(EP_ORGAN_ACCOUNT.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_COURSE_TEAM.SORT.desc(), EP_ORGAN_COURSE_TEAM.ID.asc())
                .fetchInto(EpOrganAccountPo.class);
    }

}

