package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.po.EpSystemDictPo;
import com.ep.domain.service.SystemDictService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import static com.ep.domain.repository.domain.Tables.EP_SYSTEM_DICT;

/**
 * @Description: 字典控制器
 * @Author: CC.F
 * @Date: 20:12 2018/6/5
 */
@Controller
@Slf4j
@RequestMapping("auth/dict")
public class SystemDictController extends BackendController {

    @Autowired
    private SystemDictService systemDictService;

    /**
     * 平台字典分页
     *
     * @return
     */
    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('platform:dict:index')")
    public String findbyPageAndCondition(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                         @RequestParam(value = "label", required = false) String label,
                                         @RequestParam(value = "groupName", required = false) String groupName,
                                         @RequestParam(value = "key", required = false) String key,
                                         @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                                         @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime) {
        Map searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(label)) {
            conditions.add(EP_SYSTEM_DICT.LABEL.eq(label));
        }
        searchMap.put("label", label);
        if (StringTools.isNotBlank(groupName)) {
            conditions.add(EP_SYSTEM_DICT.GROUP_NAME.eq(groupName));
        }
        searchMap.put("groupName", groupName);
        if (StringTools.isNotBlank(key)) {
            conditions.add(EP_SYSTEM_DICT.KEY.eq(key));
        }
        searchMap.put("key", key);

        if (null != crStartTime) {
            conditions.add(EP_SYSTEM_DICT.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_SYSTEM_DICT.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP_SYSTEM_DICT.DEL_FLAG.eq(false));
        Page<EpSystemDictPo> page = systemDictService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "systemDict/index";
    }
}
