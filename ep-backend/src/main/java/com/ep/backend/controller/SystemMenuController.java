package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.SystemRoleAuthorityRepository;
import com.ep.domain.repository.domain.enums.EpSystemMenuTarget;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import com.ep.domain.service.SystemMenuService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 菜单管理控制器
 * @Author: CC.F
 * @Date: 16:31 2018/1/20
 */
@Slf4j
@RequestMapping("auth/menu")
@Controller
@Api(value = "菜单", description = "菜单")
public class SystemMenuController extends BackendController {
    @Autowired
    private SystemMenuService systemMenuService;
    @Autowired
    private SystemRoleAuthorityRepository systemRoleAuthorityRepository;


    /**
     * 菜单首页
     *
     * @return
     */
    @GetMapping("/index")
    public String index(HttpServletRequest request,Model model) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        List<EpSystemMenuPo> menuList = systemMenuService.getAllByUserType(currentUser.getType());
        model.addAttribute("menuList",menuList);
        return "systemMenu/index";
    }

    /**
     * 新增/修改菜单
     * @param request
     * @param po
     * @return
     */
    @PostMapping("/create")
    @ResponseBody
    public ResultDo<String> create(HttpServletRequest request,EpSystemMenuPo po) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        if(currentUser.getType().equals(EpSystemUserType.platform)){
            po.setTarget(EpSystemMenuTarget.admin);
        }else{
            po.setTarget(EpSystemMenuTarget.backend);
        }
        ResultDo<String> resultDo = ResultDo.build();
        if (po.getId() == null) {//新增菜单

            EpSystemMenuPo insertPo=systemMenuService.insert(po);
            log.info("[菜单]，新增菜单成功，菜单id={},currentUserId={}。", insertPo.getId(),currentUser.getId());
            return resultDo;

        }
        //更新菜单
        systemMenuService.update(po);
        log.info("[菜单]，修改菜单成功，菜单id={},currentUserId={}。", po.getId(),currentUser.getId());
        return resultDo;

    }

    /**
     * 查看菜单
     *
     * @return
     */
    @GetMapping("/view/{id}")
    @ResponseBody
    public ResultDo<SystemMenuBo> viewAjax(@PathVariable(value = "id") Long id) {
        ResultDo<SystemMenuBo> resultDo = ResultDo.build();
        SystemMenuBo bo = new SystemMenuBo();

        EpSystemMenuPo po = systemMenuService.getById(id);
        BeanTools.copyPropertiesIgnoreNull(po, bo);
        bo.setParentName(systemMenuService.getById(po.getParentId()).getMenuName());
        resultDo.setResult(bo);
        return resultDo;

    }

    /**
     * 删除菜单
     * @param request
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultDo delete(HttpServletRequest request, @RequestParam("ids[]") Long[] ids) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        ResultDo resultDo = ResultDo.build();
        for(int i=0;i<ids.length;i++){
            systemMenuService.delete(ids[i]);
            log.info("[菜单]，删除菜单成功，菜单id={},currentUserId={}。", ids[i].toString(),currentUser.getId());
        }

        return resultDo;
    }


}
