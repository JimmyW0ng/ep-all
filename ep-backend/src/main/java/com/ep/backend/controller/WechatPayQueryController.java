package com.ep.backend.controller;

import com.ep.domain.component.WechatPayComponent;
import com.ep.domain.pojo.ResultDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 微信支付控制器
 * @Author: CC.F
 * @Date: 14:41 2018/5/8/008
 */
@Slf4j
@RequestMapping("/auth/wechatPay")
@Controller
public class WechatPayQueryController {

    @Autowired
    private WechatPayComponent wechatPayComponent;


    @GetMapping("orderquery")
    public String getOrderquery() {
        return "wechatPayQuery/orderquery";

    }

    /**
     * 查询订单
     *
     * @param transactionId
     * @param outTradeNo
     * @return
     * @throws Exception
     */
    @PostMapping("orderquery")
    @ResponseBody
    public ResultDo postOrderquery(@RequestParam(value = "transactionId", required = false) String transactionId,
                                   @RequestParam(value = "outTradeNo", required = false) String outTradeNo) throws Exception {
        return wechatPayComponent.orderQuery(transactionId, outTradeNo, false);
    }

    @GetMapping("refundquery")
    public String getRefundquery() {
        return "wechatPayQuery/refundquery";

    }

    /**
     * 查询退款
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户订单号
     * @param outRefundNo   商户退款单号
     * @param refundId      微信退款单号
     * @return
     * @throws Exception
     */
    @PostMapping("refundquery")
    @ResponseBody
    public ResultDo postRefundquery(@RequestParam(value = "transaction_id", required = false) String transactionId,
                                    @RequestParam(value = "out_trade_no", required = false) String outTradeNo,
                                    @RequestParam(value = "out_refund_no", required = false) String outRefundNo,
                                    @RequestParam(value = "refund_id", required = false) String refundId,
                                    @RequestParam(value = "offset", required = false) Integer offset
    ) throws Exception {
        return wechatPayComponent.refundquery(transactionId, outTradeNo, outRefundNo, refundId, offset);
    }


}
