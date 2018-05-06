package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.*;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.*;
import com.ep.domain.pojo.dto.CreateOrganCourseDto;
import com.ep.domain.pojo.dto.FileDto;
import com.ep.domain.pojo.dto.RectifyOrganCourseDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.domain.enums.EpConstantTagStatus;
import com.ep.domain.repository.domain.enums.EpOrganAccountStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseType;
import com.ep.domain.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import java.util.*;

import static com.ep.domain.repository.domain.Ep.EP;
import static com.ep.domain.repository.domain.Tables.EP_ORGAN_COURSE;

/**
 * @Description: 机构产品控制器
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
     * 平台产品列表
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
                        @RequestParam(value = "courseStatus", required = false) String courseStatus,
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
        if (StringTools.isNotBlank(courseStatus)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_STATUS.eq(EpOrganCourseCourseStatus.valueOf(courseStatus)));
        }
        searchMap.put("courseStatus", courseStatus);
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
     * 商家后台产品列表
     *
     * @return
     */
    @GetMapping("/merchantIndex")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    public String merchantIndex(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
            , @RequestParam(value = "courseName", required = false) String courseName
            , @RequestParam(value = "courseType", required = false) String courseType
            , @RequestParam(value = "courseStatus", required = false) String courseStatus
            , @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime
            , @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(courseType)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_TYPE.eq(EpOrganCourseCourseType.valueOf(courseType)));
        }
        searchMap.put("courseType", courseType);
        if (StringTools.isNotBlank(courseStatus)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_STATUS.eq(EpOrganCourseCourseStatus.valueOf(courseStatus)));
        }
        searchMap.put("courseStatus", courseStatus);
        if (null != crStartTime) {
            conditions.add(EP.EP_ORGAN_COURSE.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP.EP_ORGAN_COURSE.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP.EP_ORGAN_COURSE.DEL_FLAG.eq(false));
        Long ognId = super.getCurrentUser().get().getOgnId();
        conditions.add(EP.EP_ORGAN_COURSE.OGN_ID.eq(ognId));
        Page<OrganCourseBo> page = organCourseService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        model.addAttribute("wechatXcxCourseUrl", String.format(BizConstant.WECHAT_XCX_COURSE_URL, super.getCurrentUserOgnId()));
        return "organCourse/merchantIndex";
    }

    /**
     * 商家后台新增产品初始化
     *
     * @return
     */
    @GetMapping("/merchantCreateInit")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    public String merchantCreateInit(Model model) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        List<EpConstantCatalogPo> firstConstantCatalogSelectModel = constantCatalogService.findFirstCatalogSelectModel();
        //产品科目下拉框
        model.addAttribute("firstConstantCatalogSelectModel", firstConstantCatalogSelectModel);
        List<EpOrganAccountPo> organAccountSelectModel = organAccountService.findSelectModelByOgnIdAndStatus(ognId,
                new EpOrganAccountStatus[]{EpOrganAccountStatus.normal, EpOrganAccountStatus.freeze});
        //教师下拉框
        model.addAttribute("organAccountSelectModel", organAccountSelectModel);
        //产品对象
        EpOrganCoursePo organCoursePo = new EpOrganCoursePo();

        model.addAttribute("organCoursePo", organCoursePo);

        Optional<EpOrganPo> organOptional = organService.getById(ognId);
        if (organOptional.isPresent()) {
            //该机构是否有会员制度
            model.addAttribute("organVipFlag", organOptional.get().getVipFlag());
            model.addAttribute("organVipName", organOptional.get().getVipName());
            //机构电话
            model.addAttribute("ognPhone", organOptional.get().getOgnPhone());
            //机构经度
            model.addAttribute("ognLng", organOptional.get().getOgnLng());
            //机构纬度
            model.addAttribute("ognLat", organOptional.get().getOgnLat());
            //上课地址
            model.addAttribute("ognAddress", organOptional.get().getOgnAddress());
        }

        //是否支持标签
        Optional<EpOrganConfigPo> organConfigOptional = organConfigService.getByOgnId(currentUser.getOgnId());
        model.addAttribute("supportTag", organConfigOptional.get().getSupportTag());
        if (organConfigOptional.get().getSupportTag()) {
            //公用标签
            List<EpConstantTagPo> constantTagList = constantTagService.findByOgnIdAndStatus(null,
                    new EpConstantTagStatus[]{EpConstantTagStatus.online});
            //私有标签
            List<EpConstantTagPo> ognTagList = constantTagService.findByOgnIdAndStatus(ognId,
                    new EpConstantTagStatus[]{EpConstantTagStatus.online});
            ognTagList.addAll(constantTagList);

            model.addAttribute("ognTagList", ognTagList);
        }

        return "organCourse/merchantForm";
    }


    /**
     * 商家后台新增产品
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
     * 商家后台查看产品
     *
     * @return
     */
    @GetMapping("/merchantview/{courseId}")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex','platform:organCourse:index')")
    public String merchantview(Model model, @PathVariable(value = "courseId") Long courseId) {
        //校验是否本机构的资源
        if (null == this.innerOgnOrPlatformReq(courseId, super.getCurrentUserOgnId())) {
            return "noresource";
        }
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        //机构产品
        Optional<EpOrganCoursePo> courseOptional = organCourseService.findById(courseId);
        model.addAttribute("organCoursePo", courseOptional.get());
        Optional<EpOrganPo> organOptional = organService.getById(currentUser.getOgnId());
        if (organOptional.isPresent()) {
            //该机构是否有会员制度
            model.addAttribute("organVipFlag", organOptional.get().getVipFlag());
            model.addAttribute("organVipName", organOptional.get().getVipName());
        }
        List<EpConstantCatalogPo> firstConstantCatalogSelectModel = constantCatalogService.findFirstCatalogSelectModel();
        //产品科目一级下拉框
        model.addAttribute("firstConstantCatalogSelectModel", firstConstantCatalogSelectModel);
        Optional<EpConstantCatalogPo> constantCatalogOptional = constantCatalogService.findById(courseOptional.get().getCourseCatalogId());
        if (constantCatalogOptional.isPresent()) {
            model.addAttribute("firstConstantCatalog",
                    constantCatalogOptional.get().getParentId().longValue() == BizConstant.FIRST_CONSTANT_CATALOG_PID ?
                            courseOptional.get().getCourseCatalogId() : constantCatalogOptional.get().getParentId());
            //产品科目二级下拉框
            List<EpConstantCatalogPo> secondCatalogs = constantCatalogService.findSecondCatalogSelectModelByPid(constantCatalogOptional.get().getParentId());
            model.addAttribute("secondCatalogs", secondCatalogs);
        }
        List<EpOrganAccountPo> organAccountList = organAccountService.findByOgnIdAndStatus(currentUser.getOgnId(),
                new EpOrganAccountStatus[]{EpOrganAccountStatus.normal, EpOrganAccountStatus.freeze});
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
        //产品主图
        Optional<EpFilePo> mainpicImgOptional = organCourseService.getCourseMainpic(courseId);
        if (mainpicImgOptional.isPresent()) {
            model.addAttribute("mainpicImgUrl", mainpicImgOptional.get().getFileUrl());
        }
        return "organCourse/merchantView";
    }


    /**
     * 商家后台修改产品初始化
     *
     * @return
     */
    @GetMapping("/merchantUpdateInit/{courseId}")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    public String merchantUpdateInit(Model model, @PathVariable(value = "courseId") Long courseId) {
        //校验是否本机构的资源
        if (null == this.innerOgnOrPlatformReq(courseId, super.getCurrentUserOgnId())) {
            return "noresource";
        }
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        //教师下拉框
        List<EpOrganAccountPo> organAccountSelectModel = organAccountService.findSelectModelByOgnIdAndStatus(ognId,
                new EpOrganAccountStatus[]{EpOrganAccountStatus.normal, EpOrganAccountStatus.freeze});
        model.addAttribute("organAccountSelectModel", organAccountSelectModel);
        //产品
        Optional<EpOrganCoursePo> courseOptional = organCourseService.findById(courseId);
        EpOrganCoursePo organCoursePo = courseOptional.get();
        Optional<EpOrganPo> organOptional = organService.getById(currentUser.getOgnId());
        if (organOptional.isPresent()) {
            //该机构是否有会员制度
            model.addAttribute("organVipFlag", organOptional.get().getVipFlag());
            model.addAttribute("organVipName", organOptional.get().getVipName());
            //机构电话
            model.addAttribute("ognPhone", organOptional.get().getOgnPhone());
            //机构经度
            model.addAttribute("ognLng", organOptional.get().getOgnLng());
            //机构纬度
            model.addAttribute("ognLat", organOptional.get().getOgnLat());
            //上课地址
            model.addAttribute("ognAddress", organOptional.get().getOgnAddress());
        }
        //产品对象
        model.addAttribute("organCoursePo", organCoursePo);
        List<EpConstantCatalogPo> firstConstantCatalogSelectModel = constantCatalogService.findFirstCatalogSelectModel();
        //产品科目一级下拉框
        model.addAttribute("firstConstantCatalogSelectModel", firstConstantCatalogSelectModel);
        Optional<EpConstantCatalogPo> constantCatalogOptional = constantCatalogService.findById(organCoursePo.getCourseCatalogId());
        if (constantCatalogOptional.isPresent()) {
            Long firstConstantCatalog = constantCatalogOptional.get().getParentId() == BizConstant.FIRST_CONSTANT_CATALOG_PID
                    ? constantCatalogOptional.get().getId() : constantCatalogOptional.get().getParentId();
            model.addAttribute("firstConstantCatalog", firstConstantCatalog);
            //产品科目二级下拉框
            List<EpConstantCatalogPo> secondCatalogs = Lists.newArrayList();
            EpConstantCatalogPo secondCatalog = constantCatalogService.findById(firstConstantCatalog).get();
            secondCatalogs.add(secondCatalog);
            secondCatalogs.addAll(constantCatalogService.findSecondCatalogSelectModelByPid(constantCatalogOptional.get().getParentId()));
            model.addAttribute("secondCatalogs", secondCatalogs);
        }
        //班次
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
        model.addAttribute("organClassBos", organClassBos);
        //产品标签
        List<OrganCourseTagBo> organCourseTagBos = organCourseTagService.findBosByCourseId(courseId);
        //公用标签
        List<EpConstantTagPo> constantTagList = constantTagService.findByOgnIdAndStatus(null,
                new EpConstantTagStatus[]{EpConstantTagStatus.online, EpConstantTagStatus.offline});
        //私有标签
        List<EpConstantTagPo> ognTagList = constantTagService.findByOgnIdAndStatus(ognId,
                new EpConstantTagStatus[]{EpConstantTagStatus.online, EpConstantTagStatus.offline});
        //私有标签+公用标签
        ognTagList.addAll(constantTagList);


        model.addAttribute("organCourseTagBos", organCourseTagBos);
        //去除状态为offline且没被用到的标签
        model.addAttribute("ognTagList", this.courseShowTags(organCourseTagBos, ognTagList));
        //产品主图
        Optional<EpFilePo> mainpicImgOptional = organCourseService.getCourseMainpic(courseId);
        if (mainpicImgOptional.isPresent()) {
            model.addAttribute("mainpicImgUrl", mainpicImgOptional.get().getFileUrl());
        }
        Optional<EpOrganConfigPo> organConfigOptional = organConfigService.getByOgnId(currentUser.getOgnId());
        model.addAttribute("supportTag", organConfigOptional.get().getSupportTag());
        return "organCourse/merchantForm";
    }

    /**
     * 商家后台紧急修改产品初始化
     *
     * @return
     */
    @GetMapping("/merchantRectifyInit/{courseId}")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    public String merchantRectifyInit(Model model, @PathVariable(value = "courseId") Long courseId) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = super.getCurrentUserOgnId();
        //校验是否本机构的资源
        if (null == this.innerOgnOrPlatformReq(courseId, ognId)) {
            return "noresource";
        }
        //教师下拉框
        List<EpOrganAccountPo> organAccountList = organAccountService.findByOgnIdAndStatus(currentUser.getOgnId(),
                new EpOrganAccountStatus[]{EpOrganAccountStatus.normal, EpOrganAccountStatus.freeze});
        Map<Long, String> organAccountMap = Maps.newHashMap();
        organAccountList.forEach(p -> {
            organAccountMap.put(p.getId(), p.getAccountName());
        });
        model.addAttribute("organAccountMap", organAccountMap);
        //产品
        Optional<EpOrganCoursePo> courseOptional = organCourseService.findById(courseId);
        List<EpConstantCatalogPo> firstConstantCatalogSelectModel = constantCatalogService.findFirstCatalogSelectModel();
        //产品科目一级下拉框
        model.addAttribute("firstConstantCatalogSelectModel", firstConstantCatalogSelectModel);
        Optional<EpConstantCatalogPo> constantCatalogOptional = constantCatalogService.findById(courseOptional.get().getCourseCatalogId());
        if (constantCatalogOptional.isPresent()) {
            model.addAttribute("firstConstantCatalog",
                    constantCatalogOptional.get().getParentId().longValue() == BizConstant.FIRST_CONSTANT_CATALOG_PID ?
                            courseOptional.get().getCourseCatalogId() : constantCatalogOptional.get().getParentId());
            //产品科目二级下拉框
            List<EpConstantCatalogPo> secondCatalogs = constantCatalogService.findSecondCatalogSelectModelByPid(constantCatalogOptional.get().getParentId());
            model.addAttribute("secondCatalogs", secondCatalogs);
        }
        Optional<EpOrganPo> organOptional = organService.getById(currentUser.getOgnId());
        if (organOptional.isPresent()) {
            //该机构是否有会员制度
            model.addAttribute("organVipFlag", organOptional.get().getVipFlag());
            model.addAttribute("organVipName", organOptional.get().getVipName());
        }
        //产品对象
        EpOrganCoursePo organCoursePo = courseOptional.get();
        model.addAttribute("organCoursePo", organCoursePo);
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
        //产品标签
        List<OrganCourseTagBo> organCourseTagBos = organCourseTagService.findBosByCourseId(courseId);
        //公用标签
        List<EpConstantTagPo> constantTagList = constantTagService.findByOgnIdAndStatus(null,
                new EpConstantTagStatus[]{EpConstantTagStatus.online, EpConstantTagStatus.offline});
        //私有标签
        List<EpConstantTagPo> ognTagList = constantTagService.findByOgnIdAndStatus(ognId,
                new EpConstantTagStatus[]{EpConstantTagStatus.online, EpConstantTagStatus.offline});
        //私有标签+公用标签
        ognTagList.addAll(constantTagList);
        //去除状态为offline且没被用到的标签
        model.addAttribute("organCourseTagBos", organCourseTagBos);
        model.addAttribute("ognTagList", this.courseShowTags(organCourseTagBos, ognTagList));
        //产品主图
        Optional<EpFilePo> mainpicImgOptional = organCourseService.getCourseMainpic(courseId);
        if (mainpicImgOptional.isPresent()) {
            model.addAttribute("mainpicImgUrl", mainpicImgOptional.get().getFileUrl());
        }
        //机构配置
        Optional<EpOrganConfigPo> organConfigOptional = organConfigService.getByOgnId(currentUser.getOgnId());
        //是否支持称号
        model.addAttribute("supportTag", organConfigOptional.get().getSupportTag());
        return "organCourse/merchantRectify";
    }

    /**
     * 商家后台修改产品
     *
     * @return
     */
    @PostMapping("/merchantUpdate")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public ResultDo merchantUpdate(CreateOrganCourseDto dto) {
        //校验是否本机构的资源
        if (null == this.innerOgnOrPlatformReq(dto.getOrganCoursePo().getId(), super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return organCourseService.updateOrganCourseByMerchant(dto, super.getCurrentUserOgnId());
    }

    /**
     * 商家后台紧急修改产品
     *
     * @return
     */
    @PostMapping("/merchantRectify")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public ResultDo merchantRectify(RectifyOrganCourseDto dto) {
        Long ognId = super.getCurrentUserOgnId();
        if (null == dto) {
            log.error("[产品]紧急修改产品失败。该产品不存在。");
            return ResultDo.build(MessageCode.ERROR_COURSE_NOT_EXIST);
        }
        //校验是否本机构的资源
        if (null == this.innerOgnOrPlatformReq(dto.getOrganCoursePo().getId(), ognId)) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        dto.getOrganCoursePo().setOgnId(ognId);
        return organCourseService.rectifyOrganCourseByMerchant(dto);
    }

    /**
     * 删除产品
     *
     * @param courseId
     * @return
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public ResultDo deleteByCourseId(@PathVariable(value = "id") Long courseId) {
        if (null == this.innerOgnOrPlatformReq(courseId, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        return organCourseService.deleteCourseByCourseId(courseId, ognId);
    }

    /**
     * 上线产品
     *
     * @param id
     * @return
     */
    @GetMapping("online/{id}")
    @ResponseBody
    public ResultDo onlineById(@PathVariable(value = "id") Long id) {
        if (null == this.innerOgnOrPlatformReq(id, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        EpSystemUserPo userPo = super.getCurrentUser().get();
        return organCourseService.onlineById(userPo, id);
    }

    /**
     * 上传产品主图
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
     * 改变科目一级目录
     *
     * @param firstConstantCatalogId
     * @return
     */
    @GetMapping("changeFirstCatalog/{firstConstantCatalogId}")
    @ResponseBody
    public ResultDo changeFirstCatalog(@PathVariable(value = "firstConstantCatalogId") Long firstConstantCatalogId) {
        return ResultDo.build().setResult(constantCatalogService.findSecondCatalogSelectModelByPid(firstConstantCatalogId));
    }


    /**
     * 上传产品内容图片uEditor
     *
     * @return
     */
    @PostMapping("uploadCourseDescPic")
    @PreAuthorize("hasAnyAuthority('merchant:organCourse:merchantIndex')")
    @ResponseBody
    public String umEditorUploadCourseDescPic(
            @RequestParam("upfile") MultipartFile file
    ) throws Exception {
        ResultDo resultDo = fileService.addFileByBizType(file.getName(), file.getBytes(), BizConstant.FILE_BIZ_TYPE_CODE_COURSE_DESC_PIC, null);
        FileDto fileDto = (FileDto) resultDo.getResult();
        String fileUrl = fileDto.getFileUrl();
        String filePreCode = fileDto.getPreCode();
        String result = "";
        result = "{\"name\":\"" + file.getName() + "\", \"originalName\": \"" + file.getOriginalFilename() + "\", \"size\": " + file.getSize()
                + ", \"state\": \"SUCCESS\", \"type\": \"" + FileTools.getFileExt(file.getOriginalFilename()) + "\", \"url\": \"" + fileUrl + "\", \"preCode\":\"" + filePreCode + "\"}";
        result = result.replaceAll("\\\\", "\\\\");
        return result;
    }

    /**
     * 去除状态为offline且没被用到的标签，保留online和（offline但被用到的）
     *
     * @param organCourseTagBos 产品标签
     * @param tagList           私有标签+公用标签
     */
    private Set<EpConstantTagPo> courseShowTags(List<OrganCourseTagBo> organCourseTagBos, List<EpConstantTagPo> tagList) {
        Set<EpConstantTagPo> tagSet = Sets.newHashSet();
        Set<Long> courseTagSetId = Sets.newHashSet();
        organCourseTagBos.forEach(courseTagBo -> {
            courseTagSetId.add(courseTagBo.getTagId());
        });
        tagList.forEach(tag -> {
            if (tag.getStatus().equals(EpConstantTagStatus.online)) {
                tagSet.add(tag);
            }
            if (tag.getStatus().equals(EpConstantTagStatus.offline)) {
                courseTagSetId.contains(tag.getId());
                tagSet.add(tag);
            }
        });
        return tagSet;
    }

    /**
     * 校验业务对象是否属于该机构，是：返回po;否：返回null
     *
     * @param sourceId
     * @param ognId
     * @return
     */
    private EpOrganCoursePo innerOgnOrPlatformReq(Long sourceId, Long ognId) {
        if (sourceId == null) {
            return null;
        }
        Optional<EpOrganCoursePo> optional = organCourseService.findById(sourceId);
        if (!optional.isPresent()) {
            return null;
        }
        if (ognId == null) {
            return optional.get();
        }
        if (optional.get().getOgnId().equals(ognId)) {
            return optional.get();
        } else {
            log.error(SpringComponent.messageSource("ERROR_ILLEGAL_RESOURCE"));
            return null;
        }
    }
}


