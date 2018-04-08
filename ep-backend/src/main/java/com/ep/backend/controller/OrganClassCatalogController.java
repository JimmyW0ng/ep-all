package com.ep.backend.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.service.OrganClassCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 班次目录控制器
 * @Author: CC.F
 * @Date: 9:30 2018/3/2
 */
@Controller
@RequestMapping("auth/organClassCatalog")
public class OrganClassCatalogController extends BackendController {
    @Autowired
    private OrganClassCatalogService organClassCatalogService;


    /**
     * 查看该班次下班次目录
     *
     * @param classId
     * @return
     */
    @GetMapping("findClassCatalog/{classId}")
    @PreAuthorize("hasAnyAuthority('merchant:organClass:index')")
    @ResponseBody
    public ResultDo findClassChild(@PathVariable("classId") Long classId) {
        return ResultDo.build().setResult(organClassCatalogService.findClassCatalogByClassId(classId));
    }

}
