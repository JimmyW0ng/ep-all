package com.ep.backend.controller;

import com.ep.domain.pojo.bo.OrderRefundBo;
import com.ep.domain.service.OrderRefundService;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Map;

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

    @RequestMapping("index")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
//        if (null != mobile) {
//            conditions.add(EP_MEMBER.MOBILE.eq(mobile));
//        }
//        searchMap.put("mobile", mobile);
//        if (null != crStartTime) {
//            conditions.add(EP_MEMBER.CREATE_AT.greaterOrEqual(crStartTime));
//        }
//        searchMap.put("crStartTime", crStartTime);
//        if (null != crEndTime) {
//            conditions.add(EP_MEMBER.CREATE_AT.lessOrEqual(crEndTime));
//        }
        conditions.add(EP_ORDER_REFUND.DEL_FLAG.eq(false));
        Page<OrderRefundBo> page = orderRefundService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);

        return "orderRefund/index";
    }
}
