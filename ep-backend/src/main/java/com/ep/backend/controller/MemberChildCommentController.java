package com.ep.backend.controller;

import com.ep.common.tool.CollectionsTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.ep.domain.repository.domain.Tables.EP_MEMBER_CHILD_COMMENT;

/**
 * @Description: 孩子评论控制器
 * @Author: CC.F
 * @Date: 20:59 2018/2/25
 */
@Controller
@RequestMapping("auth/childComment")
public class MemberChildCommentController extends BackendController {
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
    @Autowired
    ApplicationEventPublisher publisher;

    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "courseId", required = false) Long courseId,
                        @RequestParam(value = "classId", required = false) Long classId,
                        @RequestParam(value = "classCatalogId", required = false) Long classCatalogId

    ) {
        Map<Object, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        //机构id
        Long ognId = super.getCurrentUser().get().getOgnId();
        Long mobile = super.getCurrentUser().get().getMobile();
        if (null == classCatalogId) {
            model.addAttribute("page", new PageImpl<MemberChildCommentBo>(new ArrayList<MemberChildCommentBo>()));
        } else {


            conditions.add(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch).or(EP_MEMBER_CHILD_COMMENT.TYPE.isNull()));
            Page<MemberChildCommentBo> page = memberChildCommentService.findbyPageAndCondition(classId, classCatalogId, pageable, conditions);
            model.addAttribute("page", page);
        }


        Optional<EpOrganAccountPo> organAccountOptioal = organAccountService.getByOgnIdAndReferMobile(ognId, mobile);
        //教师id
        model.addAttribute("organAccountId", organAccountOptioal.isPresent() ? organAccountOptioal.get().getId() : null);
        //课程下拉框
        List<EpOrganCoursePo> organCoursePos = organCourseService.findByOgnIdAndStatus(ognId, EpOrganCourseCourseStatus.online);
        Map<Long, String> courseMap = Maps.newHashMap();
        organCoursePos.forEach(p -> {
            courseMap.put(p.getId(), p.getCourseName());
        });
        model.addAttribute("courseMap", courseMap);
        //班次下拉框
        List<EpOrganClassPo> organClassPos = organClassService.findByCourseIdAndStatus(courseId, EpOrganClassStatus.opening);
        Map<Long, String> classMap = Maps.newHashMap();
        organClassPos.forEach(p -> {
            classMap.put(p.getId(), p.getClassName());
        });
        model.addAttribute("classMap", classMap);
        List<EpOrganClassCatalogPo> organClassCatalogPos = organClassCatalogService.findByClassId(classId);
        Map<Long, String> classCatalogMap = Maps.newHashMap();
        organClassCatalogPos.forEach(p -> {
            classCatalogMap.put(p.getId(), p.getCatalogTitle());
        });
        model.addAttribute("classCatalogMap", classCatalogMap);
        //查询条件
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
    @ResponseBody
    public ResultDo updateComment(@RequestParam(value = "id") Long id,
                                  @RequestParam(value = "childId") Long childId,
                                  @RequestParam(value = "classCatalogId") Long classCatalogId,
                                  @RequestParam(value = "content") String content,
                                  @RequestParam(value = "tagId[]", required = false) List<Long> tagIds
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        ResultDo resultDo = memberChildCommentService.updateComment(id, content, childId, classCatalogId, ognId, tagIds);
        //课时评价事件start
        ClassCatalogCommentEventBo eventPojo = new ClassCatalogCommentEventBo();
        eventPojo.setChildId(childId);
        eventPojo.setClassCatalogId(classCatalogId);
        eventPojo.setComment(content);
        eventPojo.setTagIds(tagIds);
        this.publisher.publishEvent(eventPojo);
        //课时评价事件end
        return resultDo;
    }


    /**
     * 初始化标签
     *
     * @param childId
     * @param courseId
     * @param classCatalogId
     * @return
     */
    @PostMapping("updateChildTagInit")
    @ResponseBody
    public ResultDo updateChildTagInit(
            @RequestParam(value = "childId") Long childId,
            @RequestParam(value = "courseId") Long courseId,
            @RequestParam(value = "classCatalogId") Long classCatalogId
    ) {
        ResultDo resultDo = ResultDo.build();
        Map<String, Object> map = Maps.newHashMap();
        //课程标签
        List<OrganCourseTagBo> organCourseTagBos = organCourseTagService.findBosByCourseId(courseId);


        List<EpMemberChildTagPo> memberChildTagPos = memberChildTagService.findByChildIdAndClassCatalogId(childId, classCatalogId);
        List<Long> list = Lists.newArrayList();
        map.put("organCourseTagBos", organCourseTagBos);
        map.put("memberChildTagPos", memberChildTagPos);
        resultDo.setResult(map);
        return resultDo;
    }

    /**
     * 改变课程获取班次下拉框
     *
     * @param courseId
     * @return
     */
    @GetMapping("changeCourse/{courseId}")
    @ResponseBody
    public ResultDo getClassByCourseId(@PathVariable("courseId") Long courseId) {
        List<EpOrganClassPo> organClassPos = organClassService.findByCourseId(courseId);
        if (CollectionsTools.isEmpty(organClassPos)) {
            return ResultDo.build().setResult(new HashMap<Long, String>());
        }
        Map<Long, String> classMap = Maps.newHashMap();
        organClassPos.forEach(p -> {
            classMap.put(p.getId(), p.getClassName());
        });
        return ResultDo.build().setResult(classMap);
    }

    /**
     * 改变班次获取班次目录下拉框
     *
     * @param classId
     * @return
     */
    @GetMapping("changeClass/{classId}")
    @ResponseBody
    public ResultDo getCatalogByClassId(@PathVariable("classId") Long classId) {
        List<EpOrganClassCatalogPo> classCatalogPos = organClassCatalogService.findByClassId(classId);
        if (CollectionsTools.isEmpty(classCatalogPos)) {
            return ResultDo.build().setResult(new HashMap<Long, String>());
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
