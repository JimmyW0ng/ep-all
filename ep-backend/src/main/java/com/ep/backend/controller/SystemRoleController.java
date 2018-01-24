package com.ep.backend.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpSystemRoleAuthorityPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.service.SystemRoleAuthorityService;
import com.ep.domain.service.SystemRoleService;
import com.google.common.collect.Lists;
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
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
//                        @RequestParam(value = "title", required = false) String name,
    ) {
        Collection<Condition> conditions = Lists.newArrayList();
        Page<EpSystemRolePo> page = systemRoleService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        return "systemRole/index";
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
    public ResultDo create(HttpServletRequest request, EpSystemRolePo po, @RequestParam(value = "authority[]", required = false) String[] array) {
        ResultDo resultDo = ResultDo.build();
        po.setCreateBy(super.getCurrentUser(request).get().getId());
        List<EpSystemRoleAuthorityPo> list = Lists.newArrayList();
        for (int i = 0; i < array.length; i++) {
            list.add(new EpSystemRoleAuthorityPo(null, po.getRoleCode(), array[i], null, null, null, null, null));
        }
        try {
            systemRoleService.createSystemRole(po, list);

        } catch (Exception e) {
            resultDo.setSuccess(false);
        }

        return resultDo;

    }

}
