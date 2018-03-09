package com.ep.domain.service;

import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemRoleBo;
import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.SystemRoleAuthorityRepository;
import com.ep.domain.repository.SystemRoleRepository;
import com.ep.domain.repository.SystemUserRoleRepository;
import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Description: 系统角色服务类
 * @Author: CC.F
 * @Date: 17:10 2018/1/24
 */
@Slf4j
@Service
public class SystemRoleService {
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    @Autowired
    private SystemRoleAuthorityRepository systemRoleAuthorityRepository;
    @Autowired
    private SystemUserRoleRepository systemUserRoleRepository;

    public Optional<EpSystemRolePo> findById(Long id) {
        return systemRoleRepository.findById(id);
    }

    public Page<EpSystemRolePo> findbyPageAndCondition(Pageable pageable, Collection<? extends Condition> condition) {
        return systemRoleRepository.findByPageable(pageable, condition);
    }

    /**
     * @param type
     * @return
     */
    public List<EpSystemRolePo> getAllRoleByUserType(EpSystemUserType type) {
        if (type.equals(EpSystemUserType.merchant)) {
            return systemRoleRepository.getAllRoleByTarget(EpSystemRoleTarget.backend);
        } else if (type.equals(EpSystemUserType.platform)) {
            return systemRoleRepository.getAllRoleByTarget(EpSystemRoleTarget.admin);

        } else {
            return systemRoleRepository.getAllRoleByTarget(null);
        }
    }

    /**
     * 新增角色
     *
     * @param bo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo createSystemRole(SystemRoleBo bo) {
        log.info("[角色]新增角色开始。角色对象={}。", bo);
        if (null == bo.getTarget() || StringTools.isBlank(bo.getRoleName()) || StringTools.isBlank(bo.getRoleCode())
                ) {
            log.error("[角色]新增角色失败，请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        List<EpSystemRoleAuthorityPo> list = bo.getSystemRoleAuthorityPos();
        EpSystemRolePo po = new EpSystemRolePo();
        this.copyBoPropertyToPo(bo, po);
        systemRoleRepository.insert(po);
        list.forEach(p -> {
            p.setRoleId(po.getId());
        });
        systemRoleAuthorityRepository.insert(list);
        log.info("[角色]新增角色成功。id={}。", po.getId());
        return ResultDo.build();
    }

    /**
     * 修改角色
     *
     * @param bo
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo updateSystemRole(SystemRoleBo bo) {
        log.info("[角色]修改角色开始。角色对象={}。", bo);
        if (null == bo.getId() || null == bo.getTarget()
                || StringTools.isBlank(bo.getRoleName()) || StringTools.isBlank(bo.getRoleCode())
                ) {
            log.error("[角色]修改角色失败，请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        if (!systemRoleRepository.findById(bo.getId()).isPresent()) {
            log.error("[角色]修改角色失败，该角色不存在。id={}", bo.getId());
            return ResultDo.build(MessageCode.ERROR_SYSTEM_ROLE_NOT_EXISTS);
        }
        EpSystemRolePo po = new EpSystemRolePo();

        po.setUpdateBy(bo.getUpdateBy());
        this.copyBoPropertyToPo(bo, po);

        List<EpSystemRoleAuthorityPo> list = bo.getSystemRoleAuthorityPos();
        if (systemRoleRepository.updatePo(po) == BizConstant.DB_NUM_ONE) {
            //角色权限表删除记录
            systemRoleAuthorityRepository.deletePhysicByRoleId(po.getId());
            list.forEach(p -> {
                p.setRoleId(po.getId());
            });
            //角色权限表插入记录
            systemRoleAuthorityRepository.insert(list);
            log.info("[角色]修改角色成功。id={}。", po.getId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        } else {
            log.error("[角色]修改角色失败。");
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo deleteRole(Long id) {
        log.info("[角色]删除角色开始。id={}。", id);
        if (systemRoleRepository.deleteLogical(id) == BizConstant.DB_NUM_ONE) {
            systemUserRoleRepository.deletePhysicByRoleId(id);
            systemRoleAuthorityRepository.deletePhysicByRoleId(id);
            log.info("[角色]删除角色成功。id={}。", id);
            return ResultDo.build();
        } else {
            log.error("[角色]删除角色失败。id={}。", id);
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    private void copyBoPropertyToPo(SystemRoleBo bo, EpSystemRolePo po) {
        if (bo.getId() != null) {
            po.setId(bo.getId());
        }
        po.setTarget(bo.getTarget());
        po.setRoleName(StringTools.getNullIfBlank(bo.getRoleName()));
        po.setRoleCode(StringTools.getNullIfBlank(bo.getRoleCode()));
        po.setRemark(StringTools.getNullIfBlank(bo.getRemark()));
    }
}
