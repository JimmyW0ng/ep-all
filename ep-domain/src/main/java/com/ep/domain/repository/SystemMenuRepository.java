package com.ep.domain.repository;

import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.repository.domain.enums.EpSystemMenuStatus;
import com.ep.domain.repository.domain.enums.EpSystemMenuTarget;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import com.ep.domain.repository.domain.tables.records.EpSystemMenuRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_MENU;
import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_ROLE_AUTHORITY;


@Repository
public class SystemMenuRepository extends AbstractCRUDRepository<EpSystemMenuRecord, Long, EpSystemMenuPo> {

    @Autowired
    public SystemMenuRepository(DSLContext dslContext) {
        super(dslContext, EP_SYSTEM_MENU, EP_SYSTEM_MENU.ID, EpSystemMenuPo.class);
    }

    public EpSystemMenuPo findById(Long id) {
        return dslContext.selectFrom(EP_SYSTEM_MENU)
                .where(EP_SYSTEM_MENU.ID.equal(id))
                .and(EP_SYSTEM_MENU.DEL_FLAG.equal(false))
                .fetchOneInto(EpSystemMenuPo.class);
    }

    public List<EpSystemMenuPo> getAllByUserType(EpSystemUserType type) {
        if(type.equals(EpSystemUserType.platform)){
            return dslContext.selectFrom(EP_SYSTEM_MENU)
                    .where(EP_SYSTEM_MENU.DEL_FLAG.equal(false))
                    .and(EP_SYSTEM_MENU.TARGET.eq(EpSystemMenuTarget.admin))
                    .fetchInto(EpSystemMenuPo.class);
        }else{
            return dslContext.selectFrom(EP_SYSTEM_MENU)
                    .where(EP_SYSTEM_MENU.DEL_FLAG.equal(false))
                    .and(EP_SYSTEM_MENU.TARGET.eq(EpSystemMenuTarget.backend))
                    .fetchInto(EpSystemMenuPo.class);
        }
    }

    public List<SystemMenuBo> getAllMenu(Long parentId) {
        return dslContext.selectFrom(EP_SYSTEM_MENU)
                .where(EP_SYSTEM_MENU.PARENT_ID.equal(parentId))
                .and(EP_SYSTEM_MENU.DEL_FLAG.equal(false))
                .fetchInto(SystemMenuBo.class);
    }

    /**
     * 批量逻辑删除记录
     *
     * @param ids
     * @return
     */
    public int deleteLogicByIds(List<Long> ids) {
        return dslContext.update(EP_SYSTEM_MENU)
                .set(EP_SYSTEM_MENU.DEL_FLAG, true)
                .where(EP_SYSTEM_MENU.ID.in(ids))
                .execute();
    }

    public int updatePo(EpSystemMenuPo po) {
        return dslContext.update(EP_SYSTEM_MENU)
                .set(EP_SYSTEM_MENU.PARENT_ID, po.getId())
                .set(EP_SYSTEM_MENU.MENU_NAME, po.getMenuName())
                .set(EP_SYSTEM_MENU.MENU_TYPE, po.getMenuType())
                .set(EP_SYSTEM_MENU.HREF, po.getHref())
                .set(EP_SYSTEM_MENU.SORT, po.getSort())
                .set(EP_SYSTEM_MENU.STATUS, po.getStatus())
                .set(EP_SYSTEM_MENU.PERMISSION, po.getPermission())
                .set(EP_SYSTEM_MENU.REMARK, po.getRemark())
                .where(EP_SYSTEM_MENU.ID.eq(po.getId()))
                .execute();
    }

    /**
     * 根据角色id获取权限数组
     *
     * @param roleId
     * @return
     */
    public List<String> getByRoleId(Long roleId) {
        return dslContext.selectDistinct(EP_SYSTEM_MENU.PERMISSION)
                .from(EP_SYSTEM_MENU)
                .innerJoin(EP_SYSTEM_ROLE_AUTHORITY)
                .on(EP_SYSTEM_MENU.ID.eq(EP_SYSTEM_ROLE_AUTHORITY.MENU_ID))
                .and(EP_SYSTEM_ROLE_AUTHORITY.ROLE_ID.eq(roleId))
                .and(EP_SYSTEM_ROLE_AUTHORITY.DEL_FLAG.eq(false))
                .where(EP_SYSTEM_MENU.STATUS.eq(EpSystemMenuStatus.enable))
                .and(EP_SYSTEM_MENU.DEL_FLAG.eq(false))
                .fetchInto(String.class);
    }
}

