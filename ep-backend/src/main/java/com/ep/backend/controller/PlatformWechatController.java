package com.ep.backend.controller;

import com.ep.common.tool.SerialNumberTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.WechatPayComponent;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.WechatUnifiedOrderBo;
import com.ep.domain.pojo.po.EpWechatUnifiedOrderPo;
import com.ep.domain.service.WechatFwhService;
import com.ep.domain.service.WechatUnifiedOrderService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * @Description:
 * @Author: CC.F
 * @Date: 10:37 2018/4/23/023
 */
@Controller
@RequestMapping("auth/wechat")
public class PlatformWechatController extends BackendController {
    @Autowired
    private WechatFwhService wechatFwhService;
    @Autowired
    private WechatUnifiedOrderService wechatUnifiedOrderService;
    @Autowired
    private WechatPayComponent wechatPayComponent;

    @Value("${wechat.fwh.token}")
    private String wechatFwhToken;
    @Value("${wechat.fwh.id}")
    private String wechatFwhId;

    /**
     * 自定义菜单页面
     *
     * @return
     */
    @GetMapping("menu")
    public String menu() {
        return "wechat/menu";
    }

    /**
     * 自定义菜单查询
     *
     * @return
     */
    @GetMapping("menuGet")
    @ResponseBody
    public ResultDo menuGet() {
        return wechatFwhService.menuGet();
    }

    /**
     * 自定义菜单创建
     *
     * @return
     */
    @PostMapping("menuCreate")
    @ResponseBody
    public ResultDo menuCreate(@RequestParam(value = "menuJson") String menuJson) {
        return wechatFwhService.menuCreate(menuJson);
    }

    /**
     * 自定义菜单删除
     *
     * @return
     */
    @GetMapping("menuDelete")
    @ResponseBody
    public ResultDo menuDelete() {
        return wechatFwhService.menuDelete();
    }

