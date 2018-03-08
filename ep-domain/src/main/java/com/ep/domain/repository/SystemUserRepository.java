package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpSystemUserStatus;
import com.ep.domain.repository.domain.tables.records.EpSystemUserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    public Optional<EpSystemUserPo> findById(Long id) {
        EpSystemUserPo data = dslContext.selectFrom(EP_SYSTEM_USER)
                .where(EP_SYSTEM_USER.ID.equal(id))
                .and(EP_SYSTEM_USER.DEL_FLAG.equal(false))
                .fetchOneInto(EpSystemUserPo.class);
        return Optional.ofNullable(data);
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
     * 修改用户记录
     *
     * @return
     */
    public int updatePo(EpSystemUserPo po) {
        return dslContext.update(EP_SYSTEM_USER)
                .set(EP_SYSTEM_USER.MOBILE, po.getMobile())
                .set(EP_SYSTEM_USER.USER_NAME, po.getUserName())
                .set(EP_SYSTEM_USER.PASSWORD, po.getPassword())
                .set(EP_SYSTEM_USER.EMAIL, po.getEmail())
                .set(EP_SYSTEM_USER.EMAIL, po.getEmail())
//                .set(EP_SYSTEM_USER.TYPE,po.getType())
                .set(EP_SYSTEM_USER.OGN_ID, po.getOgnId())
                .where(EP_SYSTEM_USER.ID.eq(po.getId()))
                .and(EP_SYSTEM_USER.DEL_FLAG.eq(false))
                .execute();

    }

    /**
     * 根据机构id冻结平台用户
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

    /**
     * 根据机构id解冻平台用户
     *
     * @param ognId
     * @return
     */
    public int unfreezeByOgnId(Long ognId) {
        return dslContext.update(EP_SYSTEM_USER)
                .set(EP_SYSTEM_USER.STATUS, EpSystemUserStatus.normal)
                .where(EP_SYSTEM_USER.OGN_ID.eq(ognId))
                .and(EP_SYSTEM_USER.STATUS.eq(EpSystemUserStatus.freeze))
                .and(EP_SYSTEM_USER.DEL_FLAG.eq(false))
                .execute();
    }


    /**
     * 根据id冻结平台用户
     *
     * @param id
     * @return
     */
    public int freezeById(Long id) {
        return dslContext.update(EP_SYSTEM_USER)
                .set(EP_SYSTEM_USER.STATUS, EpSystemUserStatus.freeze)
                .where(EP_SYSTEM_USER.ID.eq(id))
                .and(EP_SYSTEM_USER.STATUS.eq(EpSystemUserStatus.normal))
                .and(EP_SYSTEM_USER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 根据id解冻平台用户
     *
     * @param id
     * @return
     */
    public int unfreezeById(Long id) {
        return dslContext.update(EP_SYSTEM_USER)
                .set(EP_SYSTEM_USER.STATUS, EpSystemUserStatus.normal)
                .where(EP_SYSTEM_USER.ID.eq(id))
                .and(EP_SYSTEM_USER.STATUS.eq(EpSystemUserStatus.freeze))
                .and(EP_SYSTEM_USER.DEL_FLAG.eq(false))
                .execute();
    }


    /**
     * 根据id注销平台用户
     *
     * @param id
     * @return
     */
    public int cancelById(Long id) {
        return dslContext.update(EP_SYSTEM_USER)
                .set(EP_SYSTEM_USER.STATUS, EpSystemUserStatus.cancel)
                .where(EP_SYSTEM_USER.ID.eq(id))
                .and(EP_SYSTEM_USER.STATUS.eq(EpSystemUserStatus.normal))
                .and(EP_SYSTEM_USER.DEL_FLAG.eq(false))
                .execute();
    }

    /**
     * 机构下线,机构对应平台账号注销
     *
     * @param ognId
     * @return
     */
    public int cancelByOfflineOgn(Long ognId) {
        return dslContext.update(EP_SYSTEM_USER)
                .set(EP_SYSTEM_USER.STATUS, EpSystemUserStatus.cancel)
                .where(EP_SYSTEM_USER.OGN_ID.eq(ognId))
                .and(EP_SYSTEM_USER.STATUS.in(EpSystemUserStatus.normal, EpSystemUserStatus.freeze))
                .and(EP_SYSTEM_USER.DEL_FLAG.eq(false))
                .execute();
    }
}

