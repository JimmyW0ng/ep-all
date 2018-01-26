package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.SystemUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * Created by fcc on 2018/1/10.
 */
@RequestMapping("/auth/user")
@Controller
@Api(value = "后管用户")
public class SystemUserController {

    @Autowired
    private SystemUserService systemUserService;

    /**
     * 列表
     *
     * @return
     */
    @ApiOperation(value = "列表")
    @GetMapping("/index")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "mobile", required = false) String mobile
    ) {
        Map map= Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if(StringTools.isNotBlank(mobile)){
            conditions.add(EP.EP_SYSTEM_USER.MOBILE.like("%" + mobile + "%"));
        }
        map.put("mobile",mobile);
//        collections.add(Changfu.CHANGFU.CMS_ARTICLE.DEL_FLAG.eq(false));
//        if (StringUtils.isNotBlank(name)) {
//            Condition condition = Changfu.CHANGFU.CMS_ARTICLE.TITLE.like("%" + name + "%");
//            collections.add(condition);
//        }
//        if (null != categoryId) {
//            Condition condition = Changfu.CHANGFU.CMS_ARTICLE.CATEGORY_ID.eq(categoryId);
//            collections.add(condition);
//        }
//        if (null != crStartTime) {
//
//            Condition condition = Changfu.CHANGFU.CMS_ARTICLE.CREATE_AT.greaterOrEqual(crStartTime);
//            collections.add(condition);
//        }
//        if (null != crEndTime) {
//            Condition condition = Changfu.CHANGFU.CMS_ARTICLE.CREATE_AT.lessOrEqual(crEndTime);
//            collections.add(condition);
//        }

        Page<EpSystemUserPo> page = systemUserService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);


        return "systemUser/index";
    }

    /**
     * 新增用户
     *
     * @return
     */
    @ApiOperation(value = "新增用户")
    @PostMapping("/create")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String create(EpSystemUserPo po) {
        return "systemUser/index";
    }

}
