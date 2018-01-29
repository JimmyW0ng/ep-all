package com.ep.backend.controller;

import com.ep.domain.pojo.po.EpSystemUserPo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 首页控制基类
 * @Author: FCC
 * @Date: 下午4:55 2018/1/21
 */
@Controller
@RequestMapping("auth")
public class IndexController extends BackendController {

    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        EpSystemUserPo currentUser = getCurrentUser(request).get();
        model.addAttribute("currentUser",currentUser);
        return "layout/default";
    }

}
