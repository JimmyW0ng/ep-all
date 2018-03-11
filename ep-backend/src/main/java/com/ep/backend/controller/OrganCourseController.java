package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganClassBo;
import com.ep.domain.pojo.bo.OrganCourseBo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
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
import java.util.Optional;

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
    @Autowired
    private OrganCourseTagService organCourseTagService;
    @Autowired
    private OrganClassCatalogService organClassCatalogService;

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
        EpSystemUserPo currentUser = super.getCurrentUser().get();
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
        //课程目录下拉框
        model.addAttribute("constantCatalogMap", constantCatalogMap);
        //教师下拉框
        model.addAttribute("organAccountMap", organAccountMap);
        //课程对象
        model.addAttribute("organCoursePo", new EpOrganCoursePo());
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
    public ResultDo findTagsByConstantCatalog(
            @PathVariable("catalogId") Long catalogId,
            HttpServletRequest request) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        ResultDo resultDo = ResultDo.build();

        //公用标签
        List<EpConstantTagPo> constantTagList = constantTagService.findByCatalogIdAndOgnId(catalogId, null);
        //私有标签
        List<EpConstantTagPo> ognTagList = constantTagService.findByCatalogIdAndOgnId(catalogId, ognId);
        ognTagList.addAll(constantTagList);
        Map<String, Object> map = Maps.newHashMap();

        map.put("ognTagList", ognTagList);


        resultDo.setResult(map);
        return resultDo;
    }


    /**
     * 商家后台新增课程
     *
     * @return
     */
    @PostMapping("/merchantCreate")
    @ResponseBody
    public ResultDo merchantCreate(CreateOrganCourseDto dto) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        dto.getOrganCoursePo().setOgnId(ognId);
//        EpOrganCoursePo organCoursePo = dto.getOrganCoursePo();
//        List<OrganClassBo> organClassBos = dto.getOrganClassBos();
//        List<EpConstantTagPo> constantTagPos = dto.getConstantTagPos();
        return organCourseService.createOrganCourseByMerchant(dto);
    }

    /**
     * 商家后台查看课程
     *
     * @return
     */
    @GetMapping("/merchantview/{courseId}")
    public String merchantview(Model model, @PathVariable(value = "courseId") Long courseId) {
        //机构课程
        Optional<EpOrganCoursePo> courseOptional = organCourseService.findById(courseId);
        if (!courseOptional.isPresent()) {
            return "errorErupt";
        }
        model.addAttribute("organCoursePo", courseOptional.get());

        //课程类目
        List<EpConstantCatalogPo> constantCatalogList = constantCatalogService.findSecondCatalog();
        Map<Long, String> constantCatalogMap = Maps.newHashMap();
        constantCatalogList.forEach(p -> {
            constantCatalogMap.put(p.getId(), p.getLabel());
        });
        model.addAttribute("constantCatalogMap", constantCatalogMap);

        //标签
        List<OrganCourseTagBo> organCourseTagBos = organCourseTagService.findBosByCourseId(courseId);
        model.addAttribute("organCourseTagBos", organCourseTagBos);

        //班次
        List<EpOrganClassPo> organClassPos = organClassService.findByCourseId(courseId);
        List<OrganClassBo> organClassBos = Lists.newArrayList();
        organClassPos.forEach(po -> {
            OrganClassBo organClassBo = new OrganClassBo();
            BeanTools.copyPropertiesIgnoreNull(po, organClassBo);
            //班次目录
            List<EpOrganClassCatalogPo> organClassCatalogPos = organClassCatalogService.findByClassId(po.getId());
            organClassBo.setOrganClassCatalogPos(organClassCatalogPos);
            organClassBos.add(organClassBo);
        });
        model.addAttribute("organClassBos", organClassBos);

        return "organCourse/merchantView";
    }


    /**
     * 商家后台修改课程初始化
     *
     * @return
     */
    @GetMapping("/merchantUpdateInit/{courseId}")
    public String merchantUpdateInit(HttpServletRequest request, Model model, @PathVariable(value = "courseId") Long courseId) {

        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
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
        //课程目录下拉框
        model.addAttribute("constantCatalogMap", constantCatalogMap);
        //教师下拉框
        model.addAttribute("organAccountMap", organAccountMap);

        Optional<EpOrganCoursePo> courseOptional = organCourseService.findById(courseId);
        if (!courseOptional.isPresent()) {
            return "errorErupt";
        }
        EpOrganCoursePo organCoursePo = courseOptional.get();
        //课程对象
        model.addAttribute("organCoursePo", organCoursePo);
        Long catalogId = organCoursePo.getCourseCatalogId();

        List<EpOrganClassPo> organClassPos = organClassService.findByCourseId(courseId);
        List<OrganClassBo> organClassBos = Lists.newArrayList();
        organClassPos.forEach(p -> {
            OrganClassBo organClassBo = new OrganClassBo();
            BeanTools.copyPropertiesIgnoreNull(p, organClassBo);
            List<EpOrganClassCatalogPo> organClassCatalogPos = organClassCatalogService.findByClassId(p.getId());
            if (CollectionsTools.isNotEmpty(organClassCatalogPos)) {
                organClassBo.setOrganClassCatalogPos(organClassCatalogPos);
            }
            organClassBos.add(organClassBo);

        });
        //班次
        model.addAttribute("organClassBos", organClassBos);

        //课程标签
        List<OrganCourseTagBo> organCourseTagBos = organCourseTagService.findBosByCourseId(courseId);

        //公用标签
        List<EpConstantTagPo> constantTagList = constantTagService.findByCatalogIdAndOgnId(catalogId, null);
        //私有标签
        List<EpConstantTagPo> ognTagList = constantTagService.findByCatalogIdAndOgnId(catalogId, ognId);
        ognTagList.addAll(constantTagList);

        model.addAttribute("organCourseTagBos", organCourseTagBos);
        model.addAttribute("ognTagList", ognTagList);

        return "organCourse/merchantForm";
    }

    /**
     * 商家后台修改课程
     *
     * @return
     */
    @PostMapping("/merchantUpdate")
    @ResponseBody
    public ResultDo merchantUpdate(CreateOrganCourseDto dto) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();

        dto.getOrganCoursePo().setOgnId(ognId);
        return organCourseService.updateOrganCourseByMerchant(dto);
    }

    /**
     * 删除课程
     *
     * @param courseId
     * @return
     */
    public ResultDo deleteByCourseId(Long courseId) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        return organCourseService.deleteCourseByCourseId(courseId, ognId);
    }

    /**
     * 上线课程
     *
     * @param id
     * @return
     */
    @GetMapping("online/{id}")
    @ResponseBody
    public ResultDo onlineById(@PathVariable(value = "id") Long id) {
        EpSystemUserPo userPo = super.getCurrentUser().get();
        return organCourseService.onlineById(userPo, id);
    }

}


