package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.repository.domain.tables.records.EpOrganAccountRecord;
import com.google.common.collect.Lists;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
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
     * 加锁
     *
     * @param id
     */
    public EpOrganAccountPo getByIdForLock(Long id) {
        return dslContext.selectFrom(EP_ORGAN_ACCOUNT)
                .where(EP_ORGAN_ACCOUNT.ID.eq(id))
                .forUpdate()
                .fetchOneInto(EpOrganAccountPo.class);
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
                .orderBy(EP_ORGAN_ACCOUNT.CREATE_AT.asc())
                .fetchInto(EpOrganAccountPo.class);
    }

    /**
     * 根据手机号和机构id获取机构账户
     *
     * @param mobile
     * @param ognId
     * @return
     */
    public Optional<EpOrganAccountPo> getByMobileAndOgnId(Long mobile, Long ognId) {
        EpOrganAccountPo accountPo = dslContext.selectFrom(EP_ORGAN_ACCOUNT)
                .where(EP_ORGAN_ACCOUNT.REFER_MOBILE.eq(mobile))
                .and(EP_ORGAN_ACCOUNT.OGN_ID.eq(ognId))
                .and(EP_ORGAN_ACCOUNT.DEL_FLAG.eq(false))
                .fetchOneInto(EpOrganAccountPo.class);
        return Optional.ofNullable(accountPo);
    }


    /**
     * 获取课程团队
     *
     * @param courseId
     * @return
     */
    public List<OrganAccountBo> getByCourseId(Long courseId) {
        return dslContext.select(EP_ORGAN_ACCOUNT.fields())
                .from(EP_ORGAN_ACCOUNT)
                .innerJoin(EP_ORGAN_COURSE_TEAM)
                .on(EP_ORGAN_ACCOUNT.ID.eq(EP_ORGAN_COURSE_TEAM.OGN_ACCOUNT_ID))
                .and(EP_ORGAN_COURSE_TEAM.COURSE_ID.eq(courseId))
                .and(EP_ORGAN_COURSE_TEAM.DEL_FLAG.eq(false))
                .where(EP_ORGAN_ACCOUNT.DEL_FLAG.eq(false))
                .orderBy(EP_ORGAN_COURSE_TEAM.SORT.desc(), EP_ORGAN_COURSE_TEAM.ID.asc())
                .fetchInto(OrganAccountBo.class);
    }

    /**
     * 商户获取后台教师分页列表
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganAccountBo> merchantFindbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_ORGAN_ACCOUNT)
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }

        List<OrganAccountBo> list = dslContext
                .selectFrom(EP_ORGAN_ACCOUNT)
                .where(condition)
                .orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganAccountBo.class);
        PageImpl<OrganAccountBo> pPage = new PageImpl<OrganAccountBo>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 平台获取后台教师分页列表
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<OrganAccountBo> platformFindbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_ORGAN_ACCOUNT)
                .leftJoin(EP_ORGAN).on(EP_ORGAN_ACCOUNT.OGN_ID.eq(EP_ORGAN.ID))
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_ACCOUNT.fields());
        fieldList.add(EP_ORGAN.OGN_NAME);
        List<OrganAccountBo> list = dslContext
                .select(fieldList).from(EP_ORGAN_ACCOUNT)
                .leftJoin(EP_ORGAN).on(EP_ORGAN_ACCOUNT.OGN_ID.eq(EP_ORGAN.ID))
                .where(condition)
                .orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganAccountBo.class);
        PageImpl<OrganAccountBo> pPage = new PageImpl<OrganAccountBo>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 更新教师
     *
     * @param po
     * @return
     */
    public int updateById(EpOrganAccountPo po) {
        return dslContext.update(EP_ORGAN_ACCOUNT)
                .set(EP_ORGAN_ACCOUNT.ACCOUNT_NAME, po.getAccountName())
                .set(EP_ORGAN_ACCOUNT.NICK_NAME, po.getNickName())
                .set(EP_ORGAN_ACCOUNT.INTRODUCE, po.getIntroduce())
                .set(EP_ORGAN_ACCOUNT.REFER_MOBILE, po.getReferMobile())
                .set(EP_ORGAN_ACCOUNT.STATUS, po.getStatus())
                .set(EP_ORGAN_ACCOUNT.REMARK, po.getRemark())
                .set(EP_ORGAN_ACCOUNT.UPDATE_AT, DSL.currentTimestamp())
                .where(EP_ORGAN_ACCOUNT.ID.eq(po.getId()))
                .execute();
    }

    /**
     * 删除教师
     *
     * @param id
     * @return
     */
    public int deleteLogical(Long id) {
        return dslContext.update(EP_ORGAN_ACCOUNT)
                .set(EP_ORGAN_ACCOUNT.DEL_FLAG, true)
                .where(EP_ORGAN_ACCOUNT.ID.eq(id))
                .execute();
    }

    public List<EpOrganAccountPo> findByOgnId(Long ognId){
        return dslContext.selectFrom(EP_ORGAN_ACCOUNT)
                .where(EP_ORGAN_ACCOUNT.OGN_ID.eq(ognId))
                .and(EP_ORGAN_ACCOUNT.DEL_FLAG.eq(false))
                .fetchInto(EpOrganAccountPo.class);
    }

}

