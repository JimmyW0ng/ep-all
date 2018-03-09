package com.ep.domain.repository;

import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import com.ep.domain.repository.domain.tables.records.EpSystemRoleRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<EpSystemRolePo> findById(Long id) {
        EpSystemRolePo data = dslContext.selectFrom(EP_SYSTEM_ROLE)
                .where(EP_SYSTEM_ROLE.ID.eq(id))
                .and(EP_SYSTEM_ROLE.DEL_FLAG.equal(false))
                .fetchOneInto(EpSystemRolePo.class);
        return Optional.ofNullable(data);
    }

    /**
     * 根据id逻辑删除角色
     *
     * @param id
     * @return
     */
    public int deleteLogical(Long id) {
        return dslContext.update(EP_SYSTEM_ROLE)
                .set(EP_SYSTEM_ROLE.DEL_FLAG, true)
                .where(EP_SYSTEM_ROLE.ID.eq(id))
                .and(EP_SYSTEM_ROLE.DEL_FLAG.eq(false))
                .execute();
    }

    public List<EpSystemRolePo> getAllRoleByTarget(EpSystemRoleTarget target) {
        return dslContext.selectFrom(EP_SYSTEM_ROLE)
                .where(EP_SYSTEM_ROLE.TARGET.eq(target))
                .and(EP_SYSTEM_ROLE.DEL_FLAG.eq(false))
                .fetchInto(EpSystemRolePo.class);
    }

    /**
     * 根据角色编码获取角色
     *
     * @param roleCode
     * @return
     */
    public EpSystemRolePo getByCode(String roleCode) {
        return dslContext.selectFrom(EP_SYSTEM_ROLE)
                .where(EP_SYSTEM_ROLE.DEL_FLAG.equal(false))
                .and(EP_SYSTEM_ROLE.ROLE_CODE.equal(roleCode))
                .fetchOneInto(EpSystemRolePo.class);
    }

    /**
     * @param po
     */
    public int updatePo(EpSystemRolePo po) {
        return dslContext.update(EP_SYSTEM_ROLE)
                .set(EP_SYSTEM_ROLE.TARGET, po.getTarget())
                .set(EP_SYSTEM_ROLE.ROLE_NAME, po.getRoleName())
                .set(EP_SYSTEM_ROLE.ROLE_CODE, po.getRoleCode())
                .set(EP_SYSTEM_ROLE.REMARK, po.getRemark())
                .set(EP_SYSTEM_ROLE.UPDATE_BY, po.getUpdateBy())
                .where(EP_SYSTEM_ROLE.ID.eq(po.getId()))
                .and(EP_SYSTEM_ROLE.DEL_FLAG.eq(false))
                .execute();
    }
}

