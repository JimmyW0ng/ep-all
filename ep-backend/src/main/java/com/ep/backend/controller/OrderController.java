package com.ep.backend.controller;

import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.OrderBo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.repository.domain.enums.EpOrderStatus;
import com.ep.domain.service.OrderService;
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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ep.domain.repository.domain.Ep.EP;


/**
 * @Description: 订单控制器
 * @Author: CC.F
 * @Date: 20:59 2018/2/25
 */
@Controller
@RequestMapping("auth/order")
public class OrderController extends BackendController {
    @Autowired
    private OrderService orderService;

    @GetMapping("index")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value="mobile",required = false)String mobile,
                        @RequestParam(value="childTrueName",required = false)String childTrueName,
                        @RequestParam(value="courseName",required = false)String courseName,
                        @RequestParam(value="className",required = false)String className,
                        @RequestParam(value="status",required = false)String status,
                        @RequestParam(value = "crStartTime", required = false) Timestamp crStartTime,
                        @RequestParam(value = "crEndTime", required = false) Timestamp crEndTime

    ) {
        Map map = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (StringTools.isNotBlank(mobile)) {
            conditions.add(EP.EP_MEMBER.MOBILE.eq(Long.parseLong(mobile)));
        }
        map.put("mobile", mobile);
        if (StringTools.isNotBlank(childTrueName)) {
            conditions.add(EP.EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%"+childTrueName+"%"));
        }
        map.put("childTrueName", childTrueName);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.like("%"+courseName+"%"));
        }
        map.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.like("%"+className+"%"));
        }
        map.put("className", className);
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP.EP_ORDER.STATUS.eq(EpOrderStatus.valueOf(status)));
        }
        map.put("status", status);
        if (null != crStartTime) {
            conditions.add(EP.EP_ORDER.CREATE_AT.greaterOrEqual(crStartTime));
        }
        map.put("crStartTime", crStartTime);
        if (null != crEndTime) {
            conditions.add(EP.EP_ORDER.CREATE_AT.lessOrEqual(crEndTime));
        }
        map.put("crEndTime", crEndTime);

        Page<OrderBo> page = orderService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("map", map);
        return "order/index";
    }

    /**
     * 批量报名
     * @param pos
     * @return
     */
    @PostMapping("batchOrderSuccess")
    @ResponseBody
    public ResultDo batchOrderSuccess(
            @RequestBody List<EpOrderPo> pos) {
        ResultDo resultDo = ResultDo.build();
        orderService.batchOrderSuccess(pos);
        return resultDo;
    }
    /**
     * 订单报名成功
     *
     * @param po
     */
    @PostMapping("orderSuccess")
    @ResponseBody
    public ResultDo orderSuccess(
            EpOrderPo po) {
        ResultDo resultDo = ResultDo.build();
        if (orderService.orderSuccessById(po.getId(),po.getClassId()) == 1) {
            //报名成功
            resultDo.setResult(EpOrderStatus.success.getLiteral());
            return resultDo;
        } else {
            EpOrderPo orderPo = orderService.getById(po.getId());
            if (null != orderPo && orderPo.getStatus().equals(EpOrderStatus.success)) {
                //重复报名
                return resultDo;
            }
            //报名失败
            resultDo.setSuccess(false);
            return resultDo;
        }
    }

    /**
     * 报名拒绝
     * @param id
     */
    @GetMapping("orderRefuse")
    @ResponseBody
    public ResultDo orderRefuse(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "remark", required = false) String remark) {
        ResultDo resultDo = ResultDo.build();
        if (orderService.orderRefuseById(id, remark) == 1) {
            return resultDo;
        } else {
            resultDo.setSuccess(false);
            return resultDo;
        }

    }
}
