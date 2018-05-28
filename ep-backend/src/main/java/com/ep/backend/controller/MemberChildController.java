package com.ep.backend.controller;

import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.po.EpMemberChildPo;
import com.ep.domain.pojo.po.EpOrderPo;
import com.ep.domain.service.MemberChildService;
import com.ep.domain.service.MemberService;
import com.ep.domain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * @Description: 孩子控制器
 * @Author: CC.F
 * @Date: 17:26 2018/5/28/028
 */
@Controller
@RequestMapping("auth/memberChild")
public class MemberChildController {
    @Autowired
    private MemberChildService memberChildService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;

    /**
     * 订单页面获取孩子信息
     *
     * @param childId
     * @return
     */
    @RequestMapping("getMemberChild/{orderId}&{childId}")
    @ResponseBody
    public ResultDo getMemberChild(@PathVariable("orderId") Long orderId, @PathVariable("childId") Long childId) {
        //校验该孩子是否存在该订单
        Optional<EpOrderPo> orderOptional = orderService.findById(orderId);
        if (orderOptional.isPresent() && orderOptional.get().getChildId().equals(childId)) {
            Optional<EpMemberChildPo> memberChildOptional = memberChildService.findById(childId);
            return memberChildOptional.isPresent() ? ResultDo.build().setResult(memberChildOptional.get())
                    : ResultDo.build(MessageCode.ERROR_CHILD_NOT_EXISTS);
        }
        return ResultDo.build(MessageCode.ERROR_CHILD_NOT_EXISTS);
    }

}
