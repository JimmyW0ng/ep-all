package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.WechatPayComponent;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrderRefundBo;
import com.ep.domain.pojo.bo.OrderRefundPayRefundBo;
import com.ep.domain.pojo.bo.WechatUnifiedOrderPayRefundBo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpOrderRefundPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.OrderRefundService;
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

import java.util.Collection;
import java.util.List;
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
    @Autowired
    private WechatUnifiedOrderService wechatUnifiedOrderService;
    @Autowired
    private WechatPayComponent wechatPayComponent;

    @RequestMapping("platformRecord")
    @PreAuthorize("hasAnyAuthority('platform:orderRefund:platformRecord')")
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
    @PreAuthorize("hasAnyAuthority('merchant:orderRefund:merchantRecord')")
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
     * 退款申请
     *
     * @return
     */
    @PostMapping("saveOrderRefund")
    @PreAuthorize("hasAnyAuthority('merchant:orderRefund:merchantRecord')")
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
    @PreAuthorize("hasAnyAuthority('platform:orderRefund:platformRecord')")
    @ResponseBody
    public ResultDo refuseOrderRefund(
            @PathVariable("orderId") Long orderId
    ) throws Exception {
        Long operateId = this.getCurrentUser().get().getId();
        return orderRefundService.refuseOrderRefund(orderId, operateId);
    }

    /**
     * 平台调微信申请退款接口
     *
     * @param outTradeNo
     * @return
     * @throws Exception
     */
    @GetMapping("wechatPayRefund/{outTradeNo}")
    @PreAuthorize("hasAnyAuthority('platform:orderRefund:platformRecord')")
    @ResponseBody
    public ResultDo wechatPayRefund(@PathVariable("outTradeNo") String outTradeNo) throws Exception {
        return wechatPayComponent.xcxPayRefund(outTradeNo);
    }

    /**
     * 退款申请初始化,获取统一下单成功后退款bo(统一下单成功记录信息，该单退款成功记录信息)
     *
     * @param orderId
     * @return
     */
    @GetMapping("/orderRefundApplyInit/{orderId}")
    @PreAuthorize("hasAnyAuthority('merchant:orderRefund:merchantRecord')")
    @ResponseBody
    public ResultDo<Map<String, Object>> orderRefundApplyInit(@PathVariable(value = "orderId") Long orderId) {
        if (null == this.innerOgnOrPlatformReq(orderId, super.getCurrentUserOgnId())) {
            return ResultDo.build(MessageCode.ERROR_ILLEGAL_RESOURCE);
        }
        List<WechatUnifiedOrderPayRefundBo> unifiedOrderlist = wechatUnifiedOrderService.findUnifiedOrderPayRefundBoByOrderId(orderId);
        List<OrderRefundPayRefundBo> orderRefundlist = orderRefundService.findOrderRefundPayRefundBoByOrderId(orderId);
        ResultDo<Map<String, Object>> resultDo = ResultDo.build();
        Map<String, Object> map = Maps.newHashMap();
        map.put("unifiedOrderlist", unifiedOrderlist);
        map.put("orderRefundlist", orderRefundlist);
        return resultDo.setResult(map);
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
