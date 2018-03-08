package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpSystemUserType;
import com.ep.domain.service.SystemMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
public class SystemMenuController extends BackendController {
    @Autowired
    private SystemMenuService systemMenuService;


    /**
     * 菜单首页
     *
     * @return
     */
    @GetMapping("/index")
    public String index(Model model) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        List<EpSystemMenuPo> menuList = systemMenuService.getAllByUserType(currentUser.getType());
        model.addAttribute("menuList",menuList);
        return "systemMenu/index";
    }

    /**
     * 商家菜单首页
     *
     * @return
     */
    @GetMapping("/merchantIndex")
    public String merchantIndex(Model model) {
//        EpSystemUserPo currentUser = super.getCurrentUser().get();
        List<EpSystemMenuPo> menuList = systemMenuService.getAllByUserType(EpSystemUserType.merchant);
        model.addAttribute("menuList",menuList);
        return "systemMenu/merchantIndex";
    }

    /**
     * 新增/修改菜单
     * @param po
     * @return
     */
    @PostMapping("/create")
    @ResponseBody
    public ResultDo<String> create(EpSystemMenuPo po) {
        if (po.getId() == null) {
            //新增菜单
            return systemMenuService.createMenu(po);
        } else {
            //更新菜单
            return systemMenuService.updateMenu(po);
        }
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
        //父级菜单名称
        bo.setParentName(systemMenuService.getById(po.getParentId()).getMenuName());
        resultDo.setResult(bo);
        return resultDo;

    }

    /**
     * 删除菜单
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultDo delete(@RequestParam("ids[]") Long[] ids) {
        ResultDo resultDo = ResultDo.build();
        systemMenuService.deleteMenu(Arrays.asList(ids));
        return resultDo;
    }
}
