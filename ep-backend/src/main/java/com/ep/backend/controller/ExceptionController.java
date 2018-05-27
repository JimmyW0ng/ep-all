package com.ep.backend.controller;

import com.ep.common.tool.DateTools;
import com.ep.domain.component.WechatPayComponent;
import com.ep.domain.pojo.ResultDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 16:47 2018/5/27
 */
@Slf4j
@Controller
@RequestMapping("auth/exception")
public class ExceptionController extends BackendController {
    @Autowired
    private WechatPayComponent wechatPayComponent;

    @GetMapping("wechatPayIndex")
    public String wechatPayIndex() {
        return "exception/wechatPayIndex";
    }

    @GetMapping("wechatPay/bill")
    @ResponseBody
    public ResultDo bill(@RequestParam(value = "billDate") String billDate) {
        log.info("[对账异常]对账异常处理开始，billDate={}，操作者={}。", billDate, this.getCurrentUser().get().getId());
        return wechatPayComponent.downloadbill(DateTools.stringToDate(billDate, DateTools.DATE_FMT_0));
    }

}
