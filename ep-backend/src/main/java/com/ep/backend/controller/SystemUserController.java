package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.common.tool.CryptTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemRoleBo;
import com.ep.domain.pojo.bo.SystemUserBo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import com.ep.domain.service.SystemRoleService;
import com.ep.domain.service.SystemUserRoleService;
import com.ep.domain.service.SystemUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * Created by fcc on 2018/1/10.
 */
@Slf4j
@RequestMapping("/auth/user")
@Controller
@Api(value = "后管用户")
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
     * 列表
     *
     * @return
     */
    @ApiOperation(value = "列表")
    @GetMapping("/index")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "mobile", required = false) String mobile,
                        @RequestParam(value = "type", required = false) String type
    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP.EP_SYSTEM_USER.MOBILE.eq(Long.parseLong(mobile)));
        }
        map.put("mobile", mobile);
        if (StringTools.isNotBlank(type)) {
            conditions.add(EP.EP_SYSTEM_USER.TYPE.eq(EpSystemUserType.valueOf(type)));
        }
        map.put("type", type);

//        if (null != crStartTime) {
//
//            Condition condition = Changfu.CHANGFU.CMS_ARTICLE.CREATE_AT.greaterOrEqual(crStartTime);
//            collections.add(condition);
//        }
//        if (null != crEndTime) {
//            Condition condition = Changfu.CHANGFU.CMS_ARTICLE.CREATE_AT.lessOrEqual(crEndTime);
//            collections.add(condition);
//        }

        Page<EpSystemUserPo> page = systemUserService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);


        return "systemUser/index";
    }

    /**
     * 新增用户初始化
     *
     * @return
     */
    @ApiOperation(value = "新增用户初始化")
    @GetMapping("/createInit")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
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
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String updateInit(Model model,@PathVariable("id") Long id) {
        EpSystemUserPo systemUserPo = systemUserService.getById(id);
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
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public ResultDo create(HttpServletRequest request,
                           @RequestBody SystemUserBo bo
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        ResultDo resultDo = ResultDo.build();
        EpSystemUserPo epSystemUserPo = new EpSystemUserPo();
        BeanTools.copyPropertiesIgnoreNull(bo, epSystemUserPo);
        epSystemUserPo.setSalt(StringTools.generateShortUrl(epSystemUserPo.getMobile(), passwordSaltKey, BizConstant.PASSWORD_SALT_MINLENGTH));
        try {
            epSystemUserPo.setPassword(CryptTools.aesEncrypt(epSystemUserPo.getPassword(), epSystemUserPo.getSalt()));

        } catch (Exception e) {
            resultDo.setSuccess(false);
            return resultDo;
        }
        EpSystemUserPo insertPo=systemUserService.createUser(epSystemUserPo,bo.getSystemRolePos());
        log.info("[用户]，新增用户成功，用户id={},currentUserId={}。", insertPo.getId(),currentUser.getId());

        return resultDo;
    }

    /**
     * 修改用户
     *
     * @return
     */
    @ApiOperation(value = "修改用户")
    @PostMapping("/update")
    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public ResultDo update(HttpServletRequest request,
                           @RequestBody SystemUserBo bo
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        ResultDo resultDo = ResultDo.build();
        EpSystemUserPo epSystemUserPo = new EpSystemUserPo();
        BeanTools.copyPropertiesIgnoreNull(bo, epSystemUserPo);
        epSystemUserPo.setSalt(StringTools.generateShortUrl(epSystemUserPo.getMobile(), passwordSaltKey, BizConstant.PASSWORD_SALT_MINLENGTH));
        try {
            epSystemUserPo.setPassword(CryptTools.aesEncrypt(epSystemUserPo.getPassword(), epSystemUserPo.getSalt()));

        } catch (Exception e) {
            resultDo.setSuccess(false);
            return resultDo;
        }
        systemUserService.updateUser(epSystemUserPo,bo.getSystemRolePos());
        log.info("[用户]，修改用户成功，用户id={},currentUserId={}。", bo.getId(),currentUser.getId());

        return resultDo;
    }

    /**
     * 查看用户
     *
     * @return
     */
    @ApiOperation(value = "查看用户")
    @GetMapping("/view/{id}")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String view(Model model, @PathVariable("id") Long id) {
        EpSystemUserPo systemUserPo = systemUserService.getById(id);
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
    @ApiOperation(value = "删除用户")
    @GetMapping("/delete/{id}")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    @ResponseBody
    public ResultDo delete(HttpServletRequest request,@PathVariable("id") Long id) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        ResultDo resultDo=ResultDo.build();
        systemUserService.deleteUser(id);
        log.info("[用户]，删除用户成功，用户id={},currentUserId={}。", id,currentUser.getId());

        return resultDo;
    }

    /**
     * 根据用户类型获得角色列表
     * @param type
     * @return
     */
    @GetMapping("/getRoleByUserType/{type}")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    @ResponseBody
    public ResultDo view(@PathVariable("type") EpSystemUserType type) {
        ResultDo resultDo = ResultDo.build();
        List<EpSystemRolePo> lists = systemRoleService.getAllRoleByUserType(type);
        resultDo.setResult(lists);
        return resultDo;
    }

}
