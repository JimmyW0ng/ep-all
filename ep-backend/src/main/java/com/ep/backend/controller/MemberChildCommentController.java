package com.ep.backend.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.pojo.event.ClassCatalogCommentEventBo;
import com.ep.domain.pojo.po.EpMemberChildTagPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.MemberChildCommentService;
import com.ep.domain.service.MemberChildTagService;
import com.ep.domain.service.OrganCourseTagService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    ApplicationEventPublisher publisher;

    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
//                        @RequestParam(value = "courseName", required = false) String courseName,
//                        @RequestParam(value = "childTrueName", required = false) String childTrueName,
//                        @RequestParam(value = "className", required = false) String className,
//                        @RequestParam(value = "classCatalogTitle", required = false) String classCatalogTitle,
                        @RequestParam(value = "classCatalogId") Long classCatalogId
//                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
//                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
//        if (StringTools.isNotBlank(courseName)) {
//            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
//        }
//        map.put("courseName", courseName);
//        if (StringTools.isNotBlank(className)) {
//            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
//        }
//        map.put("className", className);
//        if (StringTools.isNotBlank(classCatalogTitle)) {
//            conditions.add(EP_ORGAN_CLASS_CATALOG.CATALOG_TITLE.like("%" + classCatalogTitle + "%"));
//        }
//        map.put("classCatalogTitle", classCatalogTitle);
//        if (StringTools.isNotBlank(childTrueName)) {
//            conditions.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
//        }
//        map.put("childTrueName", childTrueName);
//
//        conditions.add(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch));
//        if (null != crStartTime) {
//            conditions.add(EP_MEMBER_CHILD_COMMENT.CREATE_AT.greaterOrEqual(crStartTime));
//        }
//        map.put("crStartTime", crStartTime);
//        if (null != crEndTime) {
//            conditions.add(EP_MEMBER_CHILD_COMMENT.CREATE_AT.lessOrEqual(crEndTime));
//        }
//        map.put("crEndTime", crEndTime);
        Long ognId = super.getCurrentUser().get().getOgnId();
        conditions.add(EP_MEMBER_CHILD_COMMENT.OGN_ID.eq(ognId));
        conditions.add(EP_MEMBER_CHILD_COMMENT.OGN_ID.eq(ognId));
        Page<MemberChildCommentBo> page = memberChildCommentService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
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

}
