package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpTokenPo;
import com.ep.domain.repository.domain.enums.EpTokenType;
import com.ep.domain.repository.domain.tables.records.EpTokenRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_TOKEN;

/**
 * @Description:Token表Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class TokenRepository extends AbstractCRUDRepository<EpTokenRecord, Long, EpTokenPo> {

    @Autowired
    public TokenRepository(DSLContext dslContext) {
        super(dslContext, EP_TOKEN, EP_TOKEN.ID, EpTokenPo.class);
    }

    /**
     * 更新最近一次访问ip
     *
     * @param tokenId
     * @param ip
     * @return
     */
    public int updateLastAccessIpById(Long tokenId, String ip) {
        return dslContext.update(EP_TOKEN)
                         .set(EP_TOKEN.LAST_ACCESS_IP, ip)
                         .where(EP_TOKEN.ID.eq(tokenId))
                         .and(EP_TOKEN.DEL_FLAG.eq(false))
                         .execute();
    }

    /**
     * 删除无效token
     *
     * @param mobile
     * @param id
     * @param type
     */
    public int deleteByMobileAndId(Long mobile, Long id, EpTokenType type) {
        return dslContext.delete(EP_TOKEN)
                         .where(EP_TOKEN.MOBILE.eq(mobile))
                         .and(EP_TOKEN.ID.notEqual(id))
                         .and(EP_TOKEN.TYPE.eq(type))
                         .execute();
    }

    /**
     * 更新code
     *
     * @param token
     * @param id
     */
    public int updateCodeById(String token, Long id) {
        return dslContext.update(EP_TOKEN)
                         .set(EP_TOKEN.CODE, token)
                         .where(EP_TOKEN.ID.eq(id))
                         .and(EP_TOKEN.DEL_FLAG.eq(false))
                         .execute();
    }
}

