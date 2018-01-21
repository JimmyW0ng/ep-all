package com.ep.backend.controller;

import com.ep.common.component.SpringComponent;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.SystemUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @Description: 后台控制基类
 * @Author: J.W
 * @Date: 下午4:55 2018/1/21
 */
public class BackendController {

    /**
     * 从session获取当前登录用户
     *
     * @param request
     * @return
     */
    protected Optional<EpSystemUserPo> getCurrentUser(HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return Optional.empty();
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return Optional.empty();
        }
        Long mobile = Long.valueOf(principal.toString());
        EpSystemUserPo currentUser = SpringComponent.getBean(SystemUserRepository.class).getByMobile(mobile);
        return Optional.ofNullable(currentUser);
    }

}
