package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.domain.tables.records.EpSystemRoleRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
                .where(EP_SYSTEM_ROLE.DEL_FLAG.equal(false)).fetchOneInto(EpSystemRolePo.class);
    }
}

