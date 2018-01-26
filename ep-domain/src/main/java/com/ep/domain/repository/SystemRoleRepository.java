package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import com.ep.domain.repository.domain.tables.records.EpSystemRoleRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_ROLE;


@Repository
public class SystemRoleRepository extends AbstractCRUDRepository<EpSystemRoleRecord, Long, EpSystemRolePo> {

    @Autowired
    public SystemRoleRepository(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_ROLE, EP_SYSTEM_ROLE.ID, EpSystemRolePo.class);
    }

    /**
     * 根据ID获取角色po
     *
     * @param id
     * @return
     */
    public EpSystemRolePo getById(Long id) {
        return dslContext.selectFrom(EP_SYSTEM_ROLE)
                .where(EP_SYSTEM_ROLE.DEL_FLAG.equal(false))
                .and(EP_SYSTEM_ROLE.ID.equal(id))
                .fetchOneInto(EpSystemRolePo.class);
    }

    public int deleteLogical(Long id){
        return dslContext.update(EP_SYSTEM_ROLE)
                .set(EP_SYSTEM_ROLE.DEL_FLAG,true)
                .where(EP_SYSTEM_ROLE.ID.eq(id))
                .execute();
    }

    public List<EpSystemRolePo> getAllRoleByTarget(EpSystemRoleTarget target){
        return dslContext.selectFrom(EP_SYSTEM_ROLE)
                    .where(EP_SYSTEM_ROLE.TARGET.eq(target))
                .and(EP_SYSTEM_ROLE.DEL_FLAG.eq(false))
                .fetchInto(EpSystemRolePo.class);
    }
}

