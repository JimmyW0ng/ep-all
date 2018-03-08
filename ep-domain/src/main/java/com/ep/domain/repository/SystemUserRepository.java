package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpSystemUserStatus;
import com.ep.domain.repository.domain.tables.records.EpSystemUserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_USER;

/**
 * @Description: 系统用户仓库
 * @Author: CC.F
 * @Date: 11:19 2018/1/29
 */
@Repository
public class SystemUserRepository extends AbstractCRUDRepository<EpSystemUserRecord, Long, EpSystemUserPo> {

    @Autowired
    public SystemUserRepository(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_USER, EP_SYSTEM_USER.ID, EpSystemUserPo.class);
    }

    public EpSystemUserPo findById(Long id) {
        return dslContext.selectFrom(EP_SYSTEM_USER)
                .where(EP_SYSTEM_USER.ID.equal(id))
                .and(EP_SYSTEM_USER.DEL_FLAG.equal(false))
                .fetchOneInto(EpSystemUserPo.class);
    }

    /**
     * 根据手机号获取用户
     *
     * @param mobile
     * @return
     */
    public EpSystemUserPo getByMobile(Long mobile) {
        return dslContext.selectFrom(EP_SYSTEM_USER)
                .where(EP_SYSTEM_USER.MOBILE.equal(mobile)).fetchOneInto(EpSystemUserPo.class);
    }

    public int deleteLogical(Long userId){
        return dslContext.update(EP_SYSTEM_USER)
                .set(EP_SYSTEM_USER.DEL_FLAG,true)
                .where(EP_SYSTEM_USER.ID.eq(userId))
                .execute();

    }

    /**
     * 根据机构id冻结账号
     *
     * @param ognId
     * @return
     */
    public int freezeByOgnId(Long ognId) {
        return dslContext.update(EP_SYSTEM_USER)
                .set(EP_SYSTEM_USER.STATUS, EpSystemUserStatus.freeze)
                .where(EP_SYSTEM_USER.OGN_ID.eq(ognId))
                .and(EP_SYSTEM_USER.STATUS.eq(EpSystemUserStatus.normal))
                .and(EP_SYSTEM_USER.DEL_FLAG.eq(false))
                .execute();
    }
}

