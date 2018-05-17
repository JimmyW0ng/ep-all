package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.ClassWithdrawQueryDto;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.pojo.po.EpWechatPayWithdrawPo;
import com.ep.domain.repository.domain.enums.EpOrganClassStatus;
import com.ep.domain.service.OrderService;
import com.ep.domain.service.OrganClassService;
import com.ep.domain.service.WechatPayWithdrawService;
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

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.ep.domain.repository.domain.tables.EpOrganClass.EP_ORGAN_CLASS;
import static com.ep.domain.repository.domain.tables.EpOrganCourse.EP_ORGAN_COURSE;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 22:22 2018/5/16
 */
@Controller
@RequestMapping("auth/payWithdraw")
public class PayWithdrawController extends BackendController {

    @Autowired
    private WechatPayWithdrawService wechatPayWithdrawService;
    @Autowired
    private OrganClassService organClassService;
    @Autowired
    private OrderService orderService;

    @GetMapping("merchant/ClassWithdraw")
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
//
//
        conditions.add(EP_ORGAN_CLASS.OGN_ID.eq(this.getCurrentUserOgnId()));
        conditions.add(EP_ORGAN_CLASS.STATUS.in(EpOrganClassStatus.opening, EpOrganClassStatus.end));
        conditions.add(EP_ORGAN_CLASS.DEL_FLAG.eq(false));
//
        Page<ClassWithdrawQueryDto> page = organClassService.findClassWithdrawQueryDtoByPage(pageable, conditions);
        page.getContent().forEach(p -> {
            Long classId = p.getClassId();
            //总微信支付成功订单数
            int totalWechatPaidOrderNum = orderService.countWechatPaidOrderByClassId(classId);
            p.setTotalWechatPaidOrderNum(totalWechatPaidOrderNum);
            //已提现订单数
            int finishWithdrawNum = wechatPayWithdrawService.countPayWithdrawByClassId(classId);
            p.setWaitWithdrawOrderNum(totalWechatPaidOrderNum - finishWithdrawNum);
            EpWechatPayWithdrawPo wechatPayWithdrawPo = wechatPayWithdrawService.getLastWithdrawByClassId(classId);
            if (null != wechatPayWithdrawPo) {
                p.setLastWithdrawAmount(wechatPayWithdrawPo.getTotalAmount());
                p.setLastWithdrawOrderNum(wechatPayWithdrawPo.getWechatPayNum());
                p.setLastWithdrawTime(wechatPayWithdrawPo.getOrderDeadline());
                p.setLastWithdrawStatus(wechatPayWithdrawPo.getStatus());
                p.setPayWithdrawId(wechatPayWithdrawPo.getId());
            }

        });
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "payWithdraw/ClassWithdraw";
    }

    /**
     * 商户申请提现
     *
     * @param classId
     * @return
     */
    @GetMapping("merchant/applyPayWithdraw/{classId}")
    @ResponseBody
    public ResultDo applyPayWithdraw(@PathVariable("classId") Long classId) {
        Optional<EpOrganClassPo> organClassOptional = organClassService.findById(classId);
        if (organClassOptional.isPresent() && organClassOptional.get().getOgnId().equals(this.getCurrentUserOgnId())) {
            return wechatPayWithdrawService.applyPayWithdrawByClassId(classId, organClassOptional.get().getCourseId());
        } else {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
    }


}
