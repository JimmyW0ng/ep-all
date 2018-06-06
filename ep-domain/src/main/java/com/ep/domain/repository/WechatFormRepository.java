package com.ep.domain.repository;

import com.ep.domain.pojo.bo.XcxMessageTemplateOrderBo;
import com.ep.domain.pojo.po.EpWechatFormPo;
import com.ep.domain.repository.domain.enums.EpWechatFormBizType;
import com.ep.domain.repository.domain.tables.records.EpWechatFormRecord;
import com.google.common.collect.Lists;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.*;
import static com.ep.domain.repository.domain.tables.EpOrder.EP_ORDER;
import static com.ep.domain.repository.domain.tables.EpOrganClass.EP_ORGAN_CLASS;
import static com.ep.domain.repository.domain.tables.EpWechatForm.EP_WECHAT_FORM;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:44 2018/5/31
 */
@Repository
public class WechatFormRepository extends AbstractCRUDRepository<EpWechatFormRecord, Long, EpWechatFormPo> {

    @Autowired
    public WechatFormRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_FORM, EP_WECHAT_FORM.ID, EpWechatFormPo.class);
    }

    /**
     * 根据业务id和业务类型获取记录
     *
     * @param sourceIds
     * @param bizType
     * @return
     */
    public List<EpWechatFormPo> findBySourceIdsAndBizType(List<Long> sourceIds, EpWechatFormBizType bizType) {
        return dslContext.selectFrom(EP_WECHAT_FORM)
                .where(EP_WECHAT_FORM.SOURCE_ID.in(sourceIds))
                .and(EP_WECHAT_FORM.BIZ_TYPE.eq(bizType))
                .and(EP_WECHAT_FORM.DEL_FLAG.eq(false))
                .fetchInto(EpWechatFormPo.class);
    }

    public List<XcxMessageTemplateOrderBo> findXcxMessageTemplateOrderBo(List<Long> sourceIds, EpWechatFormBizType bizType) {
        List<Field<?>> fieldList = Lists.newArrayList();
        fieldList.add(EP_WECHAT_FORM.SOURCE_ID.as("orderId"));
        fieldList.add(EP_WECHAT_FORM.TOUSER);
        fieldList.add(EP_WECHAT_FORM.FORM_ID);
        fieldList.add(EP_WECHAT_FORM.EXPIRE_TIME);
        fieldList.add(EP_ORGAN_COURSE.COURSE_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME);
        fieldList.add(EP_MEMBER_CHILD.CHILD_NICK_NAME);
        fieldList.add(EP_ORGAN_CLASS.ADDRESS);
        fieldList.add(EP_ORGAN_ACCOUNT.NICK_NAME.as("accountNickName"));
        fieldList.add(EP_ORGAN_ACCOUNT.REFER_MOBILE);
        fieldList.add(EP_ORGAN_CLASS_CATALOG.START_TIME);
        return dslContext.select(fieldList).from(EP_WECHAT_FORM)
                .innerJoin(EP_ORDER).on(EP_WECHAT_FORM.SOURCE_ID.eq(EP_ORDER.ID))
                .innerJoin(EP_ORGAN_COURSE).on(EP_ORDER.COURSE_ID.eq(EP_ORGAN_COURSE.ID))
                .innerJoin(EP_MEMBER_CHILD).on(EP_ORDER.CHILD_ID.eq(EP_MEMBER_CHILD.ID))
                .innerJoin(EP_ORGAN_CLASS).on(EP_ORDER.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .innerJoin(EP_ORGAN_ACCOUNT).on(EP_ORGAN_ACCOUNT.ID.eq(EP_ORGAN_CLASS.OGN_ACCOUNT_ID))
                .innerJoin(EP_ORGAN_CLASS_CATALOG).on(EP_ORGAN_CLASS_CATALOG.CLASS_ID.eq(EP_ORGAN_CLASS.ID))
                .where(EP_WECHAT_FORM.SOURCE_ID.in(sourceIds))
                .and(EP_WECHAT_FORM.BIZ_TYPE.eq(bizType))
                .and(EP_WECHAT_FORM.DEL_FLAG.eq(false))
                .groupBy(EP_ORGAN_CLASS_CATALOG.CLASS_ID)
                .fetchInto(XcxMessageTemplateOrderBo.class);
    }
}
