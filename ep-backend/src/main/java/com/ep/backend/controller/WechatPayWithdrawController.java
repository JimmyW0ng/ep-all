package com.ep.backend.controller;

import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.WechatPayWithdrawBo;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.service.OrderService;
import com.ep.domain.service.OrganClassService;
import com.ep.domain.service.WechatPayBillService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.ep.domain.repository.domain.tables.EpOrganClass.EP_ORGAN_CLASS;
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
    @Autowired
    private OrderService orderService;
    @Autowired
    private WechatPayBillService wechatPayBillService;

    @GetMapping("merchantIndex")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
//        if (StringTools.isNotBlank(courseName)) {
//            conditions.add(EP_ORGAN_COURSE.COURSE_NAME.eq(courseName));
//        }
//        searchMap.put("courseName", courseName);
//        if (StringTools.isNotBlank(className)) {
//            conditions.add(EP_ORGAN_CLASS.CLASS_NAME.eq(className));
//        }
//        searchMap.put("className", className);

        conditions.add(EP_ORGAN_CLASS.OGN_ID.eq(this.getCurrentUserOgnId()));
        conditions.add(EP_ORGAN_CLASS.DEL_FLAG.eq(false));
        conditions.add(EP_WECHAT_PAY_WITHDRAW.DEL_FLAG.eq(false));

        Page<WechatPayWithdrawBo> page = wechatPayWithdrawService.findbyPageAndCondition(pageable, conditions);

        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "wechatPayWithdraw/index";
    }

    /**
     * 商户申请提现
     *
     * @param classId
     * @return
     */
    @GetMapping("merchant/applyPayWithdraw")
    @ResponseBody
    public ResultDo applyPayWithdraw(@RequestParam(value = "classId") Long classId,
                                     @RequestParam(value = "withdrawDeadline") String withdrawDeadline) {
        Optional<EpOrganClassPo> organClassOptional = organClassService.findById(classId);
        if (organClassOptional.isPresent() && organClassOptional.get().getOgnId().equals(this.getCurrentUserOgnId())) {
            return wechatPayWithdrawService.applyPayWithdrawByClassId(classId, organClassOptional.get().getCourseId(), withdrawDeadline);
        } else {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
//        return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
    }


}
