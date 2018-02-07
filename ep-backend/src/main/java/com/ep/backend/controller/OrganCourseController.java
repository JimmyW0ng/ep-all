package com.ep.backend.controller;

import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.bo.OrganCourseBo;
import com.ep.domain.service.OrganCourseService;
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

import java.util.Collection;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * @Description: 机构课程控制器
 * @Author: CC.F
 * @Date: 15:46 2018/2/6
 */
@Controller
@RequestMapping("auth/organCourse")
public class OrganCourseController extends BackendController {
    @Autowired
    private OrganCourseService organCourseService;

    @GetMapping("index")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
//                         , @RequestParam(value = "mobile", required = false) String mobile,
//                        @RequestParam(value = "type", required = false) String type,
//                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
//                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
//        if (StringTools.isNotBlank(mobile)) {
//            conditions.add(EP.EP_SYSTEM_USER.MOBILE.eq(Long.parseLong(mobile)));
//        }
//        map.put("mobile", mobile);
//        if (StringTools.isNotBlank(type)) {
//            conditions.add(EP.EP_SYSTEM_USER.TYPE.eq(EpSystemUserType.valueOf(type)));
//        }
//        map.put("type", type);
//
//        if (null != crStartTime) {
//            conditions.add(EP.EP_SYSTEM_USER.CREATE_AT.greaterOrEqual(crStartTime));
//        }
//        map.put("crStartTime", crStartTime);
//        if (null != crEndTime) {
//            conditions.add(EP.EP_SYSTEM_USER.CREATE_AT.lessOrEqual(crEndTime));
//        }
//        map.put("crEndTime", crEndTime);
        conditions.add(EP.EP_ORGAN_COURSE.DEL_FLAG.eq(false));
        Page<OrganCourseBo> page = organCourseService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "/organCourse/index";
    }
}
