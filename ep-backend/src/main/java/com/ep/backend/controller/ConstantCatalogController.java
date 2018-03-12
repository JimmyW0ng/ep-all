package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.ConstantCatalogBo;
import com.ep.domain.pojo.po.EpConstantCatalogPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.ConstantCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 课程类目控制器
 * @Author: CC.F
 * @Date: 18:06 2018/2/5
 */
@Slf4j
@Controller
@RequestMapping("auth/catalog")
public class ConstantCatalogController extends BackendController {
    @Autowired
    private ConstantCatalogService constantCatalogService;

    @GetMapping("index")
    public String index(Model model){
        List<EpConstantCatalogPo> catalogList = constantCatalogService.getAll();
        model.addAttribute("catalogList",catalogList);
        return "catalog/index";
    }

    /**
     * 新增/修改
     * @param request
     * @param po
     * @return
     */
    @PostMapping("/create")
    @ResponseBody
    public ResultDo<String> create(HttpServletRequest request, EpConstantCatalogPo po) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();

        ResultDo<String> resultDo = ResultDo.build();
        if (po.getId() == null) {//新增课程类目

            EpConstantCatalogPo insertPo=constantCatalogService.insert(po);
            log.info("[课程类目]，新增课程类目成功，课程类目={},currentUserId={}。", insertPo.getId(),currentUser.getId());
            return resultDo;

        }
        //更新课程类目
        constantCatalogService.update(po);
        log.info("[课程类目]，修改课程类目成功，课程类目id={},currentUserId={}。", po.getId(),currentUser.getId());
        return resultDo;

    }

    /**
     * 查看详情
     *
     * @return
     */
    @GetMapping("/view/{id}")
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
     * 删除
     * @param request
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultDo delete(HttpServletRequest request, @RequestParam("ids[]") Long[] ids) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        ResultDo resultDo = ResultDo.build();
        for(int i=0;i<ids.length;i++){
            constantCatalogService.delete(ids[i]);
            log.info("[菜单]，删除菜单成功，菜单id={},currentUserId={}。", ids[i].toString(),currentUser.getId());
        }

        return resultDo;
    }
}
