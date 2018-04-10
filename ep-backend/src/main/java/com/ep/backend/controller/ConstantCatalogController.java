package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.ConstantCatalogBo;
import com.ep.domain.pojo.po.EpConstantCatalogPo;
import com.ep.domain.service.ConstantCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 产品类目控制器
 * @Author: CC.F
 * @Date: 18:06 2018/2/5
 */
@Slf4j
@Controller
@RequestMapping("auth/catalog")
public class ConstantCatalogController extends BackendController {
    @Autowired
    private ConstantCatalogService constantCatalogService;

    /**
     * 列表
     *
     * @param model
     * @return
     */
    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('platform:catalog:index')")
    public String index(Model model){
        List<EpConstantCatalogPo> catalogList = constantCatalogService.getAll();
        model.addAttribute("catalogList",catalogList);
        return "catalog/index";
    }

    /**
     * 新增/修改
     * @param po
     * @return
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('platform:catalog:index')")
    @ResponseBody
    public ResultDo create(EpConstantCatalogPo po) {
        if (po.getId() == null) {//新增产品类目
            return constantCatalogService.createConstantCatalog(po);
        } else {
            //更新产品类目
            return constantCatalogService.updateConstantCatalog(po);
        }
    }

    /**
     * 查看详情
     *
     * @return
     */
    @GetMapping("/view/{id}")
    @PreAuthorize("hasAnyAuthority('platform:catalog:index')")
    @ResponseBody
    public ResultDo<ConstantCatalogBo> viewAjax(@PathVariable(value = "id") Long id) {
        ResultDo<ConstantCatalogBo> resultDo = ResultDo.build();
        ConstantCatalogBo bo = new ConstantCatalogBo();

        EpConstantCatalogPo po = constantCatalogService.getById(id);
        BeanTools.copyPropertiesIgnoreNull(po, bo);
        bo.setParentName(constantCatalogService.getById(po.getParentId()).getLabel());
        resultDo.setResult(bo);
        return resultDo;

    }

    /**
     * 删除类目
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @PreAuthorize("hasAnyAuthority('platform:catalog:index')")
    @ResponseBody
    public ResultDo delete(@RequestParam("ids[]") Long[] ids) {
        return constantCatalogService.delete(ids);
    }
}
