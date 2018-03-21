package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganVipBo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.OrganVipService;
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

import static com.ep.domain.repository.domain.Tables.*;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 23:54 2018/3/19
 */
@Controller
@RequestMapping("auth/organVip")
public class OrganVipController extends BackendController {
    @Autowired
    private OrganVipService organVipService;

    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "mobile", required = false) Long mobile,
                        @RequestParam(value = "childNickName", required = false) String childNickName,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();

        if (StringTools.isNotBlank(childNickName)) {
            conditions.add(EP_MEMBER_CHILD.CHILD_NICK_NAME.like("%" + childNickName + "%"));
        }
        searchMap.put("childNickName", childNickName);
        if (null != mobile) {
            conditions.add(EP_MEMBER.MOBILE.eq(mobile));
        }
        searchMap.put("mobile", mobile);

        if (null != crStartTime) {
            conditions.add(EP_ORGAN_VIP.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_ORGAN_VIP.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        Long ognId = super.getCurrentUser().get().getOgnId();
        conditions.add(EP_ORGAN_VIP.OGN_ID.eq(ognId));
        Page<OrganVipBo> page = organVipService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "organVip/index";
    }

    /**
     * 新增初始化
     *
     * @return
     */
    @GetMapping("/createInit")
    public String createInit(Model model) {

        model.addAttribute("organVipBo", new OrganVipBo());
        return "organVip/form";
    }

    /**
     * 新增
     *
     * @return
     */
    @PostMapping("/create")
    @ResponseBody
    public ResultDo create(OrganVipBo bo) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        bo.setOgnId(currentUser.getOgnId());
//        return organVipService.createOgnVip(bo);
        return ResultDo.build();
    }
}
