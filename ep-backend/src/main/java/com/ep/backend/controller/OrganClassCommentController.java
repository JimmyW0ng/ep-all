package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.bo.OrganClassCommentBo;
import com.ep.domain.service.OrganClassCommentService;
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

/**
 * @Description: 班次评价控制器
 * @Author: CC.F
 * @Date: 0:47 2018/3/18
 */
@Controller
@RequestMapping("auth/classComment")
public class OrganClassCommentController extends BackendController {
    @Autowired
    private OrganClassCommentService organClassCommentService;

    @GetMapping("index")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "ognName", required = false) String ognName,
                        @RequestParam(value = "courseName", required = false) String courseName,
                        @RequestParam(value = "className", required = false) String className,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(ognName)) {
            conditions.add(EP_ORGAN.OGN_NAME.like("%" + ognName + "%"));
        }
        searchMap.put("ognName", ognName);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        searchMap.put("className", className);
        if (null != crStartTime) {
            conditions.add(EP_ORGAN_CLASS_COMMENT.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_ORGAN_CLASS_COMMENT.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP_ORGAN_CLASS_COMMENT.DEL_FLAG.eq(false));
        Page<OrganClassCommentBo> page = organClassCommentService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "classComment/index";
    }
}
