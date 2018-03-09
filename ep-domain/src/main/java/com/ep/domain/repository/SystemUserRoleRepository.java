package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemUserRolePo;
import com.ep.domain.repository.domain.tables.records.EpSystemUserRoleRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_USER_ROLE;

/**
 * @Description:系统用户-角色Repository
 * @Author: CC.F
 * @Date: 15:34 2018/1/26
 */
@Repository
public class SystemUserRoleRepository extends AbstractCRUDRepository<EpSystemUserRoleRecord, Long, EpSystemUserRolePo> {

    @Autowired
    public SystemUserRoleRepository(DSLContext dslContext) {
            super(dslContext, EP_SYSTEM_USER_ROLE, EP_SYSTEM_USER_ROLE.ID, EpSystemUserRolePo.class);
    }


    public List<Long> getRoleIdsByUserId(Long userId){
        return dslContext.select(EP_SYSTEM_USER_ROLE.ROLE_ID)
                .from(EP_SYSTEM_USER_ROLE)
                .where(EP_SYSTEM_USER_ROLE.USER_ID.eq(userId))
                .fetchInto(Long.class);
    }

    public void deleteByUserIdAndRoleId(Long userId,Long roleId){
        dslContext.delete(EP_SYSTEM_USER_ROLE)
                .where(EP_SYSTEM_USER_ROLE.USER_ID.eq(userId))
                .and(EP_SYSTEM_USER_ROLE.ROLE_ID.eq(roleId))
                .execute();
    }

    public void deleteLogicByUserId(Long userId) {
        dslContext.delete(EP_SYSTEM_USER_ROLE)
                .where(EP_SYSTEM_USER_ROLE.USER_ID.eq(userId))
                .execute();
    }

    public void deletePhysicByRoleId(Long roleId) {
        dslContext.delete(EP_SYSTEM_USER_ROLE)
                .where(EP_SYSTEM_USER_ROLE.ROLE_ID.eq(roleId))
                .execute();
    }
}
