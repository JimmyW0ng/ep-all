package com.ep.api.security;

import com.ep.common.tool.CryptTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SecurityCredentialBo;
import com.ep.domain.pojo.bo.SecurityPrincipalBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 鉴权组件（Spring Security）
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@Component
public class SecurityAuthComponent {

    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.expiration}")
    private int tokenExpiration;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private SysClientRepository sysClientRepository;
//
//    @Autowired
//    private MbrInfoRepository mbrInfoRepository;
//
//    @Autowired
//    private OgnAccountRepository ognAccountRepository;
//
//    @Autowired
//    private SysUserRepository sysUserRepository;
//
//    @Autowired
//    private SysRoleAuthorityRepository sysRoleAuthorityRepository;
//
//    @Autowired
//    private MsgCaptchaRepository msgCaptchaRepository;
//
//    /**
//     * 会员登录获取token
//     *
//     * @param userName
//     * @param captchaCode
//     * @param captchaContent
//     * @param clientId
//     * @param clientSecret
//     * @return
//     */
//    public ResultDo<String> loginFromApi(String userName,
//                                         String captchaCode,
//                                         String captchaContent,
//                                         String clientId,
//                                         String clientSecret) {
//        ResultDo<String> resultDo = ResultDo.build();
//        if (StringTools.isBlank(userName)
//                || StringTools.isBlank(captchaCode)
//                || StringTools.isBlank(captchaContent)
//                || StringTools.isBlank(clientId)
//                || StringTools.isBlank(clientSecret)) {
//            log.error("登录获取token失败，入参缺失！");
//            return ResultDo.build(MessageCodeConstant.ERROR_SYSTEM_PARAM_FORMAT);
//        }
//        // 认证身份
//        SecurityPrincipalBo principal = new SecurityPrincipalBo(userName, clientId);
//        // 验证码业务编码
//        principal.setCaptchaCode(captchaCode);
//        SecurityCredentialBo credentials = new SecurityCredentialBo(captchaContent, clientSecret);
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, credentials);
//        final Authentication authentication = authenticationManager.authenticate(authToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        try {
//            // 生成token
//            String accessToken = this.buildAccessToken(authToken.getPrincipal());
//            return resultDo.setResult(accessToken);
//        } catch (GeneralSecurityException e) {
//            return resultDo.setError(MessageCodeConstant.ERROR_ENCODE);
//        }
//    }
//
//    /**
//     * 平台后台登录获取token
//     *
//     * @param userName
//     * @param password
//     * @param clientId
//     * @param clientSecret
//     * @return
//     */
//    public ResultDo<String> loginFromAdmin(String userName,
//                                           String password,
//                                           String clientId,
//                                           String clientSecret) {
//        ResultDo<String> resultDo = ResultDo.build();
//        if (StringTools.isBlank(userName)
//                || StringTools.isBlank(password)
//                || StringTools.isBlank(clientId)
//                || StringTools.isBlank(clientSecret)) {
//            log.error("登录获取token失败，入参缺失！");
//            return ResultDo.build(MessageCodeConstant.ERROR_SYSTEM_PARAM_FORMAT);
//        }
//        // 认证身份
//        SecurityPrincipalBo principal = new SecurityPrincipalBo(userName, clientId);
//        SecurityCredentialBo credentials = new SecurityCredentialBo(password, clientSecret);
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, credentials);
//        final Authentication authentication = authenticationManager.authenticate(authToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        try {
//            // 生成token
//            String accessToken = this.buildAccessToken(authToken.getPrincipal());
//            return resultDo.setResult(accessToken);
//        } catch (GeneralSecurityException e) {
//            return resultDo.setError(MessageCodeConstant.ERROR_ENCODE);
//        }
//    }
//
//    /**
//     * 机构后台登录获取token
//     *
//     * @param userName
//     * @param password
//     * @param clientId
//     * @param clientSecret
//     * @return
//     */
//    public ResultDo<String> loginFromBackend(Long ognId,
//                                             String userName,
//                                             String password,
//                                             String clientId,
//                                             String clientSecret) {
//        ResultDo<String> resultDo = ResultDo.build();
//        if (ognId == null
//                || StringTools.isBlank(userName)
//                || StringTools.isBlank(password)
//                || StringTools.isBlank(clientId)
//                || StringTools.isBlank(clientSecret)) {
//            log.error("登录获取token失败，入参缺失！");
//            return ResultDo.build(MessageCodeConstant.ERROR_SYSTEM_PARAM_FORMAT);
//        }
//        // 认证身份
//        SecurityPrincipalBo principal = new SecurityPrincipalBo(userName, clientId);
//        principal.setOgnId(ognId);
//        SecurityCredentialBo credentials = new SecurityCredentialBo(password, clientSecret);
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, credentials);
//        final Authentication authentication = authenticationManager.authenticate(authToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        try {
//            // 生成token
//            String accessToken = this.buildAccessToken(authToken.getPrincipal());
//            return resultDo.setResult(accessToken);
//        } catch (GeneralSecurityException e) {
//            return resultDo.setError(MessageCodeConstant.ERROR_ENCODE);
//        }
//    }
//
//    /**
//     * 解析token
//     *
//     * @param token
//     * @return
//     */
    public ResultDo<SecurityPrincipalBo> getTokenInfo(String token) {
        return null;
//        ResultDo<SecurityPrincipalBo> resultDo = ResultDo.build();
//        if (StringTools.isBlank(token)) {
//            log.error("token解析失败，token为空:{}，", token);
//            return resultDo.setError(MessageCodeConstant.ERROR_SYSTEM_PARAM_FORMAT);
//        }
//        try {
//            String tokenJsonStr = CryptTools.aesDecrypt(token, tokenSecret);
//            SecurityPrincipalBo principalBo = JsonTools.decode(tokenJsonStr, SecurityPrincipalBo.class);
//            if (StringTools.isBlank(principalBo.getUserName())
//                    || StringTools.isBlank(principalBo.getClientId())
//                    || StringTools.isBlank(principalBo.getRole())
//                    || principalBo.getCreateTime() == null
//                    || principalBo.getExpireTime() == null) {
//                return resultDo.setError(MessageCodeConstant.ERROR_SESSION_TOKEN);
//            }
//            Date serverTime = DateTools.getCurrentDate();
//            Date tokenCreateTime = new Date(principalBo.getCreateTime());
//            Date tokenExpireTime = new Date(principalBo.getExpireTime());
//            if (serverTime.before(tokenCreateTime) || serverTime.after(tokenExpireTime)) {
//                return resultDo.setError(MessageCodeConstant.ERROR_SESSION_TOKEN);
//            }
//            return resultDo.setResult(principalBo);
//        } catch (GeneralSecurityException e) {
//            log.error("token解密失败，token={}", token, e);
//            return resultDo.setError(MessageCodeConstant.ERROR_DECODE);
//        } catch (Exception e) {
//            log.error("token解析失败，token={}", token, e);
//            return resultDo.setError(MessageCodeConstant.ERROR_SYSTEM);
//        }
    }
