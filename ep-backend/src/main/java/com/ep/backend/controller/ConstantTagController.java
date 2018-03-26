package com.ep.backend.controller;

import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.ConstantTagBo;
import com.ep.domain.pojo.po.EpConstantCatalogPo;
import com.ep.domain.pojo.po.EpConstantTagPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.ConstantCatalogService;
import com.ep.domain.service.ConstantTagService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Tables.EP_CONSTANT_TAG;

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

    @GetMapping("index")
    public String index(Model model) {
        List<EpConstantCatalogPo> constantCatalogPos = constantCatalogService.findSecondCatalog();
        Map<Long,String> constantCatalogMap=Maps.newHashMap();
        Map<Long,List<ConstantTagBo>> constantTagsMap=Maps.newHashMap();
        constantCatalogPos.forEach(po->{
            constantCatalogMap.put(po.getId(),po.getLabel());
            List<ConstantTagBo> constantTagBos = constantTagService.findBosByCatalogIdAndOgnId(po.getId(),null);
            constantTagBos.forEach(bo->{
                bo.setUsedFlag(bo.getUsedOrganCourseTag().longValue() > BizConstant.DB_NUM_ZERO);
            });
            constantTagsMap.put(po.getId(),constantTagBos);
        });
        model.addAttribute("constantCatalogMap", constantCatalogMap);
        model.addAttribute("constantTagsMap", constantTagsMap);
        return "constantTag/index";
    }

//    @GetMapping("merchantIndex")
//    public String merchantIndex(Model model) {
//        List<EpConstantCatalogPo> constantCatalogPos = constantCatalogService.findSecondCatalog();
//        model.addAttribute("constantCatalogPos", constantCatalogPos);
//        return "constantTag/merchantIndex";
//    }


    @GetMapping("merchantIndex")
//    @PreAuthorize("hasAnyAuthority('platform:organCourse:index')")
    public String merchantIndex(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                @RequestParam(value = "courseName", required = false) String courseName,
                                @RequestParam(value = "courseType", required = false) String courseType,
                                @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                                @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
//        if (StringTools.isNotBlank(courseName)) {
//            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
//        }
//        searchMap.put("courseName", courseName);
//        if (StringTools.isNotBlank(courseType)) {
//            conditions.add(EP_ORGAN_COURSE.COURSE_TYPE.eq(EpOrganCourseCourseType.valueOf(courseType)));
//        }
//        searchMap.put("courseType", courseType);
        if (null != crStartTime) {
            conditions.add(EP_CONSTANT_TAG.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_CONSTANT_TAG.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP_CONSTANT_TAG.OGN_ID.eq(ognId));
        conditions.add(EP_CONSTANT_TAG.DEL_FLAG.eq(false));
        Page<ConstantTagBo> page = constantTagService.findbyPageAndCondition(pageable, conditions);
        page.getContent().forEach(p -> {
            p.setUsedFlag(p.getUsedOrganCourseTag().longValue() > BizConstant.DB_NUM_ZERO);
        });
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "constantTag/merchantIndex";
    }


    /**
     * 根据类目获得标签
     *
     * @param catalogId
     * @return
     */
    @GetMapping("findTags")
    @ResponseBody
    public ResultDo findTags(
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
     * 后台创建公用标签
     *
     * @param catalogId
     * @param tagName
     * @return
     */
    @GetMapping("createConstantTag")
    @ResponseBody
    public ResultDo createConstantTag(
            @RequestParam(value = "catalogId") Long catalogId,
            @RequestParam(value = "tagName") String tagName
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        EpConstantTagPo constantTagPo = new EpConstantTagPo();
        constantTagPo.setOgnId(ognId);
        constantTagPo.setTagName(tagName);

        return constantTagService.createConstantTag(constantTagPo);
    }

    /**
     * 商户创建私有标签
     *
     * @param catalogId
     * @param tagName
     * @return
     */
    @GetMapping("createOgnTag")
    @ResponseBody
    public ResultDo createOgnTag(
            @RequestParam(value = "catalogId") Long catalogId,
            @RequestParam(value = "tagName") String tagName
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        EpConstantTagPo constantTagPo = new EpConstantTagPo();
        constantTagPo.setOgnId(ognId);
        constantTagPo.setOgnFlag(true);
        constantTagPo.setTagName(tagName);
        ResultDo resultDo = constantTagService.createConstantTag(constantTagPo);
        return resultDo;
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @GetMapping("deleteTag/{id}")
    @ResponseBody
    public ResultDo deleteOgnTag(@PathVariable("id") Long id) {

        return constantTagService.deleteById(id);
    }

}
