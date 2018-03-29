package com.ep.backend.controller;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.pojo.po.EpMemberChildTagPo;
import com.ep.domain.pojo.po.EpOrganClassCatalogPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.service.*;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


//    /**
//     * 修改评价(评论内容，标签)
//     *
//     * @param id
//     * @param content
//     * @return
//     */
//    @PostMapping("updateComment")
//    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
//    @ResponseBody
//    public ResultDo updateComment(@RequestParam(value = "id") Long id,
//                                  @RequestParam(value = "childId") Long childId,
//                                  @RequestParam(value = "classScheduleId") Long classScheduleId,
//                                  @RequestParam(value = "content") String content,
//                                  @RequestParam(value = "tagId[]", required = false) List<Long> tagIds
//    ) {
//        EpSystemUserPo currentUser = super.getCurrentUser().get();
//        Long ognId = currentUser.getOgnId();
//        ResultDo resultDo = memberChildCommentService.updateComment(id, content, childId, classScheduleId, ognId, tagIds);
//        //课时评价事件start
//        ClassCatalogCommentEventBo eventPojo = new ClassCatalogCommentEventBo();
//        eventPojo.setChildId(childId);
//        eventPojo.setClassScheduleId(classScheduleId);
//        eventPojo.setComment(content);
//        eventPojo.setTagIds(tagIds);
//        this.publisher.publishEvent(eventPojo);
//        //课时评价事件end
//        return resultDo;
//    }

    /**
     * 添加评价(评论内容，标签)
     *
     * @param content
     * @return
     */
//    @PostMapping("createComment")
//    @PreAuthorize("hasAnyAuthority('merchant:childComment:index')")
//    @ResponseBody
//    public ResultDo createComment(
//            @RequestParam(value = "childId") Long childId,
//            @RequestParam(value = "courseId") Long courseId,
//            @RequestParam(value = "classId") Long classId,
//            @RequestParam(value = "classScheduleId") Long classScheduleId,
//            @RequestParam(value = "content") String content,
//            @RequestParam(value = "tagId[]", required = false) List<Long> tagIds
//    ) {
//        EpSystemUserPo currentUser = super.getCurrentUser().get();
//        Long ognId = currentUser.getOgnId();
//        Long mobile = currentUser.getMobile();
//        ResultDo resultDo = memberChildCommentService.createCommentLaunch(childId, ognId, courseId, classId, classScheduleId, content, mobile);
////        ResultDo resultDo = memberChildCommentService.createCommentLaunchByOgn(childId, ognId, courseId, classId,
////                classCatalogId, content, currentUser.getId(), tagIds);tagIds
//        //课时评价事件start
//        ClassCatalogCommentEventBo eventPojo = new ClassCatalogCommentEventBo();
//        eventPojo.setChildId(childId);
//        eventPojo.setClassScheduleId(classScheduleId);
//        eventPojo.setComment(content);
//        eventPojo.setTagIds(tagIds);
//        this.publisher.publishEvent(eventPojo);
//        //课时评价事件end
//        return resultDo;
//    }


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

        //拥有标签
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


}
