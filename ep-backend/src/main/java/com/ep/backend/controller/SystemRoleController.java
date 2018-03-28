package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemRoleBo;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import com.ep.domain.service.SystemMenuService;
import com.ep.domain.service.SystemRoleAuthorityService;
import com.ep.domain.service.SystemRoleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * @Description: 系统角色控制器
 * @Author: CC.F
 * @Date: 17:01 2018/1/24
 */
@Slf4j
@Controller
@RequestMapping("auth/role")
public class SystemRoleController extends BackendController {

    @Autowired
    private SystemRoleService systemRoleService;

    @Autowired
    private SystemRoleAuthorityService systemRoleAuthorityService;

    @Autowired
    private SystemMenuService systemMenuService;

    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('platform:role:index')")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "roleName", required = false) String roleName,
                        @RequestParam(value = "roleCode", required = false) String roleCode,
                        @RequestParam(value = "target", required = false) String target,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(roleName)) {
            conditions.add(EP.EP_SYSTEM_ROLE.ROLE_NAME.like("%" + roleName + "%"));
        }
        map.put("roleName", roleName);

        if (StringTools.isNotBlank(roleCode)) {
            conditions.add(EP.EP_SYSTEM_ROLE.ROLE_CODE.like("%" + roleCode + "%"));
        }
        map.put("roleCode", roleCode);
        if (StringTools.isNotBlank(target)) {
            conditions.add(EP.EP_SYSTEM_ROLE.TARGET.eq(EpSystemRoleTarget.valueOf(target)));
        }
        map.put("target", target);
        if (null != crStartTime) {
            conditions.add(EP.EP_SYSTEM_ROLE.CREATE_AT.greaterOrEqual(crStartTime));
        }
        map.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP.EP_SYSTEM_ROLE.CREATE_AT.lessOrEqual(crEndTime));
        }
        map.put("crEndTime", crEndTime);
        conditions.add(EP.EP_SYSTEM_ROLE.DEL_FLAG.eq(false));

        Page<EpSystemRolePo> page = systemRoleService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "systemRole/index";
    }

    /**
     * 查看角色
     *
     * @return
     */
    @GetMapping("view/{id}")
    @PreAuthorize("hasAnyAuthority('platform:role:index')")
    public String read(Model model, @PathVariable("id") Long id) {
        Optional<EpSystemRolePo> systemRolePoOptional = systemRoleService.findById(id);
        if (systemRolePoOptional.isPresent()) {
            model.addAttribute("systemRolePo", systemRolePoOptional.get());
            //拥有权限的菜单
            List<Long> menuIds = systemRoleAuthorityService.getMenuIdByRole(systemRolePoOptional.get().getId());
            model.addAttribute("menuIds", menuIds);
        }

        //所有菜单
        List<EpSystemMenuPo> menuList = systemMenuService.getAllByRoleTarget(systemRolePoOptional.get().getTarget());
        model.addAttribute("menuList", menuList);
        return "systemRole/view";

    }

    /**
     * 新增角色初始化
     *
     * @return
     */
    @GetMapping("createInit")
    @PreAuthorize("hasAnyAuthority('platform:role:index')")
    public String createInit(Model model) {
        model.addAttribute("systemRolePo", new EpSystemRolePo());
        //所有菜单,默认商户菜单
        List<EpSystemMenuPo> menuList = systemMenuService.getAllByRoleTarget(EpSystemRoleTarget.backend);
        model.addAttribute("menuList", menuList);
        return "systemRole/form";

    }

    /**
     * 新增角色
     *
     * @return
     */
    @PostMapping("create")
    @PreAuthorize("hasAnyAuthority('platform:role:index')")
    @ResponseBody
    public ResultDo create(@RequestBody SystemRoleBo bo
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        bo.setCreateBy(currentUser.getId());

        return systemRoleService.createSystemRole(bo);


    }

    /**
     * 根据角色目标切换菜单
     *
     * @return
     */
    @GetMapping("getMenuByRoleTarget/{target}")
    @PreAuthorize("hasAnyAuthority('platform:role:index')")
    @ResponseBody
    public ResultDo<List<EpSystemMenuPo>> getMenuByRoleTarget(@PathVariable("target") String target) {
        ResultDo<List<EpSystemMenuPo>> resultDo = ResultDo.build();
        List<EpSystemMenuPo> menuList = systemMenuService.getAllByRoleTarget(EpSystemRoleTarget.valueOf(target));
        return resultDo.setResult(menuList);
    }

    /**
     * 修改角色
     *
     * @return
     */
    @PostMapping("update")
    @PreAuthorize("hasAnyAuthority('platform:role:index')")
    @ResponseBody
    public ResultDo update(@RequestBody SystemRoleBo bo
    ) {
        bo.setUpdateBy(super.getCurrentUser().get().getId());
        return systemRoleService.updateSystemRole(bo);


    }

    /**
     * 修改角色初始化
     *
     * @return
     */
    @GetMapping("updateInit/{id}")
    @PreAuthorize("hasAnyAuthority('platform:role:index')")
    public String updateInit(Model model, @PathVariable("id") Long id) {
        Optional<EpSystemRolePo> systemRolePoOptional = systemRoleService.findById(id);
        if (systemRolePoOptional.isPresent()) {
            List<Long> menuIds = systemRoleAuthorityService.getMenuIdByRole(systemRolePoOptional.get().getId());
            model.addAttribute("systemRolePo", systemRolePoOptional.get());
            model.addAttribute("menuIds", menuIds);
        }

        List<EpSystemMenuPo> menuList = systemMenuService.getAllByRoleTarget(systemRolePoOptional.get().getTarget());
        model.addAttribute("menuList", menuList);
        return "systemRole/form";

    }

    /**
     * 删除角色
     *
     * @return
     */
    @GetMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('platform:role:index')")
    @ResponseBody
    public ResultDo deleteRole(HttpServletRequest request, @PathVariable("id") Long id) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        ResultDo resultDo = ResultDo.build();
        systemRoleService.deleteRole(id);
        log.info("[角色]，角色删除成功，角色id={},currentUserId={}。", id, currentUser.getId());

        return resultDo;
    }

}
