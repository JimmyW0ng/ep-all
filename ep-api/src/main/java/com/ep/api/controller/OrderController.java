package com.ep.api.controller;

import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.OrderDto;
import com.ep.domain.pojo.dto.OrderInitDto;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @Description: 订单控制类
 * @Author: J.W
 * @Date: 下午2:06 2018/1/28
 */
@Slf4j
@RequestMapping("auth/order")
@RestController
@Api(value = "api-auth-order", description = "订单接口")
public class OrderController extends ApiController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "立即订单初始化信息")
    @PostMapping("/init")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrderInitDto> init(@RequestParam("courseId") Long courseId) {
        Optional<EpMemberPo> optional = super.getCurrentUser();
        return orderService.initInfo(optional.get().getId(), courseId);
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrderDto> order(@RequestParam("childId") Long childId,
                                    @RequestParam("classId") Long classId) {
        Optional<EpMemberPo> optional = super.getCurrentUser();
        return orderService.order(optional.get().getId(), childId, classId);
    }

    @ApiOperation(value = "创建订单")
    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('api:base')")
    public ResultDo<OrderDto> order(@RequestParam("childId") Long childId,
                                    @RequestParam("classId") Long classId,
                                    @RequestParam("formId") String formId,
                                    @RequestParam("openid") String openid
    ) {
        Optional<EpMemberPo> optional = super.getCurrentUser();
        return orderService.order(optional.get().getId(), childId, classId, formId, openid);
    }

}
