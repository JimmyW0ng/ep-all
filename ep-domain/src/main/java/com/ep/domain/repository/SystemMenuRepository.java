package com.ep.domain.repository;

import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.repository.domain.tables.records.EpSystemMenuRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_MENU;


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

    public List<EpSystemMenuPo> getAll() {
        return dslContext.selectFrom(EP_SYSTEM_MENU)
                .where(EP_SYSTEM_MENU.DEL_FLAG.equal(false))
                .fetchInto(EpSystemMenuPo.class);
    }

    public List<SystemMenuBo> getAllMenu(Long parentId) {
        return dslContext.selectFrom(EP_SYSTEM_MENU)
                .where(EP_SYSTEM_MENU.PARENT_ID.equal(parentId))
                .and(EP_SYSTEM_MENU.DEL_FLAG.equal(false))
                .fetchInto(SystemMenuBo.class);
    }
}

