package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatFormPo;
import com.ep.domain.repository.domain.tables.records.EpWechatFormRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
