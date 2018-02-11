package com.ep.backend.controller;

import com.ep.common.tool.CryptTools;
import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.SystemMenuService;
import com.ep.domain.service.SystemRoleService;
import com.ep.domain.service.SystemUserRoleService;
import com.ep.domain.service.SystemUserService;
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
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemUserRoleService systemUserRoleService;
    @Autowired
    private SystemRoleService systemRoleService;

    /**
     * 登录成功后首页
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        EpSystemUserPo currentUser = getCurrentUser(request).get();
        List<SystemMenuBo> leftMenu=systemMenuService.getLeftMenuByUserType(currentUser.getType());
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("leftMenu",leftMenu);
        return "layout/default";
    }

    /**
     * 登录成功后ifeame首页
     * @return
     */
    @GetMapping("/homePage")
    public String homePage() {
        return "index";
    }

    /**
     * 个人设置初始化
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/settingUpdateInit")
    public String settingUpdateInit(Model model, HttpServletRequest request) {
//        EpSystemUserPo currentUser = getCurrentUser(request).get();
//        List<SystemMenuBo> leftMenu=systemMenuService.getLeftMenuByUserType(currentUser.getType());
//        model.addAttribute("currentUser",currentUser);
//        model.addAttribute("leftMenu",leftMenu);
        return "layout/default";
    }

    /**
     * 个人查看
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/settingView")
    public String settingView(Model model, HttpServletRequest request) {
        Long id=super.getCurrentUser(request).get().getId();
        EpSystemUserPo systemUserPo = systemUserService.getById(id);
        List<Long> roleIds = systemUserRoleService.getRoleIdsByUserId(id);
        List<EpSystemRolePo> lists = systemRoleService.getAllRoleByUserType(systemUserPo.getType());
        try{
            systemUserPo.setPassword(CryptTools.aesDecrypt(systemUserPo.getPassword(),systemUserPo.getSalt()));

        }catch(Exception e){
            model.addAttribute("systemUserPo", systemUserPo);
            model.addAttribute("roleList", lists);
            model.addAttribute("roleIds", roleIds);
            return "/systemUser/view";
        }
        model.addAttribute("systemUserPo", systemUserPo);
        model.addAttribute("roleList", lists);
        model.addAttribute("roleIds", roleIds);
        return "/systemUser/settingView";
    }

}
