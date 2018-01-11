package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemClientPo;
import com.ep.domain.repository.domain.tables.records.EpSystemClientRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.tables.EpSystemClient.EP_SYSTEM_CLIENT;

/**
 * @Description:鉴权Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class SystemClientRepository extends AbstractCRUDRepository<EpSystemClientRecord, Long, EpSystemClientPo> {

    @Autowired
    public SystemClientRepository(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_CLIENT, EP_SYSTEM_CLIENT.ID, EpSystemClientPo.class);
    }

    /**
     * 根据clientid获取数据
     *
     * @param clientId
     * @return
     */
    public EpSystemClientPo getByClientId(String clientId) {
        return dslContext.selectFrom(EP_SYSTEM_CLIENT)
                .where(EP_SYSTEM_CLIENT.CLIENT_ID.eq(clientId))
                .and(EP_SYSTEM_CLIENT.DEL_FLAG.eq(false))
                .fetchOneInto(EpSystemClientPo.class);
    }

}

