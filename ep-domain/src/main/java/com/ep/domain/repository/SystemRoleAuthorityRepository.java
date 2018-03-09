package com.ep.domain.repository;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.domain.tables.records.EpSystemRoleAuthorityRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.ep.domain.repository.domain.Tables.*;
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

    public List<String> getAuthoritesByPrincipal(String principal) {
        Long userId=dslContext.select(EP_SYSTEM_USER.ID)
                .from(EP_SYSTEM_USER)
                .where(EP_SYSTEM_USER.MOBILE.eq(Long.parseLong(principal)))
                .fetchOneInto(Long.class);
       return dslContext.select(EP_SYSTEM_MENU.PERMISSION).from(EP_SYSTEM_ROLE_AUTHORITY)
                .leftJoin(EP_SYSTEM_MENU).on(EP_SYSTEM_ROLE_AUTHORITY.MENU_ID.eq(EP_SYSTEM_MENU.ID))
                .leftJoin(EP_SYSTEM_USER_ROLE).on(EP_SYSTEM_ROLE_AUTHORITY.ROLE_ID.eq(EP_SYSTEM_USER_ROLE.ROLE_ID))
                .where(EP_SYSTEM_USER_ROLE.USER_ID.eq(userId))
                .and(EP_SYSTEM_MENU.PERMISSION.isNotNull())
                .groupBy(EP_SYSTEM_ROLE_AUTHORITY.MENU_ID)
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

    public void deletePhysicByRoleId(Long roleId) {
        dslContext.delete(EP_SYSTEM_ROLE_AUTHORITY)
                .where(EP_SYSTEM_ROLE_AUTHORITY.ROLE_ID.eq(roleId))
                .execute();
    }

    public void deletePhysicByMenuIds(List<Long> ids) {
        dslContext.delete(EP_SYSTEM_ROLE_AUTHORITY)
                .where(EP_SYSTEM_ROLE_AUTHORITY.MENU_ID.in(ids))
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
