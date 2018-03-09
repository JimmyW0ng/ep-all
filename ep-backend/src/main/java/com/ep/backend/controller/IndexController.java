package com.ep.backend.controller;

import com.ep.common.tool.CryptTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SystemMenuBo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.service.SystemMenuService;
import com.ep.domain.service.SystemUserRoleService;
import com.ep.domain.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
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

    /**
     * 登录成功后首页
     *
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/index")
    public String index(Model model) {
        EpSystemUserPo currentUser = getCurrentUser().get();
        List<Long> roleIds = systemUserRoleService.getRoleIdsByUserId(currentUser.getId());
        List<SystemMenuBo> leftMenu = systemMenuService.getLeftMenuByUserType(currentUser.getType(), roleIds);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("leftMenu", leftMenu);
        return "layout/default";
    }

    /**
     * 登录成功后ifeame首页
     *
     * @return
     */
    @GetMapping("/homePage")
    public String homePage() {
        return "index";
    }


    /**
     * 个人查看
     *
     * @param model
     * @return
     */
    @GetMapping("/settingView")
    public String settingView(Model model) throws GeneralSecurityException {
        EpSystemUserPo systemUserPo = super.getCurrentUser().get();
        systemUserPo.setPassword(CryptTools.aesDecrypt(systemUserPo.getPassword(), systemUserPo.getSalt()));

        model.addAttribute("systemUserPo", systemUserPo);

        return "/systemUser/view";

    }

    /**
     * 个人设置初始化
     *
     * @param model
     * @return
     */
    @GetMapping("/settingEdit")
    public String settingEdit(Model model) {
        EpSystemUserPo systemUserPo = super.getCurrentUser().get();

        model.addAttribute("systemUserPo", systemUserPo);
        return "/systemUser/settingForm";
    }

    /**
     * 个人设置修改密码
     *
     * @param oldPsd
     * @param password
     * @return
     * @throws GeneralSecurityException
     */
    @PostMapping("updatePsd")
    @ResponseBody
    public ResultDo updatePsd(@RequestParam(value = "oldPsd") String oldPsd,
                              @RequestParam(value = "password") String password) throws GeneralSecurityException {
        EpSystemUserPo systemUserPo = super.getCurrentUser().get();
        return systemUserService.updatePassword(systemUserPo.getId(), oldPsd, password);
    }

}
