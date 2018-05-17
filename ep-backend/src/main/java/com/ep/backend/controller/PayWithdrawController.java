package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.dto.ClassWithdrawQueryDto;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.service.OrganClassService;
import com.ep.domain.service.WechatPayWithdrawService;
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

import java.util.Collection;
import java.util.Map;

import static com.ep.domain.repository.domain.tables.EpOrganClass.EP_ORGAN_CLASS;
import static com.ep.domain.repository.domain.tables.EpOrganCourse.EP_ORGAN_COURSE;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 22:22 2018/5/16
 */
@Controller
@RequestMapping("auth/payWithdraw")
public class PayWithdrawController extends BackendController {

    @Autowired
    private WechatPayWithdrawService wechatPayWithdrawService;
    @Autowired
    private OrganClassService organClassService;

    @GetMapping("merchant/ClassWithdraw")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "courseName", required = false) String courseName,
                        @RequestParam(value = "className", required = false) String className) {

        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.eq(courseName));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.eq(className));
        }
        searchMap.put("className", className);
//
//
        conditions.add(EP_ORGAN_CLASS.OGN_ID.eq(this.getCurrentUserOgnId()));
        conditions.add(EP_ORGAN_CLASS.STATUS.in(EpOrganClassStatus.opening, EpOrganClassStatus.end));
        conditions.add(EP_ORGAN_CLASS.DEL_FLAG.eq(false));
//
        Page<ClassWithdrawQueryDto> page = organClassService.findClassWithdrawQueryDtoByPage(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "payWithdraw/ClassWithdraw";
    }
}
