package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.component.WechatPayComponent;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrderBo;
import com.ep.domain.pojo.bo.WechatUnifiedOrderBo;
import com.ep.domain.pojo.po.EpWechatUnifiedOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderPayStatus;
import com.ep.domain.repository.domain.enums.EpOrderPayType;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import com.ep.domain.service.OrderService;
import com.ep.domain.service.WechatUnifiedOrderService;
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
import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 9:32 2018/5/25/025
 */
@Slf4j
@Controller
@RequestMapping("auth/wechatUnifiedOrder")
public class WechatUnifiedOrderController extends BackendController {
    @Autowired
    private WechatUnifiedOrderService wechatUnifiedOrderService;
    @Autowired
    private WechatPayComponent wechatPayComponent;
    @Autowired
    private OrderService orderService;

    /**
     * 微信支付统一订单 分页
     *
     * @return
     */
    @GetMapping("unifiedorderIndex")
    @PreAuthorize("hasAnyAuthority('platform:wechatUnifiedOrder:unifiedorderIndex')")
    public String unifiedorderIndex(Model model,
                                    @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                    @RequestParam(value = "orderId", required = false) Long orderId,
                                    @RequestParam(value = "outTradeNo", required = false) String outTradeNo,
                                    @RequestParam(value = "transactionId", required = false) String transactionId,
                                    @RequestParam(value = "courseName", required = false) String courseName,
                                    @RequestParam(value = "className", required = false) String className,
                                    @RequestParam(value = "payStatus", required = false) String payStatus,
                                    @RequestParam(value = "timeEndStart", required = false) Timestamp timeEndStart,
                                    @RequestParam(value = "timeEndEnd", required = false) Timestamp timeEndEnd,
                                    @RequestParam(value = "createAtStart", required = false) Timestamp createAtStart,
                                    @RequestParam(value = "createAtEnd", required = false) Timestamp createAtEnd
    ) {
        Map searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (null != orderId) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.ORDER_ID.eq(orderId));
        }
        searchMap.put("orderId", orderId);
        if (StringTools.isNotBlank(outTradeNo)) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.OUT_TRADE_NO.eq(outTradeNo));
        }
        searchMap.put("outTradeNo", outTradeNo);
        if (StringTools.isNotBlank(transactionId)) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.TRANSACTION_ID.eq(transactionId));
        }
        searchMap.put("transactionId", transactionId);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.eq(courseName));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.eq(className));
        }
        searchMap.put("className", className);
        if (StringTools.isNotBlank(payStatus)) {
            conditions.add(EP.EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.valueOf(payStatus)));
        }
        searchMap.put("payStatus", payStatus);

        conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.DEL_FLAG.eq(false));

        searchMap.put("timeEndStart", timeEndStart);
        searchMap.put("timeEndEnd", timeEndEnd);
        if (null != createAtStart) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.CREATE_AT.greaterOrEqual(createAtStart));
        }
        searchMap.put("createAtStart", createAtStart);
        if (null != createAtEnd) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.CREATE_AT.lessOrEqual(createAtEnd));
        }
        searchMap.put("createAtEnd", createAtEnd);
        model.addAttribute("searchMap", searchMap);
        Page<WechatUnifiedOrderBo> page = wechatUnifiedOrderService.findbyPageAndCondition(pageable, conditions, timeEndStart, timeEndEnd);
        model.addAttribute("page", page);
        return "wechatUnifiedOrder/unifiedorderIndex";
    }

