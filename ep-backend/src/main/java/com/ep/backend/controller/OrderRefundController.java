package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrderRefundBo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.pojo.po.EpOrderRefundPo;
import com.ep.domain.pojo.po.EpSystemUserPo;
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

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

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

    @RequestMapping("index")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();

        conditions.add(EP_ORDER_REFUND.DEL_FLAG.eq(false));
        Page<OrderRefundBo> page = orderRefundService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);

        return "orderRefund/index";
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
