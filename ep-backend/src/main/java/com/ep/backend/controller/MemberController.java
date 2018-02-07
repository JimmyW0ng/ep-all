package com.ep.backend.controller;

import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.service.MemberService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
//                         , @RequestParam(value = "mobile", required = false) String mobile,
//                        @RequestParam(value = "type", required = false) String type,
//                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
//                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();

        conditions.add(EP.EP_MEMBER.DEL_FLAG.eq(false));
        Page<EpMemberPo> page = memberService.getByPage(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);

        return "member/index";
    }
}
