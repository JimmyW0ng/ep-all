package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrderBo;
import com.ep.domain.pojo.bo.OrderRefundBo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpOrderRefundPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.domain.enums.EpOrderPayStatus;
import com.ep.domain.repository.domain.enums.EpOrderPayType;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.repository.domain.enums.EpOrganClassType;
import com.ep.domain.service.OrderRefundService;
import com.ep.domain.service.OrderService;
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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.ep.domain.repository.domain.Ep.EP;
import static com.ep.domain.repository.domain.Tables.EP_ORDER_REFUND;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 12:43 2018/5/22/022
 */
@Controller
@Slf4j
@RequestMapping("auth/orderRefund")
public class OrderRefundController extends BackendController {
    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("platformRecord")
    public String platformRecord(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value = "ognName", required = false) String ognName,
                                 @RequestParam(value = "orderId", required = false) Long orderId) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(ognName)) {
            conditions.add(EP.EP_ORGAN.OGN_NAME.eq(ognName));
        }
        searchMap.put("ognName", ognName);
        if (null != orderId) {
            conditions.add(EP.EP_ORDER_REFUND.ORDER_ID.eq(orderId));
        }
        searchMap.put("orderId", orderId);
        conditions.add(EP_ORDER_REFUND.DEL_FLAG.eq(false));
        Page<OrderRefundBo> page = orderRefundService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);

        return "orderRefund/platformRecord";
    }

    @RequestMapping("merchantRecord")
    public String merchantRecord(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value = "orderId", required = false) Long orderId) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (null != orderId) {
            conditions.add(EP.EP_ORDER_REFUND.ORDER_ID.eq(orderId));
        }
        searchMap.put("orderId", orderId);
        conditions.add(EP_ORDER_REFUND.DEL_FLAG.eq(false));
        conditions.add(EP_ORDER_REFUND.OGN_ID.eq(this.getCurrentUserOgnId()));
        Page<OrderRefundBo> page = orderRefundService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);

        return "orderRefund/merchantRecord";
    }

    /**
     * 商户退款管理列表
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
    @GetMapping("merchantIndex")
    public String orderRefundIndex(Model model,
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
        return "order/orderRefundIndex";
    }

    /**
     * 退款申请
     *
     * @return
     */
    @PostMapping("saveOrderRefund")
    @ResponseBody
    public ResultDo saveOrderRefund(
            @RequestParam("orderId") Long orderId,
            @RequestParam("outTradeNo") String outTradeNo,
            @RequestParam("refundReason") String refundReason
    ) throws Exception {
        EpSystemUserPo currentUser = this.getCurrentUser().get();
        Optional<EpOrderPo> orderOptional = orderService.findById(orderId);
        if (orderOptional.isPresent() && orderOptional.get().getOgnId().equals(currentUser.getOgnId())) {
            return orderRefundService.saveOrderRefund(orderOptional.get().getOgnId(), orderId, outTradeNo, refundReason, currentUser.getId());
        } else {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
    }

    /**
     * 退款申请拒绝
     *
     * @return
     */
    @GetMapping("refuseOrderRefund/{orderId}")
    @ResponseBody
    public ResultDo refuseOrderRefund(
            @PathVariable("orderId") Long orderId
    ) throws Exception {
        return orderRefundService.refuseOrderRefund(orderId);
    }

    /**
     * 校验业务对象是否属于该机构，是：返回po;否：返回null
     *
     * @param sourceId
     * @param ognId
     * @return
     */
    private EpOrderRefundPo innerOgnOrPlatformReq(Long sourceId, Long ognId) {
        if (sourceId == null) {
            return null;
        }
        Optional<EpOrderRefundPo> optional = orderRefundService.findById(sourceId);
        if (!optional.isPresent()) {
            return null;
        }
        if (ognId == null) {
            return optional.get();
        }
        if (optional.get().getOgnId().equals(ognId)) {
            return optional.get();
        } else {
            log.error(SpringComponent.messageSource("ERROR_ILLEGAL_RESOURCE"));
            return null;
        }
    }
}
