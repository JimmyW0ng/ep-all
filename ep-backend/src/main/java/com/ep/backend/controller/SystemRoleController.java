package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemRoleBo;
import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.domain.enums.EpSystemRoleTarget;
import com.ep.domain.service.SystemRoleAuthorityService;
import com.ep.domain.service.SystemRoleService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * @Description: 系统角色控制器
 * @Author: CC.F
 * @Date: 17:01 2018/1/24
 */
@Slf4j
@Controller
@RequestMapping("auth/role")
@Api(value = "backend-auth-role", description = "系统角色")
public class SystemRoleController extends BackendController {

    @Autowired
    private SystemRoleService systemRoleService;

    @Autowired
    private SystemRoleAuthorityService systemRoleAuthorityService;

    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "roleName", required = false) String roleName,
                        @RequestParam(value = "roleCode", required = false) String roleCode,
                        @RequestParam(value = "target", required = false) String target
    ) {
        Map map= Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if(StringTools.isNotBlank(roleName)){
            conditions.add(EP.EP_SYSTEM_ROLE.ROLE_NAME.like("%" + roleName + "%"));
        }
        map.put("roleName",roleName);

        if(StringTools.isNotBlank(roleCode)){
            conditions.add(EP.EP_SYSTEM_ROLE.ROLE_CODE.like("%" + roleCode + "%"));
        }
        map.put("roleCode",roleCode);
        if(StringTools.isNotBlank(target)){
            conditions.add(EP.EP_SYSTEM_ROLE.TARGET.eq(EpSystemRoleTarget.valueOf(target)));
        }
        map.put("target",target);
//        if (null != crTimeStart) {
//
//            Condition condition = Changfu.CHANGFU.SYS_ROLE.CREATE_AT.greaterOrEqual(crStartTime);
//            collections.add(condition);
//        }
//        if (null != crEndTime) {
//            Condition condition = Changfu.CHANGFU.SYS_ROLE.CREATE_AT.lessOrEqual(crEndTime);
//            collections.add(condition);
//        }
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
    public String read(Model model,@PathVariable("id") Long id) {
        EpSystemRolePo systemRolePo = systemRoleService.getById(id);

        List<Long> menuIds=systemRoleAuthorityService.getMenuIdByRole(systemRolePo.getId());
        model.addAttribute("systemRolePo", systemRolePo);
        model.addAttribute("menuIds", menuIds);
        return "/systemRole/view";

    }

    /**
     * 新增角色初始化
     *
     * @return
     */
    @GetMapping("createInit")
    public String createInit(Model model) {
        model.addAttribute("systemRolePo", new EpSystemRolePo());
        return "/systemRole/form";

    }

    /**
     * 新增角色
     *
     * @return
     */
    @PostMapping("create")
    @ResponseBody
    public ResultDo create(HttpServletRequest request,
                           @RequestBody SystemRoleBo bo
//            ,
//                           EpSystemRolePo po, @RequestParam(value = "systemRoleAuthorityPos[]", required = false) EpSystemRoleAuthorityPo[] array
    ) {
        ResultDo resultDo = ResultDo.build();
        bo.setCreateBy(super.getCurrentUser(request).get().getId());
        List<EpSystemRoleAuthorityPo> systemRoleAuthorityPos=bo.getSystemRoleAuthorityPos();
        EpSystemRolePo systemRolePo=new EpSystemRolePo();
        BeanTools.copyPropertiesIgnoreNull(bo,systemRolePo);

        try {
            systemRoleService.createSystemRole(systemRolePo, systemRoleAuthorityPos);

        } catch (Exception e) {
            resultDo.setSuccess(false);
        }

        return resultDo;

    }

    /**
     * 修改角色
     *
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultDo update(HttpServletRequest request,
                           @RequestBody SystemRoleBo bo
    ) {
        ResultDo resultDo = ResultDo.build();
        bo.setUpdateAt(DateTools.getCurrentDateTime());
        bo.setUpdateBy(super.getCurrentUser(request).get().getId());
        List<EpSystemRoleAuthorityPo> systemRoleAuthorityPos=bo.getSystemRoleAuthorityPos();
        EpSystemRolePo systemRolePo=new EpSystemRolePo();
        BeanTools.copyPropertiesIgnoreNull(bo,systemRolePo);

        try {
            systemRoleService.updateSystemRole(systemRolePo, systemRoleAuthorityPos);

        } catch (Exception e) {
            resultDo.setSuccess(false);
        }

        return resultDo;

    }

    /**
     * 修改角色初始化
     *
     * @return
     */
    @GetMapping("updateInit/{id}")
    public String updateInit(Model model,@PathVariable("id") Long id) {
        EpSystemRolePo systemRolePo = systemRoleService.getById(id);

        List<Long> menuIds=systemRoleAuthorityService.getMenuIdByRole(systemRolePo.getId());
        model.addAttribute("systemRolePo", systemRolePo);
        model.addAttribute("menuIds", menuIds);
        return "/systemRole/form";

    }

    /**
     * 删除角色
     * @return
     */
    @GetMapping("delete/{id}")
    @ResponseBody
    public ResultDo deleteRole(@PathVariable("id") Long id){
        ResultDo resultDo = ResultDo.build();
//        try{
//            systemRoleService.delete(id);
//        }catch(Exception e){
//            resultDo.setSuccess(false);
//            return resultDo;
//        }
        return resultDo;
    }

}
