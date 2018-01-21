package com.ep.backend.security;

import com.ep.common.tool.CryptTools;
import com.ep.common.tool.WebTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.SystemUserRepository;
import com.ep.domain.repository.domain.enums.EpMemberStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;

/**
 * @Description: 鉴权组件（Spring Security）
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@Component
public class BackendSecurityAuthComponent {

    @Autowired
    private SystemUserRepository systemUserRepository;

    /**
     * 登录校验
     *
     * @param principal
     * @param credential
     * @throws AuthenticationException
     */
    public String[] checkLogin(String principal, String credential, String captchaCode) throws AuthenticationException {
        // 校验图形验证码
        Object captcha = WebTools.getCurrentRequest().getSession().getAttribute(BizConstant.CAPTCHA_SESSION_KEY);
        if (captcha == null || captchaCode == null) {
            throw new BadCredentialsException("请重新获取登录图形验证码！");
        } else if (!captcha.toString().equalsIgnoreCase(captchaCode)) {
            throw new BadCredentialsException("图形验证码错误！");
        }
        // 校验用户信息
        Long mobile = Long.valueOf(principal);
        // 校验会员
        EpSystemUserPo systemUserPo = systemUserRepository.getByMobile(mobile);
        if (systemUserPo == null) {
            throw new UsernameNotFoundException("账号不存在！");
        }
        String pwdEncode;
        try {
            pwdEncode = CryptTools.aesEncrypt(credential, systemUserPo.getSalt());
        } catch (GeneralSecurityException e) {
            log.error("用户密码鉴权异常：userSecret:{} 加密失败", credential);
            throw new BadCredentialsException("密码验证失败，请联系系统管理员！");
        }
        if (systemUserPo.getStatus().equals(EpMemberStatus.cancel)) {
            throw new UsernameNotFoundException("账号已被注销！");
        } else if (systemUserPo.getStatus().equals(EpMemberStatus.freeze)) {
            throw new LockedException("账号已被锁定！");
        } else if (!systemUserPo.getPassword().equals(pwdEncode)) {
            throw new BadCredentialsException("密码错误！");
        }
        //TODO 定位角色
        return new String[]{systemUserPo.getRole()};
    }
}
