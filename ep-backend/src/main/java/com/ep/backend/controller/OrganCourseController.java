package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.EpOrganClassBo;
import com.ep.domain.pojo.bo.OrganCourseBo;
import com.ep.domain.pojo.dto.CreateOrganCourseDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseType;
import com.ep.domain.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * @Description: 机构课程控制器
 * @Author: CC.F
 * @Date: 15:46 2018/2/6
 */
@Controller
@RequestMapping("auth/organCourse")
public class OrganCourseController extends BackendController {
    @Autowired
    private OrganCourseService organCourseService;
    @Autowired
    private OrganClassService organClassService;
    @Autowired
    private ConstantCatalogService constantCatalogService;
    @Autowired
    private OrganAccountService organAccountService;
    @Autowired
    private ConstantTagService constantTagService;

    @GetMapping("index")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
//                         , @RequestParam(value = "mobile", required = false) String mobile,
//                        @RequestParam(value = "type", required = false) String type,
//                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
//                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();

        conditions.add(EP.EP_ORGAN_COURSE.DEL_FLAG.eq(false));
        Page<OrganCourseBo> page = organCourseService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "/organCourse/index";
    }

    /**
     * 商家后台课程列表
     *
     * @return
     */
    @GetMapping("/merchantIndex")
    public String merchantIndex(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
            , @RequestParam(value = "courseName", required = false) String courseName
            , @RequestParam(value = "courseType", required = false) String courseType
            , @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime
            , @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        map.put("courseName", courseName);
        if (StringTools.isNotBlank(courseType)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_TYPE.eq(EpOrganCourseCourseType.valueOf(courseType)));
        }
        map.put("courseType", courseType);
        if (null != crStartTime) {
            conditions.add(EP.EP_ORGAN_COURSE.CREATE_AT.greaterOrEqual(crStartTime));
        }
        map.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP.EP_ORGAN_COURSE.CREATE_AT.lessOrEqual(crEndTime));
        }
        map.put("crEndTime", crEndTime);
        conditions.add(EP.EP_ORGAN_COURSE.DEL_FLAG.eq(false));
        Page<OrganCourseBo> page = organCourseService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "organCourse/merchantIndex";
    }

    /**
     * 商家后台新增课程初始化
     *
     * @return
     */
    @GetMapping("/merchantCreateInit")
    public String merchantCreateInit(Model model, HttpServletRequest request) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        List<EpConstantCatalogPo> constantCatalogList = constantCatalogService.findSecondCatalog();
        Map<Long, String> constantCatalogMap = Maps.newHashMap();
        constantCatalogList.forEach(p -> {
            constantCatalogMap.put(p.getId(), p.getLabel());
        });
        List<EpOrganAccountPo> organAccountList = organAccountService.findByOgnId(currentUser.getOgnId());
        Map<Long, String> organAccountMap = Maps.newHashMap();
        organAccountList.forEach(p -> {
            organAccountMap.put(p.getId(), p.getAccountName());
        });
        model.addAttribute("constantCatalogMap", constantCatalogMap);
        model.addAttribute("organAccountMap", organAccountMap);
        return "organCourse/merchantForm";
    }

    /**
     * 根据课程类目获得标签
     *
     * @param catalogId
     * @return
     */
    @GetMapping("findTagsByCatalog/{catalogId}")
    @ResponseBody
    public ResultDo findTagsByConstantCatalog(@PathVariable("catalogId") Long catalogId, HttpServletRequest request) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        ResultDo resultDo = ResultDo.build();

        List<EpConstantTagPo> list = constantTagService.findByCatalogIdAndOgnId(catalogId, null);
        Map<Long, String> tagMap = Maps.newHashMap();
        list.forEach(p -> {
            tagMap.put(p.getId(), p.getTagName());
        });
        resultDo.setResult(tagMap);
        return resultDo;
    }


    /**
     * 商家后台新增课程
     *
     * @return
     */
    @PostMapping("/merchantCreate")
    @ResponseBody
    public ResultDo merchantCreate(HttpServletRequest request, CreateOrganCourseDto dto) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        Long ognId = currentUser.getOgnId();
        EpOrganCoursePo organCoursePo = dto.getOrganCoursePo();
        List<EpOrganClassBo> organClassBos = dto.getOrganClassBos();
        List<EpConstantTagPo> constantTagPos = dto.getConstantTagPos();
        organCoursePo.setOgnId(ognId);
        organCourseService.createOrganCourseByMerchant(organCoursePo, organClassBos, constantTagPos);
        ResultDo resultDo = ResultDo.build();
        return resultDo;
    }

    /**
     * 商家后台查看课程
     *
     * @return
     */
    @GetMapping("/merchantview/{id}")
    public String merchantview(Model model,@PathVariable(value = "id") Long id) {
        EpOrganCoursePo organCoursePo = organCourseService.getById(id);
        List<EpOrganClassPo> organClassPos = organClassService.findByCourseId(id);
        model.addAttribute("organCoursePo",organCoursePo);

        List<EpConstantCatalogPo> constantCatalogList = constantCatalogService.findSecondCatalog();
        Map<Long, String> constantCatalogMap = Maps.newHashMap();
        constantCatalogList.forEach(p -> {
            constantCatalogMap.put(p.getId(), p.getLabel());
        });
        model.addAttribute("constantCatalogMap", constantCatalogMap);
        return "organCourse/merchantView";
    }
}
