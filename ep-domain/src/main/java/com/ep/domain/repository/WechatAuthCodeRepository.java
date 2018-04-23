package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatAuthCodePo;
import com.ep.domain.repository.domain.tables.records.EpWechatAuthCodeRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_WECHAT_AUTH_CODE;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:35 2018/4/23
 */
@Repository
public class WechatAuthCodeRepository extends AbstractCRUDRepository<EpWechatAuthCodeRecord, Long, EpWechatAuthCodePo> {

    @Autowired
    public WechatAuthCodeRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_AUTH_CODE, EP_WECHAT_AUTH_CODE.ID, EpWechatAuthCodePo.class);
    }
}
