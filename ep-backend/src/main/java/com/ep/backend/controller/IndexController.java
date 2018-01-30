package com.ep.backend.controller;

import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.SystemMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 首页控制基类
 * @Author: FCC
 * @Date: 下午4:55 2018/1/21
 */
@Controller
@RequestMapping("auth")
public class IndexController extends BackendController {
    @Autowired
    private SystemMenuService systemMenuService;

    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        EpSystemUserPo currentUser = getCurrentUser(request).get();
        List<SystemMenuBo> leftMenu=systemMenuService.getLeftMenuByUserType(currentUser.getType());
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("leftMenu",leftMenu);
        return "layout/default";
    }

}
