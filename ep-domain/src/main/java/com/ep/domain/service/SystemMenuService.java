package com.ep.domain.service;

import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.repository.SystemMenuRepository;
import com.ep.domain.repository.SystemRoleAuthorityRepository;
import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @Description: 菜单服务类
 * @Author: J.W
 * @Date: 上午9:30 2018/1/14
 */
@Slf4j
@Service
public class SystemMenuService {
    @Autowired
    private SystemMenuRepository systemMenuRepository;
    @Autowired
    private SystemRoleAuthorityRepository systemRoleAuthorityRepository;

    public EpSystemMenuPo getById(Long id) {
        return systemMenuRepository.findById(id);

    }

    /**
     * 根据用户类型获取菜单集合
     *
     * @param type
     * @return
     */
    public List<EpSystemMenuPo> getAllByUserType(EpSystemUserType type) {
        return systemMenuRepository.getAllByUserType(type);
    }

    /**
     * 根据角色对应目标获取菜单集合
     *
     * @param roleTarget
     * @return
     */
    public List<EpSystemMenuPo> getAllByRoleTarget(EpSystemRoleTarget roleTarget) {
        return systemMenuRepository.getAllByRoleTarget(roleTarget);
    }

    /**
     * 根据用户类型获取左部菜单
     *
     * @param type
     * @return
     */
    public List<SystemMenuBo> getLeftMenuByUserType(EpSystemUserType type, List<Long> roleIds) {
        List<SystemMenuBo> list;
        if (type.equals(EpSystemUserType.platform)) {
            //后台系统菜单
            list = systemMenuRepository.getAllLeftMenu(BizConstant.PLATFORM_MENU_PARENT_ID, roleIds);
        } else {
            //商户菜单
            list = systemMenuRepository.getAllLeftMenu(BizConstant.MERCHANT_MENU_PARENT_ID, roleIds);
        }
        //多个角色，重复菜单去重
        Set<Long> MenuSet = Sets.newHashSet();

        list.forEach(p -> {
            p.setChildList(systemMenuRepository.findByParentId(p.getId()));
        });
        return list;
    }

    /**
     * 新增菜单
     *
     * @param po
     * @return
     */
    public ResultDo createMenu(EpSystemMenuPo po) {
        log.info("[菜单]新增菜单开始，菜单对象={}。", po);
        if (null == po.getTarget() || null == po.getMenuType()
                || StringTools.isBlank(po.getMenuName()) || null == po.getSort()
                || StringTools.isBlank(po.getPermission())) {
            log.error("[菜单]新增菜单失败。请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        systemMenuRepository.insert(po);
        log.info("[菜单]新增菜单成功。id={}。", po.getId());
        return ResultDo.build();
    }

    /**
     * 更新菜单
     *
     * @param po
     * @return
     */
    public ResultDo updateMenu(EpSystemMenuPo po) {
        log.info("[菜单]修改菜单开始，菜单对象={}。", po);
        if (null == po.getId() || null == po.getParentId() || null == po.getMenuType()
                || StringTools.isBlank(po.getMenuName()) || null == po.getSort()
                || StringTools.isBlank(po.getPermission())) {
            log.error("[菜单]修改菜单失败。请求参数异常。");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        if (systemMenuRepository.updatePo(po) == BizConstant.DB_NUM_ONE) {
            log.info("[菜单]修改菜单成功。id={}。", po.getId());
            return ResultDo.build();
        } else {
            log.error("[菜单]修改菜单失败。id={}。", po.getId());
            return ResultDo.build(MessageCode.ERROR_OPERATE_FAIL);
        }
    }

    /**
     * 删除菜单
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDo deleteMenu(List<Long> ids) {
        log.info("[菜单]删除菜单开始。删除菜单ids={}。", ids);
        //逻辑删除 菜单表 记录
        systemMenuRepository.deleteLogicByIds(ids);
        //物理删除 角色权限表 记录
        systemRoleAuthorityRepository.deletePhysicByMenuIds(ids);
        log.info("[菜单]删除菜单成功。删除菜单ids={}。", ids);
        return ResultDo.build();
    }
}
