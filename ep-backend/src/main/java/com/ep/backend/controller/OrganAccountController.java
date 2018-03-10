package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrganAccountBo;
import com.ep.domain.pojo.po.EpOrganAccountPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpOrganAccountStatus;
import com.ep.domain.service.OrganAccountService;
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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import static com.ep.domain.repository.domain.Tables.EP_ORGAN;
import static com.ep.domain.repository.domain.Tables.EP_ORGAN_ACCOUNT;

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


    /**
     * 机构账户关联信息列表
     *
     * @return
     */
    @ApiOperation(value = "列表")
    @GetMapping("/index")
//    @PreAuthorize("hasAnyAuthority({'merchant:organAccount:index','platform:organAccount:index'})")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "mobile", required = false) String mobile,
                        @RequestParam(value = "ognName", required = false) String ognName,
                        @RequestParam(value = "status", required = false) String status,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime
    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP_ORGAN_ACCOUNT.REFER_MOBILE.eq(Long.parseLong(mobile)));
        }
        searchMap.put("mobile", mobile);
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP_ORGAN_ACCOUNT.STATUS.eq(EpOrganAccountStatus.valueOf(status)));
        }
        searchMap.put("status", status);

        if (null != crStartTime) {
            conditions.add(EP_ORGAN_ACCOUNT.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP_ORGAN_ACCOUNT.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP_ORGAN_ACCOUNT.DEL_FLAG.eq(false));
        Long ognId = super.getCurrentUser().get().getOgnId();
        Page<OrganAccountBo> page;
        if (null != ognId) {
            conditions.add(EP_ORGAN_ACCOUNT.OGN_ID.eq(ognId));
            page = organAccountService.merchantFindbyPageAndCondition(pageable, conditions);

        } else {
            if (StringTools.isNotBlank(ognName)) {
                conditions.add(EP_ORGAN.OGN_NAME.like("%" + ognName + "%"));
            }
            searchMap.put("ognName", ognName);
            page = organAccountService.platformFindbyPageAndCondition(pageable, conditions);
        }

        model.addAttribute("ognId", ognId);
        model.addAttribute("ognName", ognName);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);

        return "organAccount/index";
    }

    /**
     * 查看
     *
     * @return
     */
    @GetMapping("/view/{id}")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String view(Model model, @PathVariable("id") Long id) {

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
    public String updateInit(Model model, @PathVariable("id") Long id) {

        model.addAttribute("organAccountPo", organAccountService.getById(id));
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
        return organAccountService.createOgnAccount(po);
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
        return organAccountService.updateOgnAccount(po);
    }

    /**
     * 删除
     *
     * @return
     */
    @GetMapping("/delete/{id}")
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    @ResponseBody
    public ResultDo updateInit(@PathVariable("id") Long id) {
        return organAccountService.deleteOgnAccount(id);
    }
}
