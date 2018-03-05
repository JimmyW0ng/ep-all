package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.bo.MemberChildHonorBo;
import com.ep.domain.service.MemberChildHonorService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Collection;
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

    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "childTrueName", required = false) String childTrueName,
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

        Page<MemberChildHonorBo> page = memberChildHonorService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "childHonor/index";
    }
}
