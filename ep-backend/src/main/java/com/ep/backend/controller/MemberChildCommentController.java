package com.ep.backend.controller;

import com.ep.common.tool.CollectionsTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.pojo.dto.OrganClassScheduleDto;
import com.ep.domain.pojo.event.ClassCatalogCommentEventBo;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.repository.domain.enums.EpOrganCourseCourseStatus;
import com.ep.domain.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description: 孩子评论控制器
 * @Author: CC.F
 * @Date: 20:59 2018/2/25
 */
@Controller
@RequestMapping("auth/childComment")
public class MemberChildCommentController extends BackendController {
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    private MemberChildCommentService memberChildCommentService;
    @Autowired
    private MemberChildTagService memberChildTagService;
    @Autowired
    private OrganCourseTagService organCourseTagService;
    @Autowired
    private OrganCourseService organCourseService;
    @Autowired
    private OrganClassService organClassService;
    @Autowired
    private OrganClassCatalogService organClassCatalogService;
    @Autowired
    private OrganAccountService organAccountService;

    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
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

            Page<OrganClassScheduleDto> page = memberChildCommentService.findbyPageAndCondition(courseId, pageable, conditions);
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
        //班次下拉框
        EpOrganClassStatus[] statuses = new EpOrganClassStatus[]{EpOrganClassStatus.opening, EpOrganClassStatus.end};
        List<EpOrganClassPo> organClassPos = organClassService.findByCourseIdAndStatus(courseId, statuses);
//        Map<Long, String> classMap = Maps.newHashMap();
//        organClassPos.forEach(p -> {
//            classMap.put(p.getId(), p.getClassName());
//        });
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
        return "childComment/index";
    }

    /**
     * 修改评价(评论内容，标签)
     *
     * @param id
     * @param content
     * @return
     */
    @PostMapping("updateComment")
    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
    @ResponseBody
    public ResultDo updateComment(@RequestParam(value = "id") Long id,
                                  @RequestParam(value = "childId") Long childId,
                                  @RequestParam(value = "classScheduleId") Long classScheduleId,
                                  @RequestParam(value = "content") String content,
                                  @RequestParam(value = "tagId[]", required = false) List<Long> tagIds
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        ResultDo resultDo = memberChildCommentService.updateComment(id, content, childId, classScheduleId, ognId, tagIds);
        //课时评价事件start
        ClassCatalogCommentEventBo eventPojo = new ClassCatalogCommentEventBo();
        eventPojo.setChildId(childId);
        eventPojo.setClassScheduleId(classScheduleId);
        eventPojo.setComment(content);
        eventPojo.setTagIds(tagIds);
        this.publisher.publishEvent(eventPojo);
        //课时评价事件end
        return resultDo;
    }

    /**
     * 添加评价(评论内容，标签)
     *
     * @param content
     * @return
     */
    @PostMapping("createComment")
    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
    @ResponseBody
    public ResultDo createComment(
            @RequestParam(value = "childId") Long childId,
            @RequestParam(value = "courseId") Long courseId,
            @RequestParam(value = "classId") Long classId,
            @RequestParam(value = "classScheduleId") Long classScheduleId,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "tagId[]", required = false) List<Long> tagIds
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        Long mobile = currentUser.getMobile();
        ResultDo resultDo = memberChildCommentService.createCommentLaunch(childId, ognId, courseId, classId, classScheduleId, content, mobile);
        //课时评价事件start
        ClassCatalogCommentEventBo eventPojo = new ClassCatalogCommentEventBo();
        eventPojo.setChildId(childId);
        eventPojo.setClassScheduleId(classScheduleId);
        eventPojo.setComment(content);
        eventPojo.setTagIds(tagIds);
        this.publisher.publishEvent(eventPojo);
        //课时评价事件end
        return resultDo;
    }


    /**
     * 修改评论初始化标签
     *
     * @param childId
     * @param courseId
     * @param classScheduleId
     * @return
     */
    @PostMapping("updateChildTagInit")
    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
    @ResponseBody
    public ResultDo updateChildTagInit(
            @RequestParam(value = "childId") Long childId,
            @RequestParam(value = "courseId") Long courseId,
            @RequestParam(value = "classCatalogId") Long classScheduleId
    ) {
        ResultDo resultDo = ResultDo.build();
        Map<String, Object> map = Maps.newHashMap();
        //课程标签
        List<OrganCourseTagBo> organCourseTagBos = organCourseTagService.findBosByCourseId(courseId);


        List<EpMemberChildTagPo> memberChildTagPos = memberChildTagService.findByChildIdAndClassCatalogId(childId, classScheduleId);
        map.put("organCourseTagBos", organCourseTagBos);
        map.put("memberChildTagPos", memberChildTagPos);
        resultDo.setResult(map);
        return resultDo;
    }

    /**
     * 创建评论初始化标签
     *
     * @param courseId
     * @return
     */
    @PostMapping("createChildTagInit")
    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
    @ResponseBody
    public ResultDo createChildTagInit(
            @RequestParam(value = "courseId") Long courseId
    ) {
        ResultDo resultDo = ResultDo.build();
        //课程标签
        List<OrganCourseTagBo> organCourseTagBos = organCourseTagService.findBosByCourseId(courseId);
        resultDo.setResult(organCourseTagBos);
        return resultDo;
    }

    /**
     * 改变课程获取班次下拉框
     *
     * @param courseId
     * @return
     */
    @GetMapping("changeCourse/{courseId}")
    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
    @ResponseBody
    public ResultDo getClassByCourseId(@PathVariable("courseId") Long courseId) {
        EpOrganClassStatus[] statuses = new EpOrganClassStatus[]{EpOrganClassStatus.opening, EpOrganClassStatus.end};
        List<EpOrganClassPo> organClassPos = organClassService.findByCourseIdAndStatus(courseId, statuses);
        if (CollectionsTools.isEmpty(organClassPos)) {
            return ResultDo.build().setResult(new HashMap<Long, String>(0));
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
    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
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
     * 商户后台创建评论
     *
     * @param childId
     * @param courseId
     * @param classId
     * @param catalogId
     * @param content
     * @return
     */
    @PostMapping("createCommentLaunch")
    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
    @ResponseBody
    public ResultDo createComment(
            @RequestParam(value = "childId") Long childId,
            @RequestParam(value = "courseId") Long courseId,
            @RequestParam(value = "classId") Long classId,
            @RequestParam(value = "catalogId") Long catalogId,
            @RequestParam(value = "content") String content) {
        Long ognId = super.getCurrentUser().get().getOgnId();
        Long mobile = super.getCurrentUser().get().getMobile();
        return memberChildCommentService.createCommentLaunch(childId, ognId, courseId, classId, catalogId, content, mobile);
    }

}