//    /**
//     * 商户微信支付统一订单 分页
//     *
//     * @return
//     */
//    @GetMapping("unifiedorderMerchantIndex")
//    public String unifiedorderMerchantIndex(Model model,
//                                            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
//                                            @RequestParam(value = "orderId", required = false) Long orderId,
//                                            @RequestParam(value = "outTradeNo", required = false) String outTradeNo,
//                                            @RequestParam(value = "transactionId", required = false) String transactionId,
//                                            @RequestParam(value = "courseName", required = false) String courseName,
//                                            @RequestParam(value = "className", required = false) String className,
//                                            @RequestParam(value = "timeEndStart", required = false) Timestamp timeEndStart,
//                                            @RequestParam(value = "timeEndEnd", required = false) Timestamp timeEndEnd,
//                                            @RequestParam(value = "createAtStart", required = false) Timestamp createAtStart,
//                                            @RequestParam(value = "createAtEnd", required = false) Timestamp createAtEnd
//    ) {
//        Map searchMap = Maps.newHashMap();
//        Collection<Condition> conditions = Lists.newArrayList();
//        if (null != orderId) {
//            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.ORDER_ID.eq(orderId));
//        }
//        searchMap.put("orderId", orderId);
//        if (StringTools.isNotBlank(outTradeNo)) {
//            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.OUT_TRADE_NO.eq(outTradeNo));
//        }
//        searchMap.put("outTradeNo", outTradeNo);
//        if (StringTools.isNotBlank(transactionId)) {
//            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.TRANSACTION_ID.eq(transactionId));
//        }
//        searchMap.put("transactionId", transactionId);
//        if (null != this.getCurrentUserOgnId()) {
//            conditions.add(EP.EP_ORDER.OGN_ID.eq(this.getCurrentUserOgnId()));
//        }
//        if (StringTools.isNotBlank(courseName)) {
//            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.eq(courseName));
//        }
//        searchMap.put("courseName", courseName);
//        if (StringTools.isNotBlank(className)) {
//            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.eq(className));
//        }
//        searchMap.put("className", className);
//
//        conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.DEL_FLAG.eq(false));
//        searchMap.put("timeEndStart", timeEndStart);
//        searchMap.put("timeEndEnd", timeEndEnd);
//        if (null != createAtStart) {
//            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.CREATE_AT.greaterOrEqual(createAtStart));
//        }
//        searchMap.put("createAtStart", createAtStart);
//        if (null != createAtEnd) {
//            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.CREATE_AT.lessOrEqual(createAtEnd));
//        }
//        searchMap.put("createAtEnd", createAtEnd);
//        model.addAttribute("searchMap", searchMap);
//        Page<WechatUnifiedOrderBo> page = wechatUnifiedOrderService.findbyPageAndCondition(pageable, conditions, timeEndStart, timeEndEnd);
//        model.addAttribute("page", page);
//
//        return "wechatUnifiedOrder/unifiedorderMerchantIndex";
//    }


    /**
     * 商户订单支付分页
     *
     * @param model
     * @param pageable
     * @param mobile
     * @param childTrueName
     * @param childNickName
     * @param courseName
     * @param className
     * @param status
     * @param crStartTime
     * @param crEndTime
     * @return
     */
    @GetMapping("unifiedorderMerchantIndex")
    @PreAuthorize("hasAnyAuthority('merchant:wechatUnifiedOrder:unifiedorderMerchantIndex')")
    public String unifiedorderMerchantIndex(Model model,
                                            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                            @RequestParam(value = "mobile", required = false) String mobile,
                                            @RequestParam(value = "childTrueName", required = false) String childTrueName,
                                            @RequestParam(value = "childNickName", required = false) String childNickName,
                                            @RequestParam(value = "courseName", required = false) String courseName,
                                            @RequestParam(value = "className", required = false) String className,
                                            @RequestParam(value = "classType", required = false) String classType,
                                            @RequestParam(value = "status", required = false) String status,
                                            @RequestParam(value = "payStatus", required = false) String payStatus,
                                            @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                                            @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP.EP_MEMBER.MOBILE.eq(Long.parseLong(mobile)));
        }
        searchMap.put("mobile", mobile);
        if (StringTools.isNotBlank(childTrueName)) {
            conditions.add(EP.EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
        }
        searchMap.put("childTrueName", childTrueName);
        if (StringTools.isNotBlank(childNickName)) {
            conditions.add(EP.EP_MEMBER_CHILD.CHILD_NICK_NAME.like("%" + childNickName + "%"));
        }
        searchMap.put("childNickName", childNickName);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
        }
        searchMap.put("className", className);
        if (StringTools.isNotBlank(classType)) {
            conditions.add(EP.EP_ORGAN_CLASS.TYPE.eq(EpOrganClassType.valueOf(classType)));
        }
        searchMap.put("classType", classType);
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP.EP_ORDER.STATUS.eq(EpOrderStatus.valueOf(status)));
        }
        searchMap.put("status", status);
        if (StringTools.isNotBlank(payStatus)) {
            conditions.add(EP.EP_ORDER.PAY_STATUS.eq(EpOrderPayStatus.valueOf(payStatus)));
        }
        searchMap.put("payStatus", payStatus);
        if (null != crStartTime) {
            conditions.add(EP.EP_ORDER.CREATE_AT.greaterOrEqual(crStartTime));
        }
        searchMap.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP.EP_ORDER.CREATE_AT.lessOrEqual(crEndTime));
        }
        searchMap.put("crEndTime", crEndTime);
        conditions.add(EP.EP_ORDER.DEL_FLAG.eq(false));
        conditions.add(EP.EP_ORDER.OGN_ID.eq(super.getCurrentUser().get().getOgnId()));
        conditions.add(EP.EP_ORDER.PAY_TYPE.eq(EpOrderPayType.wechat_pay));
        conditions.add(EP.EP_ORDER.PAY_STATUS.in(EpOrderPayStatus.paid, EpOrderPayStatus.refund_apply, EpOrderPayStatus.refund_finish));
        Page<OrderBo> page = orderService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "wechatUnifiedOrder/unifiedorderMerchantIndex";
    }

    /**
     * @return
     */
    @GetMapping("findWechatUnifiedOrderByOrderId/{orderId}")
    @PreAuthorize("hasAnyAuthority('merchant:wechatUnifiedOrder:unifiedorderMerchantIndex')")
    @ResponseBody
    public ResultDo findWechatUnifiedOrderByOrderId(@PathVariable("orderId") Long orderId) {
        List<EpWechatUnifiedOrderPo> list = wechatUnifiedOrderService.findByOrderId(orderId);
        return ResultDo.build().setResult(list);
    }

    /**
     * 同步统一下单的订单
     *
     * @return
     */
    @GetMapping("syncUnifiedorder/{outTradeNo}")
    @PreAuthorize("hasAnyAuthority('platform:wechatUnifiedOrder:unifiedorderIndex')")
    @ResponseBody
    public ResultDo syncUnifiedorder(@PathVariable("outTradeNo") String outTradeNo) throws Exception {
        return wechatPayComponent.orderQuery(null, outTradeNo, true);
    }
}
