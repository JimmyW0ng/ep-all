package com.ep.backend.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.pojo.po.EpMemberChildTagPo;
import com.ep.domain.service.*;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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





    /**
     * 查看/修改评论初始化标签
     *
     * @param childId
     * @param courseId
     * @param classScheduleId
     * @return
     */
    @PostMapping("updateChildTagInit")
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
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
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
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
     * 删除评价
     *
     * @param id
     * @return
     */
    @PostMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:classSchedule:index')")
    @ResponseBody
    public ResultDo delete(@PathVariable("id") Long id) {
        return memberChildCommentService.deleteLogicLaunchById(id);
    }

}
