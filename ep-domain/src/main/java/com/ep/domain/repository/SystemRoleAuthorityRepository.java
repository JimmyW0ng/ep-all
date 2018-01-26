package com.ep.domain.repository;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.domain.tables.records.EpSystemRoleAuthorityRecord;
import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_ROLE_AUTHORITY;
import static com.ep.domain.repository.domain.tables.EpSystemRole.EP_SYSTEM_ROLE;

/**
 * @Description:角色权限标Repository
 * @Author: J.W
 * @Date: 上午10:35 2017/11/27
 */
@Repository
public class SystemRoleAuthorityRepository extends AbstractCRUDRepository<EpSystemRoleAuthorityRecord, Long, EpSystemRoleAuthorityPo> {

    @Autowired
    public SystemRoleAuthorityRepository(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_ROLE_AUTHORITY, EP_SYSTEM_ROLE_AUTHORITY.ID, EpSystemRoleAuthorityPo.class);
    }

    public List<String> getAuthoritesByRole(String... role) {
        return dslContext.selectDistinct(EP_SYSTEM_ROLE_AUTHORITY.AUTHORITY)
                .from(EP_SYSTEM_ROLE_AUTHORITY)
                .where(EP_SYSTEM_ROLE_AUTHORITY.ROLE.in(role))
                .and(EP_SYSTEM_ROLE_AUTHORITY.DEL_FLAG.eq(false))
                .fetchInto(String.class);
    }

    public List<Long> getMenuIdByRole(Long roleId) {
        return dslContext.select(EP_SYSTEM_ROLE_AUTHORITY.MENU_ID)
                .from(EP_SYSTEM_ROLE_AUTHORITY)
                .where(EP_SYSTEM_ROLE_AUTHORITY.ROLE_ID.eq(roleId))
                .fetchInto(Long.class);
    }

    public void deleteByRoleIdAndMenuId(Long roleId, Long menuId) {
        dslContext.delete(EP_SYSTEM_ROLE_AUTHORITY)
                .where(EP_SYSTEM_ROLE_AUTHORITY.ROLE_ID.eq(roleId))
                .and(EP_SYSTEM_ROLE_AUTHORITY.MENU_ID.eq(menuId))
                .execute();
    }

    public void deleteByRoleId(Long roleId) {
        dslContext.delete(EP_SYSTEM_ROLE_AUTHORITY)
                .where(EP_SYSTEM_ROLE_AUTHORITY.ROLE_ID.eq(roleId))
                .execute();
    }

    public boolean isMenuUseByMenu(Long menuId) {
        return dslContext.selectCount().from(EP_SYSTEM_ROLE_AUTHORITY)
                .where(EP_SYSTEM_ROLE_AUTHORITY.MENU_ID.eq(menuId))
                .fetchOne(0, Long.class) > 0 ? true : false;
    }

    public List<EpSystemRolePo> getRoleByUseMenu(Long menuId){
        List<Long> list=dslContext.selectDistinct(EP_SYSTEM_ROLE_AUTHORITY.ROLE_ID)
                .where(EP_SYSTEM_ROLE_AUTHORITY.MENU_ID.eq(menuId))
                .fetchInto(Long.class);
        if(CollectionsTools.isEmpty(list)){
            return new ArrayList<EpSystemRolePo>();
        }else{
            return dslContext.selectFrom(EP_SYSTEM_ROLE)
                    .where(EP_SYSTEM_ROLE.DEL_FLAG.eq(false))
                    .and(EP_SYSTEM_ROLE.ID.in(list))
                    .fetchInto(EpSystemRolePo.class);
        }

    }
}
