package com.ep.backend.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.service.MemberService;
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
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import static com.ep.domain.repository.domain.Tables.EP_MEMBER;

/**
 * @Description: 后台会员信息控制器
 * @Author: CC.F
 * @Date: 17:57 2018/2/6
 */
@Controller
@Slf4j
@RequestMapping("auth/member")
public class MemberController extends BackendController {
    @Autowired
    private MemberService memberService;

    /**
     * 会员列表
     *
     * @return
     */
    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('platform:member:index')")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
            , @RequestParam(value = "mobile", required = false) Long mobile,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (null != mobile) {
            conditions.add(EP_MEMBER.MOBILE.eq(mobile));
        }
        searchMap.put("mobile", mobile);
        if (null != crStartTime) {
            conditions.add(EP_MEMBER.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_MEMBER.CREATE_AT.lessOrEqual(crEndTime));
        }
        conditions.add(EP_MEMBER.DEL_FLAG.eq(false));
        Page<EpMemberPo> page = memberService.getByPage(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);

        return "member/index";
    }


    /**
     * 冻结平台会员
     *
     * @param id
     * @return
     */
    @GetMapping("freeze/{id}")
    @PreAuthorize("hasAnyAuthority('platform:member:index')")
    @ResponseBody
    public ResultDo freeze(@PathVariable("id") Long id) {
        return memberService.freezeById(id);
    }

    /**
     * 解冻平台会员
     *
     * @param id
     * @return
     */
    @GetMapping("unfreeze/{id}")
    @PreAuthorize("hasAnyAuthority('platform:member:index')")
    @ResponseBody
    public ResultDo unfreeze(@PathVariable("id") Long id) {
        return memberService.unfreezeById(id);
    }

    /**
     * 解冻平台会员
     *
     * @param id
     * @return
     */
    @GetMapping("cancel/{id}")
    @PreAuthorize("hasAnyAuthority('platform:member:index')")
    @ResponseBody
    public ResultDo cancel(@PathVariable("id") Long id) {
        return memberService.cancelById(id);
    }
}
