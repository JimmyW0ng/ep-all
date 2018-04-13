package com.ep.backend.controller;

import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.service.OrganClassCatalogService;
import com.ep.domain.service.OrganClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * @Description: 班次目录控制器
 * @Author: CC.F
 * @Date: 9:30 2018/3/2
 */
@Slf4j
@Controller
@RequestMapping("auth/organClassCatalog")
public class OrganClassCatalogController extends BackendController {
    @Autowired
    private OrganClassCatalogService organClassCatalogService;
    @Autowired
    private OrganClassService organClassService;


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
        Long ognId = super.getCurrentUserOgnId();
        //班次必须属于该机构
        Optional<EpOrganClassPo> classOptional = organClassService.findById(classId);
        if (classOptional.isPresent() && classOptional.get().getOgnId().equals(ognId)) {
            return ResultDo.build().setResult(organClassCatalogService.findClassCatalogByClassId(classId));
        }
        return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
    }

}
