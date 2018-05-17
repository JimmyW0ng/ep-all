package com.ep.domain.repository;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.bo.WechatPayWithdrawBo;
import com.ep.domain.pojo.po.EpWechatPayWithdrawPo;
import com.ep.domain.repository.domain.enums.EpWechatPayWithdrawStatus;
import com.ep.domain.repository.domain.tables.records.EpWechatPayWithdrawRecord;
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

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 微信支付退款Repository
 * @Author: CC.F
 * @Date: 22:29 2018/5/16
 */
@Repository
public class WechatPayWithdrawRepository extends AbstractCRUDRepository<EpWechatPayWithdrawRecord, Long, EpWechatPayWithdrawPo> {

    @Autowired
    public WechatPayWithdrawRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_PAY_WITHDRAW, EP_WECHAT_PAY_WITHDRAW.ID, EpWechatPayWithdrawPo.class);
    }

    /**
     * 商户提现分页列表
     *
     * @param pageable
     * @param condition
     * @return
     */
    public Page<WechatPayWithdrawBo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        long totalCount = dslContext.selectCount()
                .from(EP_WECHAT_PAY_WITHDRAW)
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_WECHAT_PAY_WITHDRAW.COURSE_ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS.ID.eq(EP_WECHAT_PAY_WITHDRAW.CLASS_ID))
                .where(condition).fetchOne(0, Long.class);
        if (totalCount == BizConstant.DB_NUM_ZERO) {
            return new PageImpl<>(Lists.newArrayList(), pageable, totalCount);
        }
        List<Field<?>> fieldList = Lists.newArrayList(EP_WECHAT_PAY_WITHDRAW.fields());
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_ORGAN_CLASS.CLASS_NAME);

        SelectConditionStep<Record> record = dslContext.select(fieldList)
                .from(EP_WECHAT_PAY_WITHDRAW)
                .leftJoin(EP_ORGAN_COURSE).on(EP_ORGAN_COURSE.ID.eq(EP_WECHAT_PAY_WITHDRAW.COURSE_ID))
                .leftJoin(EP_ORGAN_CLASS).on(EP_ORGAN_CLASS.ID.eq(EP_WECHAT_PAY_WITHDRAW.CLASS_ID))
                .where(condition);

        List<WechatPayWithdrawBo> list = record.orderBy(getSortFields(pageable.getSort()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchInto(WechatPayWithdrawBo.class);
        PageImpl<WechatPayWithdrawBo> pPage = new PageImpl<WechatPayWithdrawBo>(list, pageable, totalCount);
        return pPage;
    }

    /**
     * 按班次统计提现成功订单数
     *
     * @param classId
     * @return
     */
    public int countPayWithdrawByClassId(Long classId) {
        return dslContext.select(DSL.ifnull(DSL.sum(EP_WECHAT_PAY_WITHDRAW.WECHAT_PAY_NUM), 0))
                .from(EP_WECHAT_PAY_WITHDRAW)
                .where(EP_WECHAT_PAY_WITHDRAW.CLASS_ID.eq(classId))
                .and(EP_WECHAT_PAY_WITHDRAW.STATUS.eq(EpWechatPayWithdrawStatus.finish))
                .and(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false))
                .fetchOneInto(Integer.class);
    }

    /**
     * 最近一次提现记录
     *
     * @param classId
     * @return
     */
    public EpWechatPayWithdrawPo getLastWithdrawByClassId(Long classId) {
        return dslContext.selectFrom(EP_WECHAT_PAY_WITHDRAW)
                .where(EP_WECHAT_PAY_WITHDRAW.CLASS_ID.eq(classId))
                .and(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false))
                .orderBy(EP_WECHAT_PAY_WITHDRAW.ID.desc())
                .limit(BizConstant.DB_NUM_ONE)
                .fetchOneInto(EpWechatPayWithdrawPo.class);
    }
}
