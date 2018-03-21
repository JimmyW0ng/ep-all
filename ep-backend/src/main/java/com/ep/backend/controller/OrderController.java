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
 * @Description: 订单控制器
 * @Author: CC.F
 * @Date: 20:59 2018/2/25
 */
@Controller
@RequestMapping("auth/order")
public class OrderController extends BackendController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单列表
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
    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    public String index(Model model,
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "mobile", required = false) String mobile,
                        @RequestParam(value = "childTrueName", required = false) String childTrueName,
                        @RequestParam(value = "childNickName", required = false) String childNickName,
                        @RequestParam(value = "courseName", required = false) String courseName,
                        @RequestParam(value = "className", required = false) String className,
                        @RequestParam(value = "status", required = false) String status,
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
        if (StringTools.isNotBlank(status)) {
            conditions.add(EP.EP_ORDER.STATUS.eq(EpOrderStatus.valueOf(status)));
        }
        searchMap.put("status", status);
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
        Page<OrderBo> page = orderService.findbyPageAndCondition(pageable, conditions);
        model.addAttribute("page", page);
        model.addAttribute("searchMap", searchMap);
        return "order/index";
    }

    /**
     * 批量报名成功
     *
     * @param pos
     * @return
     */
    @PostMapping("batchOrderSuccess")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo batchOrderSuccess(
            @RequestBody List<EpOrderPo> pos) {
        //不同班次的孩子批量报名
        return orderService.batchOrderSuccess(pos);
    }

    /**
     * 单个订单报名成功
     *
     * @param id
     * @param classId
     */
    @PostMapping("orderSuccess")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo orderSuccess(@RequestParam(value = "id") Long id,
                                 @RequestParam(value = "classId") Long classId
    ) {
        return orderService.orderSuccessById(id, classId);
    }

    /**
     * 报名拒绝
     *
     * @param id
     */
    @GetMapping("orderRefuse")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
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

    /**
     * 报名/拒绝取消
     *
     * @param id
     */
    @GetMapping("orderCancel")
    @PreAuthorize("hasAnyAuthority('merchant:order:index')")
    @ResponseBody
    public ResultDo orderCancel(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "status") String status
    ) {
        ResultDo resultDo = ResultDo.build();
        orderService.orderCancelById(id, EpOrderStatus.valueOf(status));
        return resultDo;

    }


}
