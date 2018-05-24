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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    public Optional<EpWechatPayWithdrawPo> findById(Long id) {
        EpWechatPayWithdrawPo data = dslContext.selectFrom(EP_WECHAT_PAY_WITHDRAW)
                .where(EP_WECHAT_PAY_WITHDRAW.ID.eq(id))
                .and(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false))
                .fetchOneInto(EpWechatPayWithdrawPo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 微信支付提现平台分页
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

    /**
     * 审核通过提现申请
     *
     * @param id
     * @return
     */
    public int submitPayWithdrawById(Long id) {
        return dslContext.update(EP_WECHAT_PAY_WITHDRAW)
                .set(EP_WECHAT_PAY_WITHDRAW.STATUS, EpWechatPayWithdrawStatus.submit)
                .where(EP_WECHAT_PAY_WITHDRAW.ID.eq(id))
                .and(EP_WECHAT_PAY_WITHDRAW.STATUS.eq(EpWechatPayWithdrawStatus.wait))
                .and(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 审核通过提现申请
     *
     * @param id
     * @return
     */
    public int finishPayWithdrawById(Long id, String outWithdrawNo, String payId, BigDecimal withdrawFee, Timestamp paidTime) {
        return dslContext.update(EP_WECHAT_PAY_WITHDRAW)
                .set(EP_WECHAT_PAY_WITHDRAW.STATUS, EpWechatPayWithdrawStatus.finish)
                .set(EP_WECHAT_PAY_WITHDRAW.OUT_WITHDRAW_NO, outWithdrawNo)
                .set(EP_WECHAT_PAY_WITHDRAW.PAY_ID, payId)
                .set(EP_WECHAT_PAY_WITHDRAW.WITHDRAW_FEE, withdrawFee)
                .set(EP_WECHAT_PAY_WITHDRAW.PAID_TIME, paidTime)
                .where(EP_WECHAT_PAY_WITHDRAW.ID.eq(id))
                .and(EP_WECHAT_PAY_WITHDRAW.STATUS.eq(EpWechatPayWithdrawStatus.submit))
                .and(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 拒绝提现申请
     *
     * @param id
     * @return
     */
    public int refusePayWithdrawById(Long id, String remark) {
        return dslContext.update(EP_WECHAT_PAY_WITHDRAW)
                         .set(EP_WECHAT_PAY_WITHDRAW.STATUS, EpWechatPayWithdrawStatus.refuse)
                         .set(EP_WECHAT_PAY_WITHDRAW.REMARK, remark)
                         .where(EP_WECHAT_PAY_WITHDRAW.ID.eq(id))
                         .and(EP_WECHAT_PAY_WITHDRAW.STATUS.in(EpWechatPayWithdrawStatus.wait, EpWechatPayWithdrawStatus.submit))
                         .and(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 根据班次id获取提现记录
     *
     * @param classId
     * @return
     */
    public List<EpWechatPayWithdrawPo> findByClassId(Long classId) {
        return dslContext.selectFrom(EP_WECHAT_PAY_WITHDRAW)
                .where(EP_WECHAT_PAY_WITHDRAW.CLASS_ID.eq(classId))
                .and(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false))
                .fetchInto(EpWechatPayWithdrawPo.class);
    }

    /**
     * 根据班次id和状态获取提现记录
     *
     * @param classId
     * @return
     */
    public List<Long> findIdsByClassIdAndStatus(Long classId, EpWechatPayWithdrawStatus... statuses) {
        return dslContext.select(EP_WECHAT_PAY_WITHDRAW.ID)
                .from(EP_WECHAT_PAY_WITHDRAW)
                .where(EP_WECHAT_PAY_WITHDRAW.CLASS_ID.eq(classId))
                .and(EP_WECHAT_PAY_WITHDRAW.STATUS.in(statuses))
                .and(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false))
                .fetchInto(Long.class);
    }

}
