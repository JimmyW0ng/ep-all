package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.repository.domain.tables.records.EpSystemRoleAuthorityRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_ROLE_AUTHORITY;

/**
 * @Description:角色权限标Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class SysRoleAuthorityRepository extends AbstractCRUDRepository<EpSystemRoleAuthorityRecord, Long, EpSystemRoleAuthorityPo> {

    @Autowired
    public SysRoleAuthorityRepository(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_ROLE_AUTHORITY, EP_SYSTEM_ROLE_AUTHORITY.ID, EpSystemRoleAuthorityPo.class);
    }

    public List<String> getAuthoritesByRole(String... role) {
        return dslContext.selectDistinct(EP_SYSTEM_ROLE_AUTHORITY.AUTHORITY)
                .from(EP_SYSTEM_ROLE_AUTHORITY)
                .where(EP_SYSTEM_ROLE_AUTHORITY.ROLE.in(role))
                .and(EP_SYSTEM_ROLE_AUTHORITY.DEL_FLAG.eq(false))
                .fetchInto(String.class);
    }
}
