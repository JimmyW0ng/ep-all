package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpWechatOpenidPo;
import com.ep.domain.repository.domain.enums.EpWechatOpenidType;
import com.ep.domain.repository.domain.tables.records.EpWechatOpenidRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ep.domain.repository.domain.Tables.EP_WECHAT_OPENID;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 17:28 2018/5/3/003
 */
@Repository
public class WechatOpenidRepository extends AbstractCRUDRepository<EpWechatOpenidRecord, Long, EpWechatOpenidPo> {

    @Autowired
    public WechatOpenidRepository(DSLContext dslContext) {
        super(dslContext, EP_WECHAT_OPENID, EP_WECHAT_OPENID.ID, EpWechatOpenidPo.class);
    }

    public Optional<EpWechatOpenidPo> getByOpenidAndType(String openid, EpWechatOpenidType type) {
        EpWechatOpenidPo data = dslContext.selectFrom(EP_WECHAT_OPENID)
                .where(EP_WECHAT_OPENID.OPENID.eq(openid))
                .and(EP_WECHAT_OPENID.TYPE.eq(type))
                .fetchOneInto(EpWechatOpenidPo.class);
        return Optional.ofNullable(data);
    }

    public Optional<EpWechatOpenidPo> getByMobileAndType(Long mobile, EpWechatOpenidType type) {
        EpWechatOpenidPo data = dslContext.selectFrom(EP_WECHAT_OPENID)
                .where(EP_WECHAT_OPENID.MOBILE.eq(mobile))
                .and(EP_WECHAT_OPENID.TYPE.eq(type))
                .fetchOneInto(EpWechatOpenidPo.class);
        return Optional.ofNullable(data);
    }

}
