package com.ep.backend.controller;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.WechatPayWithdrawBo;
import com.ep.domain.pojo.dto.ClassWithdrawQueryDto;
import com.ep.domain.pojo.po.EpOrganClassPo;
import com.ep.domain.pojo.po.EpOrganCoursePo;
import com.ep.domain.pojo.po.EpWechatPayBillPo;
import com.ep.domain.pojo.po.EpWechatPayWithdrawPo;
import com.ep.domain.repository.domain.enums.EpOrderPayStatus;
import com.ep.domain.service.*;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.ep.domain.repository.domain.Tables.EP_ORDER;
import static com.ep.domain.repository.domain.Tables.EP_WECHAT_PAY_BILL_DETAIL;
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
    @Autowired
    private OrganCourseService organCourseService;
    @Autowired
    private WechatPayBillService wechatPayBillService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WechatPayBillDetailService wechatPayBillDetailService;


    @GetMapping("platformIndex")
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
        return "wechatPayWithdraw/platformIndex";
    }

    @GetMapping("merchantIndex")
    public String classWithdrawMerchantIndex(Model model,
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

        conditions.add(EP_ORGAN_CLASS.OGN_ID.eq(this.getCurrentUserOgnId()));
        conditions.add(EP_ORGAN_CLASS.DEL_FLAG.eq(false));
        conditions.add(EP_ORGAN_COURSE.DEL_FLAG.eq(false));
        conditions.add(EP_ORDER.DEL_FLAG.eq(false));
        conditions.add(EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.paid));
        conditions.add(EP_WECHAT_PAY_BILL_DETAIL.DEL_FLAG.eq(false));
        //最近一次微信支付对账记录
        EpWechatPayBillPo wechatPayBillPo = wechatPayBillService.getLastPayBill();
        Date withdrawDeadline = DateTools.stringToDate(wechatPayBillPo.getBillDate().toString(), DateTools.DATE_FMT_0);
        String withdrawDeadlineStr = DateTools.toString(withdrawDeadline, DateTools.DATE_FMT_4);
        //结算截止时间
        Date conditionDeadline = DateTools.addDate(withdrawDeadline, BizConstant.DB_NUM_ONE);
        model.addAttribute("withdrawDeadline", withdrawDeadlineStr);
        model.addAttribute("withdrawDeadlineTime", DateTools.dateToString(conditionDeadline, DateTools.TIME_PATTERN));
        Page<ClassWithdrawQueryDto> page = organClassService.findClassWithdrawQueryDtoByPage(pageable, conditions, DateTools.dateToTimestamp(conditionDeadline));
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "wechatPayWithdraw/withdrawMerchantIndex";
    }

    @GetMapping("classWithdrawInit/{classId}")
    public String classWithdrawInit(@PathVariable("classId") Long classId, Model model) {
        Optional<EpOrganClassPo> classOptional = organClassService.findById(classId);
        if (!classOptional.isPresent() || !classOptional.get().getOgnId().equals(this.getCurrentUserOgnId())) {
            return "noresource";
        }
        model.addAttribute("className", classOptional.get().getClassName());
        model.addAttribute("classId", classOptional.get().getId());
        Optional<EpOrganCoursePo> courseOptional = organCourseService.findById(classOptional.get().getCourseId());
        model.addAttribute("courseName", courseOptional.get().getCourseName());
        EpWechatPayBillPo wechatPayBillPo = wechatPayBillService.getLastPayBill();


        Date withdrawDeadline = DateTools.stringToDate(wechatPayBillPo.getBillDate().toString(), DateTools.DATE_FMT_0);
        String withdrawDeadlineStr = DateTools.toString(withdrawDeadline, DateTools.DATE_FMT_4);
        //结算截止时间
        Date conditionDeadline = DateTools.addDate(withdrawDeadline, BizConstant.DB_NUM_ONE);
        model.addAttribute("withdrawDeadline", withdrawDeadlineStr);
        model.addAttribute("withdrawDeadlineTime", DateTools.dateToString(conditionDeadline, DateTools.TIME_PATTERN));


        Timestamp endTime = DateTools.dateToTimestamp(conditionDeadline);
        // 统计班次微信支付订单总数
        model.addAttribute("countWechatPaidOrder", orderService.countWechatPayOrders(classId, endTime));
        // 统计班次微信支付订单总金额
        BigDecimal sumWechatPaidTotalFee = orderService.sumWechatPaidOrderTotalFee(classId, endTime);
        model.addAttribute("sumWechatPaidTotalFee", sumWechatPaidTotalFee);
        // 统计班次微信支付订单总手续费
        BigDecimal sumWechatPoundage = orderService.sumWechatPoundage(classId, endTime);
        model.addAttribute("sumWechatPoundage", sumWechatPoundage);
        // 统计班次微信支付总实收金额
        model.addAttribute("platformReceiveFee", sumWechatPaidTotalFee.subtract(sumWechatPoundage));
        // 统计班次微信支付未提现订单总数
        int countWechatWaitWithdrawOrder = orderService.countWaitWithdrawOrderByClassId(classId, endTime);
        model.addAttribute("countWechatWaitWithdrawOrder", countWechatWaitWithdrawOrder);
        // 统计班次微信支付未提现订单总金额
        BigDecimal sumWaitWithdrawOrderTotalFee = orderService.sumWaitWithdrawOrderByClassId(classId, endTime);
        model.addAttribute("sumWaitWithdrawOrderTotalFee", sumWaitWithdrawOrderTotalFee);
        // 统计班次微信支付未提现订单手续费
        BigDecimal sumWaitWithdrawOrderPoundage = orderService.sumWaitWithdrawPoundageByClassId(classId, endTime);
        model.addAttribute("sumWaitWithdrawOrderPoundage", sumWaitWithdrawOrderPoundage);
        model.addAttribute("withdrawFee", sumWaitWithdrawOrderTotalFee.subtract(sumWaitWithdrawOrderPoundage));
        // 线下支付已支付订单数
        model.addAttribute("countOfflinePaidOrder", orderService.countOfflinePaidOrders(classId));
        // 线下支付已支付订单金额
        model.addAttribute("sumOfflinePaidOrderFee", orderService.sumOfflinePaidOrderTotalFee(classId));
        // 历史提现记录
        List<EpWechatPayWithdrawPo> wechatPayWithdrawPos = wechatPayWithdrawService.findByClassId(classId);
        model.addAttribute("wechatPayWithdrawPos", wechatPayWithdrawPos);
        return "wechatPayWithdraw/classWithdraw";
    }


    @GetMapping("merchantRecord")
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
        return "wechatPayWithdraw/merchantRecord";
    }

    /**
     * 商户申请提现
     *
     * @param classId
     * @return
     */
    @PostMapping("applyPayWithdraw")
    @ResponseBody
    public ResultDo applyPayWithdraw(@RequestParam(value = "classId") Long classId,
                                     @RequestParam(value = "withdrawDeadlineTime") String withdrawDeadlineTime,
                                     @RequestParam(value = "accountName") String accountName,
                                     @RequestParam(value = "accountNumber") String accountNumber
    ) {
        Optional<EpOrganClassPo> organClassOptional = organClassService.findById(classId);
        if (organClassOptional.isPresent() && organClassOptional.get().getOgnId().equals(this.getCurrentUserOgnId())) {
            return wechatPayWithdrawService.applyPayWithdrawByClassId(classId, organClassOptional.get().getCourseId(),
                    withdrawDeadlineTime, accountName, accountNumber);
        } else {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
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
    @PostMapping("finishPayWithdraw")
    @ResponseBody
    public ResultDo finishPayWithdraw(@RequestParam(value = "id") Long id,
                                      @RequestParam(value = "outWithdrawNo") String outWithdrawNo,
                                      @RequestParam(value = "payId") String payId,
                                      @RequestParam(value = "withdrawFee") BigDecimal withdrawFee,
                                      @RequestParam(value = "paidTime") Timestamp paidTime
    ) {
        return wechatPayWithdrawService.finishPayWithdrawById(id, outWithdrawNo, payId, withdrawFee, paidTime);
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
