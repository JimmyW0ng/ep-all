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
import java.util.*;

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
                        @RequestParam(value = "mobile", required = false) String mobile,
                        @RequestParam(value = "childTrueName", required = false) String childTrueName,
                        @RequestParam(value = "courseName", required = false) String courseName,
                        @RequestParam(value = "className", required = false) String className,
                        @RequestParam(value = "status", required = false) String status,
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
            conditions.add(EP.EP_MEMBER_CHILD.CHILD_TRUE_NAME.like("%" + childTrueName + "%"));
        }
        map.put("childTrueName", childTrueName);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.like("%" + courseName + "%"));
        }
        map.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.like("%" + className + "%"));
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
     *
     * @param pos
     * @return
     */
    @PostMapping("batchOrderSuccess")
    @ResponseBody
    public ResultDo batchOrderSuccess(
            @RequestBody List<EpOrderPo> pos) {
        ResultDo resultDo = ResultDo.build();
        //按classId排序
        Collections.sort(pos, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                EpOrderPo po1 = (EpOrderPo) o1;
                EpOrderPo po2 = (EpOrderPo) o2;
                if (po1.getClassId().longValue() > po2.getClassId().longValue()) {
                    return 1;
                } else if (po1.getClassId().longValue() == po2.getClassId().longValue()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        //不同classId的记录封装在不同的map中
        Map<Long, Object> map = Maps.newHashMap();
        List<EpOrderPo> list = Lists.newArrayList();
        Long classId = pos.get(0).getClassId();
        for (int i = 0; i < pos.size(); i++) {
            if (classId.longValue() == pos.get(i).getClassId().longValue()) {
                list.add(pos.get(i));
            } else {
                map.put(classId,list);
                list = Lists.newArrayList();
                classId = pos.get(i).getClassId();
                list.add(pos.get(i));
            }
            map.put(classId,list);
        }

        orderService.batchOrderSuccess(map);
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
        if (orderService.orderSuccessById(po.getId(), po.getClassId()) == 1) {
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
     *
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

    /**
     * 报名/拒绝取消
     *
     * @param id
     */
    @GetMapping("orderCancel")
    @ResponseBody
    public ResultDo orderCancel(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "status") String status
            ) {
        ResultDo resultDo = ResultDo.build();
        orderService.orderCancelById(id,EpOrderStatus.valueOf(status));
        return resultDo;

    }


}
