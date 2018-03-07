package com.ep.backend.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.OrganAccountService;
import com.ep.domain.service.OrganService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * @Description: 机构账户关联信息控制器
 * @Author: CC.F
 * @Date: 9:42 2018/2/6
 */
@Controller
@RequestMapping("auth/organAccount")
public class OrganAccountController extends BackendController {

    @Autowired
    private OrganAccountService organAccountService;
    @Autowired
    private OrganService organService;

    /**
     * 机构账户关联信息列表
     *
     * @return
     */
    @ApiOperation(value = "列表")
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
        conditions.add(EP.EP_ORGAN_ACCOUNT.DEL_FLAG.eq(false));
        Long ognId = super.getCurrentUser().get().getOgnId();
        conditions.add(EP.EP_ORGAN_ACCOUNT.OGN_ID.eq(ognId));
        Page<OrganAccountBo> page = organAccountService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);


        return "organAccount/index";
    }

    /**
     * 查看
     *
     * @return
     */
    @GetMapping("/view/{id}")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String view(Model model,@PathVariable("id")Long id) {

        model.addAttribute("organAccountPo", organAccountService.getById(id));
        return "/organAccount/view";
    }

    /**
     * 新增初始化
     *
     * @return
     */
    @ApiOperation(value = "新增初始化")
    @GetMapping("/createInit")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String createInit(Model model) {

        model.addAttribute("organAccountPo", new EpOrganAccountPo());
        return "/organAccount/form";
    }

    /**
     * 修改初始化
     *
     * @return
     */
    @ApiOperation(value = "修改初始化")
    @GetMapping("/updateInit/{id}")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String updateInit(Model model,@PathVariable("id")Long id) {

        model.addAttribute("organAccountPo",organAccountService.getById(id));
        return "/organAccount/form";
    }

    /**
     * 新增
     *
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping("/create")
    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public ResultDo create(EpOrganAccountPo po) {
        EpSystemUserPo currentUser = super.getCurrentUser().get();
        po.setOgnId(currentUser.getOgnId());
        ResultDo resultDo=ResultDo.build();
        organAccountService.create(po);
        return resultDo;
    }

    /**
     * 修改
     *
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public ResultDo update(EpOrganAccountPo po) {
        ResultDo resultDo=ResultDo.build();
        organAccountService.update(po);
        return resultDo;
    }

    /**
     * 删除
     *
     * @return
     */
    @GetMapping("/delete/{id}")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    @ResponseBody
    public ResultDo updateInit(@PathVariable("id")Long id) {
        ResultDo resultDo = ResultDo.build();
        organAccountService.delete(id);

        return resultDo;
    }
}
