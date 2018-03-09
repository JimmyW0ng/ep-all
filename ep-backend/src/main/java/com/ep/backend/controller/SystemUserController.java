package com.ep.backend.controller;

import com.ep.common.tool.CryptTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemUserBo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import com.ep.domain.service.SystemRoleService;
import com.ep.domain.service.SystemUserRoleService;
import com.ep.domain.service.SystemUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * @Description: 用户控制器
 * @Author: CC.F
 * @Date:
 */
@Slf4j
@RequestMapping("/auth/user")
@Controller
public class SystemUserController extends BackendController {

    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemUserRoleService systemUserRoleService;
    @Value("${password.salt.key}")
    private String passwordSaltKey;

    /**
     * 用户列表
     *
     * @return
     */
    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "mobile", required = false) String mobile,
                        @RequestParam(value = "type", required = false) String type,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP.EP_SYSTEM_USER.MOBILE.eq(Long.parseLong(mobile)));
        }
        searchMap.put("mobile", mobile);
        if (StringTools.isNotBlank(type)) {
            conditions.add(EP.EP_SYSTEM_USER.TYPE.eq(EpSystemUserType.valueOf(type)));
        }
        searchMap.put("type", type);

        if (null != crStartTime) {
            conditions.add(EP.EP_SYSTEM_USER.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP.EP_SYSTEM_USER.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP.EP_SYSTEM_USER.DEL_FLAG.eq(false));
        Page<EpSystemUserPo> page = systemUserService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "systemUser/index";
    }

    /**
     * 新增用户初始化
     *
     * @return
     */
    @GetMapping("/createInit")
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    public String createInit(Model model) {
        model.addAttribute("systemUserPo", new EpSystemUserPo());
        return "/systemUser/form";
    }

    /**
     * 修改用户初始化
     *
     * @return
     */
    @ApiOperation(value = "修改用户初始化")
    @GetMapping("/updateInit/{id}")
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    public String updateInit(Model model,@PathVariable("id") Long id) {
        EpSystemUserPo systemUserPo = systemUserService.findById(id).get();
        List<Long> roleIds = systemUserRoleService.getRoleIdsByUserId(id);
        List<EpSystemRolePo> lists = systemRoleService.getAllRoleByUserType(systemUserPo.getType());
        try{
            systemUserPo.setPassword(CryptTools.aesDecrypt(systemUserPo.getPassword(),systemUserPo.getSalt()));

        }catch(Exception e){
            model.addAttribute("systemUserPo", systemUserPo);
            model.addAttribute("roleList", lists);
            model.addAttribute("roleIds", roleIds);
            return "/systemUser/form";
        }
        model.addAttribute("systemUserPo", systemUserPo);
        model.addAttribute("roleList", lists);
        model.addAttribute("roleIds", roleIds);
        return "/systemUser/form";
    }

    /**
     * 新增用户
     *
     * @return
     */
    @ApiOperation(value = "新增用户")
    @PostMapping("/create")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    public ResultDo create(@RequestBody SystemUserBo bo
    ) {
        try {
            return systemUserService.createUser(bo);
        } catch (Exception e) {
            log.error("[用户]，新增失败。", e);
            return ResultDo.build(MessageCode.ERROR_SYSTEM);
        }
    }

    /**
     * 修改用户
     *
     * @return
     */
    @ApiOperation(value = "修改用户")
    @PostMapping("/update")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    public ResultDo update(@RequestBody SystemUserBo bo
    ) {
        try {
            return systemUserService.updateUser(bo);
        } catch (Exception e) {
            log.error("[用户]，修改失败。id={}。", bo.getId(), e);
            return ResultDo.build(MessageCode.ERROR_SYSTEM);
        }
    }

    /**
     * 查看用户
     *
     * @return
     */
    @ApiOperation(value = "查看用户")
    @GetMapping("/view/{id}")
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    public String view(Model model, @PathVariable("id") Long id) {
        EpSystemUserPo systemUserPo = systemUserService.findById(id).get();
        List<Long> roleIds = systemUserRoleService.getRoleIdsByUserId(id);
        List<EpSystemRolePo> lists = systemRoleService.getAllRoleByUserType(systemUserPo.getType());
        try{
            systemUserPo.setPassword(CryptTools.aesDecrypt(systemUserPo.getPassword(),systemUserPo.getSalt()));

        }catch(Exception e){
            model.addAttribute("systemUserPo", systemUserPo);
            model.addAttribute("roleList", lists);
            model.addAttribute("roleIds", roleIds);
            return "/systemUser/view";
        }
        model.addAttribute("systemUserPo", systemUserPo);
        model.addAttribute("roleList", lists);
        model.addAttribute("roleIds", roleIds);
        return "/systemUser/view";
    }

    /**
     * 删除用户
     *
     * @return
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    @ResponseBody
    public ResultDo delete(@PathVariable("id") Long id) {
        EpSystemUserPo systemUserPo = super.getCurrentUser().get();

        ResultDo resultDo = ResultDo.build();
        systemUserService.deleteUser(id);
        log.info("[用户]，删除用户成功，用户id={},currentUserId={}。", id, systemUserPo.getId());
        return resultDo;
    }

    /**
     * 冻结用户
     *
     * @param id
     * @return
     */
    @GetMapping("freeze/{id}")
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    @ResponseBody
    public ResultDo freeze(@PathVariable("id") Long id) {
        return systemUserService.freezeById(id);
    }

    /**
     * 注销用户
     *
     * @param id
     * @return
     */
    @GetMapping("cancel/{id}")
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    @ResponseBody
    public ResultDo cancel(@PathVariable("id") Long id) {
        return systemUserService.cancelById(id);
    }

    /**
     * 解冻用户
     *
     * @param id
     * @return
     */
    @GetMapping("unfreeze/{id}")
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    @ResponseBody
    public ResultDo unfreeze(@PathVariable("id") Long id) {
        return systemUserService.unfreezeById(id);
    }

    /**
     * 根据用户类型获得角色列表
     *
     * @param type
     * @return
     */
    @GetMapping("/getRoleByUserType/{type}")
    @PreAuthorize("hasAnyAuthority('backend:user:index')")
    @ResponseBody
    public ResultDo view(@PathVariable("type") EpSystemUserType type) {
        ResultDo resultDo = ResultDo.build();
        List<EpSystemRolePo> lists = systemRoleService.getAllRoleByUserType(type);
        resultDo.setResult(lists);
        return resultDo;
    }

}
