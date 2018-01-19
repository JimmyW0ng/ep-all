package com.ep.backend.security;

import com.ep.common.tool.*;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.BackendCredentialBo;
import com.ep.domain.pojo.bo.BackendPrincipalBo;
import com.ep.domain.pojo.po.EpSystemUserPo;
import com.ep.domain.repository.SystemUserRepository;
import com.ep.domain.repository.domain.enums.EpMemberStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * @Description: 鉴权组件（Spring Security）
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@Component
public class BackendSecurityAuthComponent {

    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.expiration}")
    private int tokenExpiration;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SystemUserRepository systemUserRepository;


    /**
     * 机构后台登录获取token
     *
     * @param userName
     * @param password
     * @return
     */
    public ResultDo<String> loginFromBackend(String userName,
                                             String password,
                                             String captchaCode) throws AuthenticationException {
        HttpSession session = WebTools.getCurrentRequest().getSession();
        ResultDo<String> resultDo = ResultDo.build();
        //校验验证码
        Object sessionCaptcha = session.getAttribute(BizConstant.CAPTCHA_SESSION_KEY);
        if (sessionCaptcha != null) {
            if (!sessionCaptcha.toString().toLowerCase().equals(captchaCode.toLowerCase())) {
                throw new AuthenticationServiceException("验证码错误");
            }
        } else {
            throw new AuthenticationServiceException("验证码无效，请重新获取");
        }
        // 认证身份
        BackendPrincipalBo principal = new BackendPrincipalBo();
        principal.setUserName(userName);
        principal.setCaptchaCode(captchaCode);
        BackendCredentialBo credentials = new BackendCredentialBo(password);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, credentials);
        final Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            // 生成token
            String accessToken = this.buildAccessToken((BackendPrincipalBo) authToken.getPrincipal());
            return resultDo.setResult(accessToken);
        } catch (GeneralSecurityException e) {
            return resultDo.setError(MessageCode.ERROR_ENCODE);
        }
    }


    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public ResultDo<BackendPrincipalBo> getTokenInfo(String token) {
        ResultDo<BackendPrincipalBo> resultDo = ResultDo.build();
        if (StringTools.isBlank(token)) {
            log.error("token解析失败，token为空:{}，", token);
            return resultDo.setError(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        BackendPrincipalBo principalBo;
        try {
            String tokenJsonStr = CryptTools.aesDecrypt(token, tokenSecret);
            principalBo = JsonTools.decode(tokenJsonStr, BackendPrincipalBo.class);
        } catch (GeneralSecurityException e) {
            log.error("token解密失败，token={}", token, e);
            return resultDo.setError(MessageCode.ERROR_DECODE);
        } catch (Exception e) {
            log.error("token解析失败，token={}", token, e);
            return resultDo.setError(MessageCode.ERROR_SYSTEM);
        }
        if (StringTools.isBlank(principalBo.getUserName())
                || StringTools.isBlank(principalBo.getRole())
                || principalBo.getCreateTime() == null
                || principalBo.getExpireTime() == null) {
            return resultDo.setError(MessageCode.ERROR_SESSION_TOKEN);
        }
        Date serverTime = DateTools.getCurrentDate();
        Date tokenCreateTime = new Date(principalBo.getCreateTime());
        Date tokenExpireTime = new Date(principalBo.getExpireTime());
        if (serverTime.before(tokenCreateTime) || serverTime.after(tokenExpireTime)) {
            return resultDo.setError(MessageCode.ERROR_SESSION_TOKEN);
        }
        return resultDo.setResult(principalBo);
    }

    /**
     * 登录校验
     *
     * @param principalBo
     * @param credentialBo
     * @throws AuthenticationException
     */
    public void checkLogin(BackendPrincipalBo principalBo, BackendCredentialBo credentialBo) throws AuthenticationException {
        // 校验用户信息
        Long mobile = Long.valueOf(principalBo.getUserName());
        // 校验会员
        EpSystemUserPo systemUserPo = systemUserRepository.getByMobile(mobile);
        String pwdEncode;
        try {
            pwdEncode = CryptTools.aesEncrypt(credentialBo.getPassword(), systemUserPo.getSalt());
        } catch (GeneralSecurityException e) {
            log.error("客户端client鉴权异常：clientSecret:{} 加密失败", credentialBo.getPassword());
            throw new BadCredentialsException("客户端凭证格式错误");
        }
        if (systemUserPo == null) {
            throw new UsernameNotFoundException("账号不存在");
        } else if (systemUserPo.getStatus().equals(EpMemberStatus.cancel)) {
            throw new UsernameNotFoundException("账号已被注销");
        } else if (systemUserPo.getStatus().equals(EpMemberStatus.freeze)) {
            throw new LockedException("账号已被锁定");
        } else if (!systemUserPo.getPassword().equals(pwdEncode)) {
            throw new BadCredentialsException("密码错误");
        }
        // 定位角色
        principalBo.setRole(systemUserPo.getRole());

    }

    /**
     * 加载当前用户信息
     *
     * @param principalBo
     */
    public void loadCurrentUserInfo(BackendPrincipalBo principalBo) {
        EpSystemUserPo user = systemUserRepository.getByMobile(Long.parseLong(principalBo.getUserName()));
        WebTools.getCurrentRequest().setAttribute(BizConstant.CURENT_BACKEND_USER, user);
    }

    /**
     * 加载当前用户信息
     *
     * @return
     */
    public EpSystemUserPo getCurrentUserRole() {
        return (EpSystemUserPo) WebTools.getCurrentRequest().getAttribute(BizConstant.CURENT_BACKEND_USER);
    }

    /**
     * 生成鉴权token
     *
     * @param principal
     * @return
     * @throws GeneralSecurityException
     */
    private String buildAccessToken(BackendPrincipalBo principal) throws GeneralSecurityException {
        Date now = DateTools.getCurrentDate();
        principal.setCreateTime(now.getTime());
        principal.setExpireTime(DateTools.addSecond(now, tokenExpiration).getTime());
        principal.setRole(null);
        String tokenJsonStr = JsonTools.encode(principal);
        return CryptTools.aesEncrypt(tokenJsonStr, tokenSecret);
    }

}
