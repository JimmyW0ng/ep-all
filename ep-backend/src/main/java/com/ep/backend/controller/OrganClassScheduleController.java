package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganClassBespeakScheduleBo;
import com.ep.domain.pojo.dto.OrganClassScheduleDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.enums.EpOrganClassScheduleStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 班次行程控制器
 * @Author: CC.F
 * @Date: 16:04 2018/3/28/028
 */
@Slf4j
@Controller
@RequestMapping("auth/classSchedule")
public class OrganClassScheduleController extends BackendController {

    @Autowired
    private OrganClassScheduleService organClassScheduleService;
    @Autowired
    private OrganAccountService organAccountService;
    @Autowired
    private OrganCourseService organCourseService;
    @Autowired
    private OrganClassService organClassService;
    @Autowired
    private OrganClassCatalogService organClassCatalogService;

    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "courseId", required = false) Long courseId,
                        @RequestParam(value = "classId", required = false) Long classId,
                        @RequestParam(value = "classCatalogId", required = false) Long classCatalogId,
                        @RequestParam(value = "childNickName", required = false) String childNickName,
                        @RequestParam(value = "childTrueName", required = false) String childTrueName

    ) {
        Map<Object, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        //机构id
        Long ognId = super.getCurrentUser().get().getOgnId();
        Long mobile = super.getCurrentUser().get().getMobile();
        if (null == classId) {
            model.addAttribute("page", new PageImpl<OrganClassScheduleDto>(new ArrayList<OrganClassScheduleDto>()));
        } else {
            if (StringTools.isNotBlank(childNickName)) {
                conditions.add(EP_MEMBER_CHILD.CHILD_NICK_NAME.eq(childNickName));
            }
            searchMap.put("childNickName", childNickName);
            if (StringTools.isNotBlank(childTrueName)) {
                conditions.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME.eq(childTrueName));
            }
            searchMap.put("childTrueName", childTrueName);
            conditions.add(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch).or(EP_MEMBER_CHILD_COMMENT.TYPE.isNull()));
            if (null != classCatalogId) {
                conditions.add(EP_ORGAN_CLASS_SCHEDULE.CLASS_CATALOG_ID.eq(classCatalogId));
            }
            if (null != classId) {
                conditions.add(EP_ORGAN_CLASS_SCHEDULE.CLASS_ID.eq(classId));
            }

            Page<OrganClassScheduleDto> page = organClassScheduleService.findbyPageAndCondition(courseId, pageable, conditions);
            model.addAttribute("page", page);
        }


        Optional<EpOrganAccountPo> organAccountOptioal = organAccountService.getByOgnIdAndReferMobile(ognId, mobile);
        //教师id
        model.addAttribute("organAccountId", organAccountOptioal.isPresent() ? organAccountOptioal.get().getId() : null);
        //班次负责人id
        Optional<EpOrganClassPo> organClassOptioal = organClassService.findById(classId);
        model.addAttribute("classOgnAccountId", organClassOptioal.isPresent() ? organClassOptioal.get().getOgnAccountId() : null);

        //课程下拉框
        List<EpOrganCoursePo> organCoursePos = organCourseService.findByOgnIdAndStatus(ognId, EpOrganCourseCourseStatus.online);
        Map<Long, String> courseMap = Maps.newHashMap();
        organCoursePos.forEach(p -> {
            courseMap.put(p.getId(), p.getCourseName());
        });
        model.addAttribute("courseMap", courseMap);
        //班次下拉框，获取opening/end的正常班次和normal/end的预约班次,提供给随堂评价页面
        List<EpOrganClassPo> organClassPos = organClassService.findProceedClassByCourseId(courseId);

        model.addAttribute("organClassPos", organClassPos);
        List<EpOrganClassCatalogPo> organClassCatalogPos = organClassCatalogService.findByClassId(classId);
        Map<Long, String> classCatalogMap = Maps.newHashMap();
        organClassCatalogPos.forEach(p -> {
            classCatalogMap.put(p.getId(), p.getCatalogTitle());
        });
        model.addAttribute("classCatalogMap", classCatalogMap);
        //查询条件
        searchMap.put("courseId", courseId);
        searchMap.put("classId", classId);
        searchMap.put("classCatalogId", classCatalogId);
        model.addAttribute("searchMap", searchMap);
        return "classSchedule/index";
    }

    /**
     * 改变课程获取班次下拉框
     *
     * @param courseId
     * @return
     */
    @GetMapping("changeCourse/{courseId}")
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
    @ResponseBody
    public ResultDo getClassByCourseId(@PathVariable("courseId") Long courseId) {
        //班次下拉框，获取opening/end的正常班次和normal/end的预约班次,提供给随堂评价页面
        List<EpOrganClassPo> organClassPos = organClassService.findProceedClassByCourseId(courseId);
        if (CollectionsTools.isEmpty(organClassPos)) {
            return ResultDo.build();
        }

        return ResultDo.build().setResult(organClassPos);
    }

    /**
     * 改变班次获取班次目录下拉框
     *
     * @param classId
     * @return
     */
    @GetMapping("changeClass/{classId}")
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
    @ResponseBody
    public ResultDo getCatalogByClassId(@PathVariable("classId") Long classId) {
        List<EpOrganClassCatalogPo> classCatalogPos = organClassCatalogService.findByClassId(classId);
        if (CollectionsTools.isEmpty(classCatalogPos)) {
            return ResultDo.build().setResult(new HashMap<Long, String>(0));
        }
        Map<Long, String> classCatalogMap = Maps.newHashMap();
        classCatalogPos.forEach(p -> {
            classCatalogMap.put(p.getId(), p.getCatalogTitle());
        });
        return ResultDo.build().setResult(classCatalogMap);
    }

    /**
     * 新增行程
     *
     * @param po
     * @return
     */
    @PostMapping("createSchedule")
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
    @ResponseBody
    public ResultDo createSchedule(EpOrganClassSchedulePo po) {
        return organClassScheduleService.createSchedule(po);
    }

    /**
     * 修改行程
     *
     * @param po
     * @return
     */
    @PostMapping("updateSchedule")
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
    @ResponseBody
    public ResultDo updateSchedule(EpOrganClassSchedulePo po) {
        return organClassScheduleService.updateSchedule(po);
    }


    /**
     * 修改行程
     *
     * @param orderId
     * @return
     */
    @GetMapping("bespeakInit/{orderId}")
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
    @ResponseBody
    public ResultDo bespeakInit(@PathVariable("orderId") Long orderId) {
        List<OrganClassBespeakScheduleBo> bos = organClassScheduleService.findBespeakScheduleBoByOrderId(orderId);
        if (bos.isEmpty()) {
            ResultDo.build();
        }
        bos.forEach(bo -> {
            Timestamp startTime = bo.getStartTime();
            if (DateTools.compareTwoTime(DateTools.getCurrentDateTime(), startTime) != -1) {
                bo.setRectifyFlag(false);
            } else {
                boolean flag = (DateTools.getTwoTimeDiffSecond(startTime, DateTools.getCurrentDateTime()) / BizConstant.TIME_UNIT) >=
                        BizConstant.RECTIFY_SCHEDULE_STARTTIME_TONOW_LT30;
                bo.setRectifyFlag(flag);
            }
        });
        return ResultDo.build().setResult(bos);
    }

    @PostMapping("changeClassScheduleStatus")
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
    @ResponseBody
    public ResultDo changeClassScheduleStatus(@RequestParam(value = "id") Long id,
                                              @RequestParam(value = "status") String status,
                                              @RequestParam(value = "classId") Long classId
    ) {
        if (null == this.innerOgnOrPlatformReq(classId, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        return organClassScheduleService.changeClassScheduleStatus(id, EpOrganClassScheduleStatus.valueOf(status));
    }

    /**
     * 校验业务对象是否属于该机构，是：返回po;否：返回null
     *
     * @param sourceId
     * @param ognId
     * @return
     */
    private EpOrganClassPo innerOgnOrPlatformReq(Long sourceId, Long ognId) {
        if (sourceId == null) {
            return null;
        }
        Optional<EpOrganClassPo> optional = organClassService.findById(sourceId);
        if (!optional.isPresent()) {
            return null;
        }
        if (optional.get().getOgnId().equals(ognId)) {
            return optional.get();
        } else {
            log.error(SpringComponent.messageSource("ERROR_ILLEGAL_RESOURCE"));
            return null;
        }
    }
}
