package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.pojo.bo.OrganCourseTagBo;
import com.ep.domain.pojo.po.EpMemberChildCommentPo;
import com.ep.domain.pojo.po.EpMemberChildTagPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpMemberChildCommentType;
import com.ep.domain.service.MemberChildCommentService;
import com.ep.domain.service.MemberChildTagService;
import com.ep.domain.service.OrganCourseTagService;
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
    private MemberChildCommentService memberChildCommentService;
    @Autowired
    private MemberChildTagService memberChildTagService;
    @Autowired
    private OrganCourseTagService organCourseTagService;

    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "courseName", required = false) String courseName,
                        @RequestParam(value = "childTrueName", required = false) String childTrueName,
                        @RequestParam(value = "className", required = false) String className,
                        @RequestParam(value = "classCatalogTitle", required = false) String classCatalogTitle,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        map.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        map.put("className", className);
        if (StringTools.isNotBlank(classCatalogTitle)) {
            conditions.add(EP_ORGAN_CLASS_CATALOG.CATALOG_TITLE.like("%" + classCatalogTitle + "%"));
        }
        map.put("classCatalogTitle", classCatalogTitle);
        if (StringTools.isNotBlank(childTrueName)) {
            conditions.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
        }
        map.put("childTrueName", childTrueName);

        conditions.add(EP_MEMBER_CHILD_COMMENT.TYPE.eq(EpMemberChildCommentType.launch));
        if (null != crStartTime) {
            conditions.add(EP_MEMBER_CHILD_COMMENT.CREATE_AT.greaterOrEqual(crStartTime));
        }
        map.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_MEMBER_CHILD_COMMENT.CREATE_AT.lessOrEqual(crEndTime));
        }
        map.put("crEndTime", crEndTime);

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
    public ResultDo updateComment(HttpServletRequest request,
                                  @RequestParam(value = "id") Long id,
                                  @RequestParam(value = "childId") Long childId,
                                  @RequestParam(value = "classCatalogId") Long classCatalogId,
                                  @RequestParam(value = "content") String content,
                                  @RequestParam(value = "tagId[]") List<Long> tagIds
    ) {
        EpSystemUserPo currentUser = super.getCurrentUser(request).get();
        Long ognId = currentUser.getOgnId();
        ResultDo resultDo = ResultDo.build();
        EpMemberChildCommentPo memberChildCommentPo = memberChildCommentService.findById(id);
        List<EpMemberChildTagPo> insertPos = Lists.newArrayList();
        tagIds.forEach(p -> {
            EpMemberChildTagPo po = new EpMemberChildTagPo();
            po.setChildId(childId);
            po.setOgnId(ognId);
            po.setCourseId(memberChildCommentPo.getCourseId());
            po.setClassId(memberChildCommentPo.getClassId());
            po.setClassCatalogId(classCatalogId);
            po.setTagId(p);
            insertPos.add(po);
        });
        memberChildCommentService.updateComment(id, content, childId, classCatalogId, insertPos);
        return resultDo;
    }


    /**
     * 初始化标签
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

    @PostMapping("updateChildTag")
    @ResponseBody
    public ResultDo updateChildTag(
            @RequestBody Long[] ids
    ) {
        ResultDo resultDo = ResultDo.build();
//        memberChildTagService.updateChildTag(ids);
        return resultDo;
    }
}
