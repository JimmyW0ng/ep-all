package com.ep.backend.controller;

import com.ep.common.tool.BeanTools;
import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.CreateOrganCourseDto;
import com.ep.domain.pojo.dto.FileDto;
import com.ep.domain.pojo.dto.RectifyOrganCourseDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseType;
import com.ep.domain.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ep.domain.repository.domain.Ep.EP;
import static com.ep.domain.repository.domain.Tables.EP_ORGAN_COURSE;

/**
 * @Description: 机构课程控制器
 * @Author: CC.F
 * @Date: 15:46 2018/2/6
 */
@Slf4j
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
    @Autowired
    private FileService fileService;
    @Autowired
    private OrganService organService;
    @Autowired
    private OrganConfigService organConfigService;

    /**
     * 平台课程列表
     *
     * @param model
     * @param pageable
     * @param courseName
     * @param courseType
     * @param crStartTime
     * @param crEndTime
     * @return
     */
    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('platform:organCourse:index')")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "courseName", required = false) String courseName,
                        @RequestParam(value = "courseType", required = false) String courseType,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(courseType)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_TYPE.eq(EpOrganCourseCourseType.valueOf(courseType)));
        }
        searchMap.put("courseType", courseType);
        if (null != crStartTime) {
            conditions.add(EP_ORGAN_COURSE.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_ORGAN_COURSE.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP_ORGAN_COURSE.DEL_FLAG.eq(false));
        Page<OrganCourseBo> page = organCourseService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "organCourse/index";
    }

    /**
     * 商家后台课程列表
     *
     * @return
     */
    @GetMapping("/merchantIndex")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
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
        Long ognId = super.getCurrentUser().get().getOgnId();
        conditions.add(EP.EP_ORGAN_COURSE.OGN_ID.eq(ognId));
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
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    public String merchantCreateInit(Model model) {
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
        EpOrganCoursePo organCoursePo = new EpOrganCoursePo();
        Optional<EpOrganPo> organOptional = organService.getById(currentUser.getOgnId());
        if (organOptional.isPresent()) {
            organCoursePo.setCourseAddress(organOptional.get().getOgnAddress());
            //该机构是否有会员制度
            model.addAttribute("organVipFlag", organOptional.get().getVipFlag());
        }
        model.addAttribute("organCoursePo", organCoursePo);

        Optional<EpOrganConfigPo> organConfigOptional = organConfigService.getByOgnId(currentUser.getOgnId());
        model.addAttribute("supportTag", organConfigOptional.get().getSupportTag());
        return "organCourse/merchantForm";
    }

    /**
     * 根据课程类目获得标签
     *
     * @param catalogId
     * @return
     */
    @GetMapping("findTagsByCatalog/{catalogId}")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public ResultDo<List<EpConstantTagPo>> findTagsByConstantCatalog(
            @PathVariable("catalogId") Long catalogId) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        ResultDo<List<EpConstantTagPo>> resultDo = ResultDo.build();

        //公用标签
        List<EpConstantTagPo> constantTagList = constantTagService.findByCatalogIdAndOgnId(catalogId, null);
        //私有标签
        List<EpConstantTagPo> ognTagList = constantTagService.findByCatalogIdAndOgnId(catalogId, ognId);
        ognTagList.addAll(constantTagList);

        resultDo.setResult(ognTagList);
        return resultDo;
    }


    /**
     * 商家后台新增课程
     *
     * @return
     */
    @PostMapping("/merchantCreate")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public ResultDo merchantCreate(CreateOrganCourseDto dto) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        return organCourseService.createOrganCourseByMerchant(dto, ognId);
    }

    /**
     * 商家后台查看课程
     *
     * @return
     */
    @GetMapping("/merchantview/{courseId}")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    public String merchantview(Model model, @PathVariable(value = "courseId") Long courseId) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
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
        List<EpOrganAccountPo> organAccountList = organAccountService.findByOgnId(currentUser.getOgnId());
        Map<Long, String> organAccountMap = Maps.newHashMap();
        organAccountList.forEach(p -> {
            organAccountMap.put(p.getId(), p.getAccountName());
        });
        //教师下拉框
        model.addAttribute("organAccountMap", organAccountMap);

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
        //课程主图
        Optional<EpFilePo> mainpicImgOptional = organCourseService.getCourseMainpic(courseId);
        if (mainpicImgOptional.isPresent()) {
            model.addAttribute("mainpicImgUrl", mainpicImgOptional.get().getFileUrl());
        }
        return "organCourse/merchantView";
    }


    /**
     * 商家后台修改课程初始化
     *
     * @return
     */
    @GetMapping("/merchantUpdateInit/{courseId}")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    public String merchantUpdateInit(Model model, @PathVariable(value = "courseId") Long courseId) {

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

        //课程主图
        Optional<EpFilePo> mainpicImgOptional = organCourseService.getCourseMainpic(courseId);
        if (mainpicImgOptional.isPresent()) {
            model.addAttribute("mainpicImgUrl", mainpicImgOptional.get().getFileUrl());
        }
        Optional<EpOrganConfigPo> organConfigOptional = organConfigService.getByOgnId(currentUser.getOgnId());
        model.addAttribute("supportTag", organConfigOptional.get().getSupportTag());
        return "organCourse/merchantForm";
    }

    /**
     * 商家后台紧急修改课程初始化
     *
     * @return
     */
    @GetMapping("/merchantRectifyInit/{courseId}")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    public String merchantRectifyInit(Model model, @PathVariable(value = "courseId") Long courseId) {

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

        //班次
        List<EpOrganClassPo> organClassPos = organClassService.findByCourseId(courseId);
        List<RectifyOrganClassBo> organClassBos = Lists.newArrayList();
        organClassPos.forEach(p -> {
            RectifyOrganClassBo organClassBo = new RectifyOrganClassBo();
            BeanTools.copyPropertiesIgnoreNull(p, organClassBo);
            //班次目录
            List<RectifyOrganClassCatalogBo> rectifyOrganClassCatalogBos = organClassCatalogService.findRectifyBoByClassId(p.getId());
            rectifyOrganClassCatalogBos.forEach(bo -> {
                Timestamp startTime = bo.getStartTime();
                if (DateTools.compareTwoTime(DateTools.getCurrentDateTime(), startTime) != -1) {
                    bo.setRectifyFlag(false);
                } else {
                    boolean flag = (DateTools.getTwoTimeDiffSecond(startTime, DateTools.getCurrentDateTime()) / BizConstant.TIME_UNIT) >=
                            BizConstant.RECTIFY_CATALOG_STARTTIME_TONOW_LT30;
                    bo.setRectifyFlag(flag);
                }
            });
            if (CollectionsTools.isNotEmpty(rectifyOrganClassCatalogBos)) {
                organClassBo.setRectifyOrganClassCatalogBos(rectifyOrganClassCatalogBos);
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

        //课程主图
        Optional<EpFilePo> mainpicImgOptional = organCourseService.getCourseMainpic(courseId);
        if (mainpicImgOptional.isPresent()) {
            model.addAttribute("mainpicImgUrl", mainpicImgOptional.get().getFileUrl());
        }
        Optional<EpOrganConfigPo> organConfigOptional = organConfigService.getByOgnId(currentUser.getOgnId());
        model.addAttribute("supportTag", organConfigOptional.get().getSupportTag());
        return "organCourse/merchantRectify";
    }

    /**
     * 商家后台修改课程
     *
     * @return
     */
    @PostMapping("/merchantUpdate")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public ResultDo merchantUpdate(CreateOrganCourseDto dto) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();

        return organCourseService.updateOrganCourseByMerchant(dto, ognId);
    }

    /**
     * 商家后台紧急修改课程
     *
     * @return
     */
    @PostMapping("/merchantRectify")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public ResultDo merchantRectify(RectifyOrganCourseDto dto) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        if (null == dto) {
            log.error("[课程]紧急修改课程失败。该课程不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        dto.getOrganCoursePo().setOgnId(ognId);
        return organCourseService.rectifyOrganCourseByMerchant(dto);
    }

    /**
     * 删除课程
     *
     * @param courseId
     * @return
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public ResultDo deleteByCourseId(@PathVariable(value = "id") Long courseId) {
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

    /**
     * 上传课程主图
     *
     * @param file
     * @return
     */
    @PostMapping("uploadMainpic")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public ResultDo uploadMainpic(@RequestParam("file") MultipartFile file) throws Exception {
        return fileService.addFileByBizType(file.getName(), file.getBytes(), BizConstant.FILE_BIZ_TYPE_CODE_COURSE_MAIN_PIC, null);
    }

    /**
     * 上传课程内容图片wangEditor
     *
     * @return
     */
    @PostMapping("uploadCourseDescPic")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public String uploadCourseDescPic(@RequestParam("file") MultipartFile file
    ) throws Exception {
        ResultDo resultDo = fileService.addFileByBizType(file.getName(), file.getBytes(), BizConstant.FILE_BIZ_TYPE_CODE_COURSE_DESC_PIC, null);
        FileDto fileDto = (FileDto) resultDo.getResult();
        String fileUrl = fileDto.getFileUrl();
        String name = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.lastIndexOf("."));
        String result = "{\"errno\":\"" + 0 + "\", \"data\":[ \"" + fileDto.getFileUrl() + "\"],\"precode\":\"" + fileDto.getPreCode() +
                "\", \"name\":\"" + name + "\"}";

        return result.replaceAll("\\\\", "\\\\");
    }
}


