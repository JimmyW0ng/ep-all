package com.ep.backend.controller;

import com.ep.domain.pojo.po.EpSystemUserPo;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fcc on 2018/1/17.
 */
@Controller
@RequestMapping("auth")
public class IndexController extends BackendController {

    /**
     * 后台首页，左侧菜单
     *
     * @return
     */
    @ApiOperation(value = "首页")
    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String index(Model model, HttpServletRequest request) {
        EpSystemUserPo currentUser = getCurrentUser(request).get();
        return "index";
    }

}