//
    /**
     * 加载用户信息
     *
     * @param request
     * @param principalBo
     */
    public void loadCurrentUserInfo(HttpServletRequest request, SecurityPrincipalBo principalBo) {
//        SysClientPo clientPo = sysClientRepository.getByClientId(principalBo.getClientId());
//        if (clientPo.getLoginSource().equals(SysClientLoginSource.wechat_app)) {
//            // 微信小程序
//            MbrInfoPo mbrInfoPo = mbrInfoRepository.getByMobile(Long.parseLong(principalBo.getUserName()));
//            request.setAttribute(BizConstant.CURENT_USER, mbrInfoPo);
//        } else if (clientPo.getLoginSource().equals(SysClientLoginSource.backend)) {
//            // 机构后台
//            OgnAccountPo ognAccountPo = ognAccountRepository.getByMobileAndOgnId(Long.parseLong(principalBo.getUserName()), principalBo.getOgnId());
//            request.setAttribute(BizConstant.CURENT_BACKEND_USER, ognAccountPo);
//        } else if (clientPo.getLoginSource().equals(SysClientLoginSource.admin)) {
//            // 平台管理后台用户
//            SysUserPo sysUserPo = sysUserRepository.getByMobile(Long.parseLong(principalBo.getUserName()));
//            request.setAttribute(BizConstant.CURENT_ADMIN_USER, sysUserPo);
//        }
    }
