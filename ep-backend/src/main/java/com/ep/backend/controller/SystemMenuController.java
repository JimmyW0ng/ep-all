package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemMenuPo;
import com.ep.domain.service.SystemMenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 菜单管理控制器
 * @Author: CC.F
 * @Date: 16:31 2018/1/20
 */
@RequestMapping("auth/menu")
@Controller
@Api(value = "菜单", description = "菜单")
public class SystemMenuController {
    @Autowired
    private SystemMenuService systemMenuService;

    /**
     * 菜单首页
     *
     * @return
     */
    @GetMapping("/index")
    public String index() {
        return "systemMenu/index";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultDo<String> create(EpSystemMenuPo po) {
        ResultDo<String> resultDo = ResultDo.build();
        if (po.getId() == null) {
            EpSystemMenuPo insertPo = systemMenuService.insert(po);
            return insertPo != null ? resultDo : ResultDo.build(MessageCode.ERROR_SYSTEM);
        }
        EpSystemMenuPo updatePo = new EpSystemMenuPo();
        systemMenuService.update(po);


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


}
