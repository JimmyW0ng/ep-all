package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.WechatPayWithdrawBo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.pojo.po.EpWechatPayWithdrawPo;
import com.ep.domain.service.OrganClassService;
import com.ep.domain.service.WechatPayWithdrawService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

import static com.ep.domain.repository.domain.tables.EpOrganClass.EP_ORGAN_CLASS;
import static com.ep.domain.repository.domain.tables.EpOrganCourse.EP_ORGAN_COURSE;
import static com.ep.domain.repository.domain.tables.EpWechatPayWithdraw.EP_WECHAT_PAY_WITHDRAW;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 22:22 2018/5/16
 */
@Slf4j
@Controller
@RequestMapping("auth/wechatPaywithdraw")
public class WechatPayWithdrawController extends BackendController {

    @Autowired
    private WechatPayWithdrawService wechatPayWithdrawService;
    @Autowired
    private OrganClassService organClassService;


    @GetMapping("index")
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

        conditions.add(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false));

        Page<WechatPayWithdrawBo> page = wechatPayWithdrawService.findbyPageAndCondition(pageable, conditions);

        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "wechatPayWithdraw/index";
    }

    @GetMapping("merchantIndex")
    public String merchantIndex(Model model,
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

        conditions.add(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false));
        conditions.add(EP_ORGAN_CLASS.OGN_ID.eq(super.getCurrentUserOgnId()));

        Page<WechatPayWithdrawBo> page = wechatPayWithdrawService.findbyPageAndCondition(pageable, conditions);

        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "wechatPayWithdraw/merchantIndex";
    }

    /**
     * 审核通过提现申请
     * @return
     */
    @GetMapping("submitPayWithdraw/{id}")
    @ResponseBody
    public ResultDo submitPayWithdraw(@PathVariable("id") Long id) {
        return wechatPayWithdrawService.submitPayWithdrawById(id);
    }

    /**
     * 完成提现
     *
     * @return
     */
    @GetMapping("finishPayWithdraw/{id}")
    @ResponseBody
    public ResultDo finishPayWithdraw(@PathVariable("id") Long id) {
        Optional<EpWechatPayWithdrawPo> withdrawOptional = wechatPayWithdrawService.findById(id);
        if (!withdrawOptional.isPresent()) {
            return wechatPayWithdrawService.finishPayWithdrawById(id);
        }
        Optional<EpOrganClassPo> organClassOptional = organClassService.findById(withdrawOptional.get().getClassId());
        if (organClassOptional.isPresent() && organClassOptional.get().getOgnId().equals(this.getCurrentUserOgnId())) {
            return wechatPayWithdrawService.finishPayWithdrawById(id);
        } else {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
    }

    /**
     * 拒绝提现
     *
     * @return
     */
    @GetMapping("refusePayWithdraw")
    @ResponseBody
    public ResultDo refusePayWithdraw(@RequestParam(value = "id") Long id, @RequestParam(value = "remark") String remark) {

        remark = StringTools.isBlank(remark) ? null : remark;
        return wechatPayWithdrawService.refusePayWithdrawById(id, remark);

    }
}