//
    /**
     * 加载用户的权限
     *
     * @param role
     * @return
     */
    public Collection<GrantedAuthority> loadCurrentUserGrantedAuthorities(String role) {
        return null;
    }
//
    /**
     * 登录校验
     *
     * @param principalBo
     * @param credentialBo
     * @throws AuthenticationException
     */
    public void checkLogin(SecurityPrincipalBo principalBo, SecurityCredentialBo credentialBo) throws AuthenticationException {


    }
//
//    /**
//     * 客户端身份校验
//     *
//     * @param clientId
//     * @param clientSecret
//     * @return
//     */
//    public ResultDo checkPrincipal(String clientId, String clientSecret) {
//        // 校验client
//        SysClientPo sysClientPo = sysClientRepository.getByClientId(clientId);
//        if (sysClientPo == null || !sysClientPo.getArchived()) {
//            log.error("客户端client鉴权异常：clientId:{} 在数据库中不存在", clientId);
//            return ResultDo.build(MessageCodeConstant.ERROR_PRINCIPAL_CHECK);
//        }
//        String pwdEncode;
//        try {
//            pwdEncode = CryptTools.aesEncrypt(clientSecret, sysClientPo.getSalt());
//        } catch (GeneralSecurityException e) {
//            log.error("客户端client鉴权异常：clientSecret:{} 加密失败", clientSecret);
//            return ResultDo.build(MessageCodeConstant.ERROR_PRINCIPAL_CHECK);
//        }
//        if (!pwdEncode.equals(sysClientPo.getClientSecret())) {
//            log.error("客户端凭证无效");
//            return ResultDo.build(MessageCodeConstant.ERROR_PRINCIPAL_CHECK);
//        }
//        return ResultDo.build();
//    }
//
//    /**
//     * 小程序端安全校验已经配置角色
//     *
//     * @param principalBo
//     * @param credentialBo
//     * @param role
//     * @throws AuthenticationException
//     */
//    private void checkWechatAppPrincipal(SecurityPrincipalBo principalBo, SecurityCredentialBo credentialBo, String role) throws AuthenticationException {
//        Long mobile = Long.valueOf(principalBo.getUserName());
//        // 校验验证码
//        this.checkAndHandleCaptcha(mobile, principalBo.getCaptchaCode(), credentialBo.getPassword());
//        // 校验会员
//        MbrInfoPo mbrInfoPo = mbrInfoRepository.getByMobile(mobile);
//        if (mbrInfoPo == null) {
//            // 保存用户
//            MbrInfoPo insertPo = new MbrInfoPo();
//            insertPo.setMobile(mobile);
//            insertPo.setStatus(MbrInfoStatus.normal);
//            mbrInfoRepository.insert(insertPo);
//        } else if (mbrInfoPo.getStatus().equals(MbrInfoStatus.cancel)) {
//            throw new UsernameNotFoundException("账号已被注销");
//        } else if (mbrInfoPo.getStatus().equals(MbrInfoStatus.freeze)) {
//            throw new LockedException("账号已被锁定");
//        }
//        // 定位角色
//        principalBo.setRole(role);
//    }
//
//    /**
//     * 后端安全校验已经配置角色
//     *
//     * @param principalBo
//     * @param credentialBo
//     */
//    private void checkBackendPrincipal(SecurityPrincipalBo principalBo, SecurityCredentialBo credentialBo) {
//        Long mobile = Long.valueOf(principalBo.getUserName());
//        OgnAccountPo accountPo = ognAccountRepository.getByMobileAndOgnId(mobile, principalBo.getOgnId());
//        if (accountPo == null || accountPo.getStatus().equals(OgnAccountStatus.cancel)) {
//            throw new UsernameNotFoundException("用户不存在");
//        } else if (accountPo.getStatus().equals(OgnAccountStatus.freeze)) {
//            throw new LockedException("账号已被锁定");
//        }
//        this.checkPassword(credentialBo.getPassword(), accountPo.getSalt(), accountPo.getPassword());
//        // 定位角色
//        principalBo.setRole(accountPo.getRole());
//    }
//
//    /**
//     * 平台系统后台安全校验已经配置角色
//     *
//     * @param principalBo
//     * @param credentialBo
//     */
//    private void checkAdminPrincipal(SecurityPrincipalBo principalBo, SecurityCredentialBo credentialBo) {
//        Long mobile = Long.valueOf(principalBo.getUserName());
//        SysUserPo sysUserPo = sysUserRepository.getByMobile(mobile);
//        if (sysUserPo == null || sysUserPo.getStatus().equals(SysUserStatus.cancel)) {
//            throw new UsernameNotFoundException("用户不存在");
//        } else if (sysUserPo.getStatus().equals(SysUserStatus.freeze)) {
//            throw new LockedException("账号已被锁定");
//        }
//        this.checkPassword(credentialBo.getPassword(), sysUserPo.getSalt(), sysUserPo.getPassword());
//        // 定位角色
//        principalBo.setRole(sysUserPo.getRole());
//    }
//
//    /**
//     * 用户密码校验
//     *
//     * @param source
//     * @param salt
//     * @param target
//     */
//    private void checkPassword(String source, String salt, String target) {
//        try {
//            // 加密
//            String password = CryptTools.aesEncrypt(source, salt);
//            if (!password.equals(target)) {
//                throw new BadCredentialsException("用户名或密码错误");
//            }
//        } catch (GeneralSecurityException e) {
//            log.error("密码加密失败！password={}", source, e);
//            throw new BadCredentialsException("用户名或密码错误");
//        }
//    }
//
//    /**
//     * 图形验证码校验
//     *
//     * @param sourceId
//     * @param captchaCode
//     * @param captchaContent
//     */
//    private void checkAndHandleCaptcha(Long sourceId, String captchaCode, String captchaContent) {
//        MsgCaptchaPo captchaPo = msgCaptchaRepository.getBySourceIdAndCaptchaCode(sourceId,
//                MsgCaptchaCaptchaType.short_msg,
//                MsgCaptchaCaptchaScene.login,
//                captchaCode);
//        if (captchaPo == null || captchaPo.getExpireTime().before(DateTools.getCurrentDateTime())) {
//            throw new BadCredentialsException("验证码无效，请重新获取");
//        } else if (!captchaContent.equals(captchaPo.getCaptchaContent())) {
//            throw new BadCredentialsException("验证码错误");
//        }
//        // 验证码使用后删除
//        msgCaptchaRepository.delBySourceIdAndTypeAndSence(sourceId, MsgCaptchaCaptchaType.short_msg, MsgCaptchaCaptchaScene.login);
//    }
//
//    /**
//     * 生成鉴权token
//     *
//     * @param principal
//     * @return
//     * @throws GeneralSecurityException
//     */
//    private String buildAccessToken(Object principal) throws GeneralSecurityException {
//        Date now = DateTools.getCurrentDate();
//        SecurityPrincipalBo securityPrincipalBo = (SecurityPrincipalBo) principal;
//        securityPrincipalBo.setCreateTime(now.getTime());
//        securityPrincipalBo.setExpireTime(DateTools.addSecond(now, tokenExpiration).getTime());
//        String tokenJsonStr = JsonTools.encode(securityPrincipalBo);
//        return CryptTools.aesEncrypt(tokenJsonStr, tokenSecret);
//    }

}
