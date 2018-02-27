package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.bo.MemberChildCommentBo;
import com.ep.domain.pojo.bo.OrderBo;
import com.ep.domain.pojo.po.EpMemberChildCommentPo;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.service.ChildCommentService;
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

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * @Description: 孩子评论控制器
 * @Author: CC.F
 * @Date: 20:59 2018/2/25
 */
@Controller
@RequestMapping("auth/comment")
public class ChildCommentController extends BackendController {
    @Autowired
    private ChildCommentService childCommentService;

    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
//                        @RequestParam(value = "mobile", required = false) String mobile,
//                        @RequestParam(value = "childTrueName", required = false) String childTrueName,
//                        @RequestParam(value = "courseName", required = false) String courseName,
//                        @RequestParam(value = "className", required = false) String className,
//                        @RequestParam(value = "status", required = false) String status,
//                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
//                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
//        if (StringTools.isNotBlank(mobile)) {
//            conditions.add(EP.EP_MEMBER.MOBILE.eq(Long.parseLong(mobile)));
//        }
//        map.put("mobile", mobile);
//        if (StringTools.isNotBlank(childTrueName)) {
//            conditions.add(EP.EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
//        }
//        map.put("childTrueName", childTrueName);
//        if (StringTools.isNotBlank(courseName)) {
//            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
//        }
//        map.put("courseName", courseName);
//        if (StringTools.isNotBlank(className)) {
//            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
//        }
//        map.put("className", className);
//        if (StringTools.isNotBlank(status)) {
//            conditions.add(EP.EP_ORDER.STATUS.eq(EpOrderStatus.valueOf(status)));
//        }
//        map.put("status", status);
//        if (null != crStartTime) {
//            conditions.add(EP.EP_ORDER.CREATE_AT.greaterOrEqual(crStartTime));
//        }
//        map.put("crStartTime", crStartTime);
//        if (null != crEndTime) {
//            conditions.add(EP.EP_ORDER.CREATE_AT.lessOrEqual(crEndTime));
//        }
//        map.put("crEndTime", crEndTime);

        Page<MemberChildCommentBo> page = childCommentService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "childComment/index";
    }

}
