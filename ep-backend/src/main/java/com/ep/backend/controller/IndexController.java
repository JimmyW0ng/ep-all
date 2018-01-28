package com.ep.backend.controller;

import com.ep.domain.pojo.po.EpSystemUserPo;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
//    @PreAuthorize("hasAnyAuthority('admin:organ:page')")
    public String index(Model model, HttpServletRequest request) {
        EpSystemUserPo currentUser = getCurrentUser(request).get();
        model.addAttribute("currentUser",currentUser);
        return "layout/default";
    }

    @GetMapping("/loginOut")
    public String loginOut(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/security/login";
//        登出的操作需要进行下面几个步骤：
//
//        1.使HTTP Session失效,然后解绑Session上的所以已绑定的对象。
//        2.将用户的认证信息从spring security的SecurityContext中移除，以防止并发请求的问题。
//        3.从当前线程中，完全的清除相关的属性值。
    }
}
