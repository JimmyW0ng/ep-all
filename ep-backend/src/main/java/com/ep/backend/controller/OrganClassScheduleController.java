package com.ep.backend.controller;

import com.ep.domain.service.OrganAccountService;
import com.ep.domain.service.OrganClassScheduleService;
import com.ep.domain.service.OrganCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 班次行程控制器
 * @Author: CC.F
 * @Date: 16:04 2018/3/28/028
 */
@Controller
@RequestMapping("auth/classSchedule")
public class OrganClassScheduleController extends BackendController {

    @Autowired
    private OrganClassScheduleService organClassScheduleService;
    @Autowired
    private OrganAccountService organAccountService;
    @Autowired
    private OrganCourseService organCourseService;

//    @GetMapping("index")
//    @PreAuthorize("hasAnyAuthority('merchant:childSchedule:index')")
//    public String index(Model model,
//                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
//                        @RequestParam(value = "courseId", required = false) Long courseId,
//                        @RequestParam(value = "classId", required = false) Long classId,
//                        @RequestParam(value = "classCatalogId", required = false) Long classCatalogId,
//                        @RequestParam(value = "childNickName", required = false) String childNickName,
//                        @RequestParam(value = "childTrueName", required = false) String childTrueName
//
//    ) {
//        Map<Object, Object> searchMap = Maps.newHashMap();
//        Collection<Condition> conditions = Lists.newArrayList();
//        //机构id
//        Long ognId = super.getCurrentUser().get().getOgnId();
//        Long mobile = super.getCurrentUser().get().getMobile();
//        if (null == classCatalogId || null == classId) {
//            model.addAttribute("page", new PageImpl<MemberChildCommentBo>(new ArrayList<MemberChildCommentBo>()));
//        } else {
//            if (StringTools.isNotBlank(childNickName)) {
//                conditions.add(EP_MEMBER_CHILD.CHILD_NICK_NAME.eq(childNickName));
//            }
//            searchMap.put("childNickName", childNickName);
//            if (StringTools.isNotBlank(childTrueName)) {
//                conditions.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME.eq(childTrueName));
//            }
//            searchMap.put("childTrueName", childTrueName);
////            conditions.add(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch).or(EP_MEMBER_CHILD_COMMENT.TYPE.isNull()));
////            Page<MemberChildCommentBo> page = memberChildCommentService.findbyPageAndCondition(courseId, classId, classCatalogId, pageable, conditions);
////            model.addAttribute("page", page);
//        }
//
//
//        Optional<EpOrganAccountPo> organAccountOptioal = organAccountService.getByOgnIdAndReferMobile(ognId, mobile);
//        //教师id
//        model.addAttribute("organAccountId", organAccountOptioal.isPresent() ? organAccountOptioal.get().getId() : null);
//        //班次负责人id
//        Optional<EpOrganClassPo> organClassOptioal = organClassService.findById(classId);
//        model.addAttribute("classOgnAccountId", organClassOptioal.isPresent() ? organClassOptioal.get().getOgnAccountId() : null);
//
//        //课程下拉框
//        List<EpOrganCoursePo> organCoursePos = organCourseService.findByOgnIdAndStatus(ognId, EpOrganCourseCourseStatus.online);
//        Map<Long, String> courseMap = Maps.newHashMap();
//        organCoursePos.forEach(p -> {
//            courseMap.put(p.getId(), p.getCourseName());
//        });
//        model.addAttribute("courseMap", courseMap);
//        //班次下拉框
//        EpOrganClassStatus[] statuses = new EpOrganClassStatus[]{EpOrganClassStatus.opening, EpOrganClassStatus.end};
//        List<EpOrganClassPo> organClassPos = organClassService.findByCourseIdAndStatus(courseId, statuses);
//        Map<Long, String> classMap = Maps.newHashMap();
//        organClassPos.forEach(p -> {
//            classMap.put(p.getId(), p.getClassName());
//        });
//        model.addAttribute("classMap", classMap);
//        List<EpOrganClassCatalogPo> organClassCatalogPos = organClassCatalogService.findByClassId(classId);
//        Map<Long, String> classCatalogMap = Maps.newHashMap();
//        organClassCatalogPos.forEach(p -> {
//            classCatalogMap.put(p.getId(), p.getCatalogTitle());
//        });
//        model.addAttribute("classCatalogMap", classCatalogMap);
//        //查询条件
//        searchMap.put("courseId", courseId);
//        searchMap.put("classId", classId);
//        searchMap.put("classCatalogId", classCatalogId);
//        model.addAttribute("searchMap", searchMap);
//        return "childComment/index";
//    }

}