    /**
     * 微信支付统一订单 分页
     *
     * @return
     */
    @GetMapping("unifiedorderIndex")
    public String unifiedorderIndex(Model model,
                                    @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                    @RequestParam(value = "orderId", required = false) Long orderId,
                                    @RequestParam(value = "outTradeNo", required = false) String outTradeNo,
                                    @RequestParam(value = "transactionId", required = false) String transactionId,
                                    @RequestParam(value = "courseName", required = false) String courseName,
                                    @RequestParam(value = "className", required = false) String className,
                                    @RequestParam(value = "timeEndStart", required = false) Timestamp timeEndStart,
                                    @RequestParam(value = "timeEndEnd", required = false) Timestamp timeEndEnd,
                                    @RequestParam(value = "createAtStart", required = false) Timestamp createAtStart,
                                    @RequestParam(value = "createAtEnd", required = false) Timestamp createAtEnd
    ) {
        Map searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (null != orderId) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.ORDER_ID.eq(orderId));
        }
        searchMap.put("orderId", orderId);
        if (StringTools.isNotBlank(outTradeNo)) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.OUT_TRADE_NO.eq(outTradeNo));
        }
        searchMap.put("outTradeNo", outTradeNo);
        if (StringTools.isNotBlank(transactionId)) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.TRANSACTION_ID.eq(transactionId));
        }
        searchMap.put("transactionId", transactionId);
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.eq(courseName));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.eq(className));
        }
        searchMap.put("className", className);

        conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.DEL_FLAG.eq(false));
        Page<WechatUnifiedOrderBo> page = wechatUnifiedOrderService.findbyPageAndCondition(pageable, conditions, timeEndStart, timeEndEnd);
        model.addAttribute("page", page);
        searchMap.put("timeEndStart", timeEndStart);
        searchMap.put("timeEndEnd", timeEndEnd);
        if (null != createAtStart) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.CREATE_AT.greaterOrEqual(createAtStart));
        }
        searchMap.put("createAtStart", createAtStart);
        if (null != createAtEnd) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.CREATE_AT.lessOrEqual(createAtEnd));
        }
        searchMap.put("createAtEnd", createAtEnd);
        model.addAttribute("searchMap", searchMap);

        return "wechat/unifiedorderIndex";
    }

    /**
     * 商户微信支付统一订单 分页
     *
     * @return
     */
    @GetMapping("unifiedorderMerchantIndex")
    public String unifiedorderMerchantIndex(Model model,
                                            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                            @RequestParam(value = "orderId", required = false) Long orderId,
                                            @RequestParam(value = "outTradeNo", required = false) String outTradeNo,
                                            @RequestParam(value = "transactionId", required = false) String transactionId,
                                            @RequestParam(value = "courseName", required = false) String courseName,
                                            @RequestParam(value = "className", required = false) String className,
                                            @RequestParam(value = "timeEndStart", required = false) Timestamp timeEndStart,
                                            @RequestParam(value = "timeEndEnd", required = false) Timestamp timeEndEnd,
                                            @RequestParam(value = "createAtStart", required = false) Timestamp createAtStart,
                                            @RequestParam(value = "createAtEnd", required = false) Timestamp createAtEnd
    ) {
        Map searchMap = Maps.newHashMap();
        Collection<Condition> conditions = Lists.newArrayList();
        if (null != orderId) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.ORDER_ID.eq(orderId));
        }
        searchMap.put("orderId", orderId);
        if (StringTools.isNotBlank(outTradeNo)) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.OUT_TRADE_NO.eq(outTradeNo));
        }
        searchMap.put("outTradeNo", outTradeNo);
        if (StringTools.isNotBlank(transactionId)) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.TRANSACTION_ID.eq(transactionId));
        }
        searchMap.put("transactionId", transactionId);
        if (null != this.getCurrentUserOgnId()) {
            conditions.add(EP.EP_ORDER.OGN_ID.eq(this.getCurrentUserOgnId()));
        }
        if (StringTools.isNotBlank(courseName)) {
            conditions.add(EP.EP_ORGAN_COURSE.COURSE_NAME.eq(courseName));
        }
        searchMap.put("courseName", courseName);
        if (StringTools.isNotBlank(className)) {
            conditions.add(EP.EP_ORGAN_CLASS.CLASS_NAME.eq(className));
        }
        searchMap.put("className", className);

        conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.DEL_FLAG.eq(false));
        Page<WechatUnifiedOrderBo> page = wechatUnifiedOrderService.findbyPageAndCondition(pageable, conditions, timeEndStart, timeEndEnd);
        model.addAttribute("page", page);
        searchMap.put("timeEndStart", timeEndStart);
        searchMap.put("timeEndEnd", timeEndEnd);
        if (null != createAtStart) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.CREATE_AT.greaterOrEqual(createAtStart));
        }
        searchMap.put("createAtStart", createAtStart);
        if (null != createAtEnd) {
            conditions.add(EP.EP_WECHAT_UNIFIED_ORDER.CREATE_AT.lessOrEqual(createAtEnd));
        }
        searchMap.put("createAtEnd", createAtEnd);
        model.addAttribute("searchMap", searchMap);

        return "wechat/unifiedorderMerchantIndex";
    }

    /**
     * 商户微信支付统一订单 分页
     *
     * @return
     */
    @GetMapping("findWechatUnifiedOrderByOrderId/{orderId}")
    @ResponseBody
    public ResultDo findWechatUnifiedOrderByOrderId(@PathVariable("orderId") Long orderId) {
        List<EpWechatUnifiedOrderPo> list = wechatUnifiedOrderService.findByOrderId(orderId);
        return ResultDo.build().setResult(list);
    }

    /**
     * 同步统一下单的订单
     *
     * @return
     */
    @GetMapping("syncUnifiedorder/{outTradeNo}")
    @ResponseBody
    public ResultDo syncUnifiedorder(@PathVariable("outTradeNo") String outTradeNo) throws Exception {
        return wechatPayComponent.orderQuery(null, outTradeNo, true);
    }

    /**
     * 退款申请
     *
     * @return
     */
    @GetMapping("payRefund/{outTradeNo}")
    @ResponseBody
    public ResultDo payRefund(@PathVariable("outTradeNo") String outTradeNo) throws Exception {
        EpWechatUnifiedOrderPo wechatUnifiedOrderPo = wechatUnifiedOrderService.getByOutTradeNo(outTradeNo);
        String outRefundNo = SerialNumberTools.generateOutRefundNo(wechatUnifiedOrderPo.getOrderId());
        return wechatPayComponent.payRefund(wechatUnifiedOrderPo.getTransactionId(), outTradeNo, outRefundNo,
                wechatUnifiedOrderPo.getTotalFee(), wechatUnifiedOrderPo.getTotalFee());
    }
}
