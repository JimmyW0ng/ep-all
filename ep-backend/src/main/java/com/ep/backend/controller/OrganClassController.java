package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganClassBo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.service.OrganClassService;
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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN_CLASS;

/**
 * @Description: 开班控制器
 * @Author: CC.F
 * @Date: 9:30 2018/3/2
 */
@Controller
@RequestMapping("auth/organClass")
public class OrganClassController {
    @Autowired
    private OrganClassService organClassService;

    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "className", required = false) String className,
                        @RequestParam(value = "status", required = false) String status,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();

        if (StringTools.isNotBlank(className)) {
            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        map.put("className", className);
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP_ORGAN_CLASS.STATUS.eq(EpOrganClassStatus.valueOf(status)));
        }
        map.put("status", status);

        if (null != crStartTime) {
            conditions.add(EP_ORGAN_CLASS.CREATE_AT.greaterOrEqual(crStartTime));
        }
        map.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_ORGAN_CLASS.CREATE_AT.lessOrEqual(crEndTime));
        }
        map.put("crEndTime", crEndTime);

        Page<OrganClassBo> page = organClassService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "organClass/index";
    }

    /**
     * 上线课程
     * @param id
     * @return
     */
    @GetMapping("opening/{id}")
    @ResponseBody
    public ResultDo opening(@PathVariable(value="id") Long id){
        ResultDo resultDo = ResultDo.build();
        organClassService.openingById(id);
        return resultDo;
    }
}
