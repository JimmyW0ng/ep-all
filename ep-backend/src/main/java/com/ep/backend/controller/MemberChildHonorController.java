package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.MemberChildHonorBo;
import com.ep.domain.pojo.bo.OrganClassChildBo;
import com.ep.domain.pojo.po.EpMemberChildHonorPo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.MemberChildHonorService;
import com.ep.domain.service.OrganClassChildService;
import com.ep.domain.service.OrganClassService;
import com.ep.domain.service.OrganCourseService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Tables.*;
import static com.ep.domain.repository.domain.tables.EpOrganCourse.EP_ORGAN_COURSE;

/**
 * @Description: 孩子荣誉控制器
 * @Author: CC.F
 * @Date: 23:48 2018/3/5
 */
@Controller
@RequestMapping("auth/childHonor")
public class MemberChildHonorController extends BackendController {
    @Autowired
    private MemberChildHonorService memberChildHonorService;
    @Autowired
    private OrganCourseService organCourseService;
    @Autowired
    private OrganClassService organClassService;
    @Autowired
    private OrganClassChildService organClassChildService;

    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "childTrueName", required = false) String childTrueName,
                        @RequestParam(value = "childNickName", required = false) String childNickName,
                        @RequestParam(value = "courseName", required = false) String courseName,
                        @RequestParam(value = "className", required = false) String className,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();

        if (StringTools.isNotBlank(childTrueName)) {
            conditions.add(EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
        }
        map.put("childTrueName", childTrueName);
        if (StringTools.isNotBlank(childNickName)) {
            conditions.add(EP_MEMBER_CHILD.CHILD_NICK_NAME.like("%" + childNickName + "%"));
        }
        map.put("childNickName", childNickName);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        map.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        map.put("className", className);

        if (null != crStartTime) {
            conditions.add(EP_MEMBER_CHILD_HONOR.CREATE_AT.greaterOrEqual(crStartTime));
        }
        map.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_MEMBER_CHILD_HONOR.CREATE_AT.lessOrEqual(crEndTime));
        }
        map.put("crEndTime", crEndTime);
        conditions.add(EP_MEMBER_CHILD_HONOR.DEL_FLAG.eq(false));
        Long ognId = super.getCurrentUser().get().getOgnId();
        conditions.add(EP_MEMBER_CHILD_HONOR.OGN_ID.eq(ognId));
        Page<MemberChildHonorBo> page = memberChildHonorService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "childHonor/index";
    }

    /**
     * 创建初始化
     *
     * @param model
     * @param request
     * @return
     */
    @GetMapping("createInit")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    public String createInit(Model model, HttpServletRequest request) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        List<EpOrganCoursePo> organCoursePos = organCourseService.findByOgnId(ognId);
        Map<Long, String> courseMap = Maps.newHashMap();
        organCoursePos.forEach(po -> {
            courseMap.put(po.getId(), po.getCourseName());
        });
        model.addAttribute("courseMap", courseMap);
        model.addAttribute("memberChildHonerPo", new MemberChildHonorBo());
        return "childHonor/form";
    }

    @GetMapping("findClassByCourseId/{courseId}")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    @ResponseBody
    public ResultDo findClassByCourseId(@PathVariable("courseId") Long courseId) {
        ResultDo resultDo = ResultDo.build();
        List<EpOrganClassPo> organClassPos = organClassService.findByCourseId(courseId);
        Map<Long, String> classMap = Maps.newHashMap();
        organClassPos.forEach(po -> {
            classMap.put(po.getId(), po.getClassName());
        });
        resultDo.setResult(classMap);
        return resultDo;
    }


    @GetMapping("findClildByClassId/{classId}")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    @ResponseBody
    public ResultDo findClildByClassId(@PathVariable("classId") Long classId) {
        ResultDo resultDo = ResultDo.build();
        List<OrganClassChildBo> bos = organClassChildService.findChildMapByClassId(classId);
        Map<Long, String> childMap = Maps.newHashMap();
        bos.forEach(bo -> {
            childMap.put(bo.getChildId(), bo.getChildTrueName());
        });
        resultDo.setResult(childMap);
        return resultDo;
    }

    @GetMapping("findNicknameByClassId/{classId}")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    @ResponseBody
    public ResultDo findNicknameByClassId(@PathVariable("classId") Long classId) {
        ResultDo resultDo = ResultDo.build();
        List<OrganClassChildBo> bos = organClassChildService.findNicknameMapByClassId(classId);
        Map<Long, String> childMap = Maps.newHashMap();
        bos.forEach(bo -> {
            childMap.put(bo.getChildId(), bo.getChildNickName());
        });
        resultDo.setResult(childMap);
        return resultDo;
    }

    /**
     * 创建孩子荣誉
     *
     * @param po
     * @return
     */
    @PostMapping("create")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    @ResponseBody
    public ResultDo create(EpMemberChildHonorPo po) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        Long ognId = currentUser.getOgnId();
        ResultDo resultDo = ResultDo.build();
        po.setOgnId(ognId);
        memberChildHonorService.createHonor(po);
        return resultDo;
    }

    /**
     * 修改孩子荣誉
     * @param po
     * @return
     */
    @PostMapping("update")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    @ResponseBody
    public ResultDo update(EpMemberChildHonorPo po) {
        ResultDo resultDo = ResultDo.build();
        memberChildHonorService.updateHonor(po);
        return resultDo;
    }

    /**
     * 查看
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("view/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    public String view(Model model, @PathVariable("id") Long id) {
        EpMemberChildHonorPo bo = memberChildHonorService.findBoById(id);
        model.addAttribute("memberChildHonerPo", bo);
        return "childHonor/view";
    }

    /**
     * 修改初始化
     * @param model
     * @param id
     * @return
     */
    @GetMapping("updateInit/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    public String updateInit(Model model, @PathVariable("id") Long id) {
        EpMemberChildHonorPo bo = memberChildHonorService.findBoById(id);
        model.addAttribute("memberChildHonerPo", bo);
        return "childHonor/form";
    }

    /**
     * 删除孩子荣誉
     * @param id
     * @return
     */
    @GetMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('merchant:childHonor:index')")
    @ResponseBody
    public ResultDo delete(@PathVariable("id") Long id) {
        return memberChildHonorService.deleteById(id);
    }
}
