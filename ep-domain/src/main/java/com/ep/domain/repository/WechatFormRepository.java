package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatFormPo;
import com.ep.domain.repository.domain.enums.EpWechatFormBizType;
import com.ep.domain.repository.domain.tables.records.EpWechatFormRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
