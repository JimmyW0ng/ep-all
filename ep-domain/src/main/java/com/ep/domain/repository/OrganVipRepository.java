package com.ep.domain.repository;

import com.ep.common.tool.DateTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.OrganVipBo;
import com.ep.domain.pojo.po.EpOrganVipPo;
import com.ep.domain.repository.domain.tables.records.EpOrganVipRecord;
import com.google.common.collect.Lists;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 机构会员信息表
 * @Author J.W
 * @Date: 下午 3:29 2018/3/16 0016
 */
@Repository
public class OrganVipRepository extends AbstractCRUDRepository<EpOrganVipRecord, Long, EpOrganVipPo> {

    @Autowired
    public OrganVipRepository(DSLContext dslContext) {
        super(dslContext, EP_ORGAN_VIP, EP_ORGAN_VIP.ID, EpOrganVipPo.class);
    }

    /**
     * 根据机构和孩子判断是否是会员
     *
     * @param ognId
     * @param childId
     * @return
     */
    public Boolean existVipByOgnIdAndChildId(Long ognId, Long childId) {
        Timestamp now = DateTools.getCurrentDateTime();
        Long count = dslContext.selectCount().from(EP_ORGAN_VIP)
                               .where(EP_ORGAN_VIP.OGN_ID.eq(ognId))
                               .and(EP_ORGAN_VIP.CHILD_ID.eq(childId))
                               .and(EP_ORGAN_VIP.START_TIME.lessThan(now))
                               .and(EP_ORGAN_VIP.END_TIME.greaterThan(now))
                               .and(EP_ORGAN_VIP.DEL_FLAG.eq(false))
                               .fetchOneInto(Long.class);
        return count > BizConstant.LONG_ZERO;
    }

    /**
     * 分页查询机构会员列表
     *
     * @param pageable
     * @return
     */
    public Page<OrganVipBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_ORGAN_VIP)
                .leftJoin(EP_MEMBER).on(EP_ORGAN_VIP.MEMBER_ID.eq(EP_MEMBER.ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_ORGAN_VIP.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_ORGAN_VIP.fields());
        fieldList.add(EP_MEMBER.MOBILE);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_ORGAN_VIP)
                .leftJoin(EP_MEMBER).on(EP_ORGAN_VIP.MEMBER_ID.eq(EP_MEMBER.ID))
                .leftJoin(EP_MEMBER_CHILD).on(EP_ORGAN_VIP.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .where(condition);

        List<OrganVipBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(OrganVipBo.class);
        PageImpl<OrganVipBo> pPage = new PageImpl<OrganVipBo>(list, pageable, totalCount);
        return pPage;
    }

}
