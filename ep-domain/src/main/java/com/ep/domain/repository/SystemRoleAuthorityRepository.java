package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.repository.domain.tables.records.EpSystemRoleAuthorityRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_ROLE_AUTHORITY;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:37 2018/1/24
 */
@Repository
public class SystemRoleAuthorityRepository extends AbstractCRUDRepository<EpSystemRoleAuthorityRecord, Long, EpSystemRoleAuthorityPo> {

    @Autowired
    public SystemRoleAuthorityRepository(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_ROLE_AUTHORITY, EP_SYSTEM_ROLE_AUTHORITY.ID, EpSystemRoleAuthorityPo.class);
    }


}
