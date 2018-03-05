package com.ep.backend.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpConstantCatalogPo;
import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.ConstantCatalogService;
import com.ep.domain.service.ConstantTagService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: 标签控制器
 * @Author: CC.F
 * @Date: 16:53 2018/2/23
 */
@Slf4j
@Controller
@RequestMapping("auth/constantTag")
public class ConstantTagController extends BackendController {
    @Autowired
    private ConstantTagService constantTagService;
    @Autowired
    private ConstantCatalogService constantCatalogService;

    @GetMapping("merchantIndex")
    public String index(HttpServletRequest request, Model model) {
        List<EpConstantCatalogPo> constantCatalogPos = constantCatalogService.findSecondCatalog();
        model.addAttribute("constantCatalogPos", constantCatalogPos);
        return "/constantTag/merchantIndex";
    }

    /**
     * 根据类目获得标签
     * @param request
     * @param catalogId
     * @return
     */
    @GetMapping("findTags")
    @ResponseBody
    public ResultDo findTags(
            HttpServletRequest request,
            @RequestParam(value = "catalogId") Long catalogId
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        ResultDo resultDo = ResultDo.build();
        Map<String, Object> map = Maps.newHashMap();
        //公用标签
        List<EpConstantTagPo> constantTag = constantTagService.findByCatalogIdAndOgnId(catalogId, null);
        //私有标签
        List<EpConstantTagPo> ognTag = constantTagService.findByCatalogIdAndOgnId(catalogId, ognId);
        map.put("constantTag", constantTag);
        map.put("ognTag", ognTag);
        resultDo.setResult(map);
        return resultDo;
    }

    /**
     * 商户创建私有标签
     * @param catalogId
     * @param tagName
     * @return
     */
    @GetMapping("createOgnTag")
    @ResponseBody
    public ResultDo createOgnTag(
            HttpServletRequest request,
            @RequestParam(value = "catalogId") Long catalogId,
            @RequestParam(value = "tagName") String tagName
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        ResultDo resultDo=ResultDo.build();
        EpConstantTagPo constantTagPo = new EpConstantTagPo();
        constantTagPo.setCatalogId(catalogId);
        constantTagPo.setOgnId(ognId);
        constantTagPo.setOgnFlag(true);
        constantTagPo.setTagName(tagName);
        EpConstantTagPo insertPo= constantTagService.createPo(constantTagPo);
        resultDo.setResult(insertPo.getTagName());
        return resultDo;
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    @GetMapping("deleteTag/{id}")
    @ResponseBody
    public ResultDo deleteOgnTag(@PathVariable("id") Long id){
        ResultDo resultDo = ResultDo.build();
        constantTagService.deleteById(id);
        return resultDo;
    }

}
