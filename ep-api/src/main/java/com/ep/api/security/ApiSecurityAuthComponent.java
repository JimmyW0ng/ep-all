package com.ep.api.security;

import com.ep.common.tool.*;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.ApiCredentialBo;
import com.ep.domain.pojo.bo.ApiPrincipalBo;
import com.ep.domain.pojo.dto.ApiLoginDto;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.pojo.po.EpSystemClientPo;
import com.ep.domain.pojo.po.EpSystemRolePo;
import com.ep.domain.repository.SystemClientRepository;
import com.ep.domain.repository.SystemMenuRepository;
import com.ep.domain.repository.SystemRoleRepository;
import com.ep.domain.repository.domain.enums.EpMemberStatus;
import com.ep.domain.repository.domain.enums.EpSystemClientLoginSource;
import com.ep.domain.service.MemberService;
import com.ep.domain.service.MessageCaptchaService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description: 鉴权组件（Spring Security）
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@Slf4j
@Component
public class ApiSecurityAuthComponent {

    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.expiration}")
    private int tokenExpiration;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SystemClientRepository systemClientRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MessageCaptchaService messageCaptchaService;
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    @Autowired
    private SystemMenuRepository systemMenuRepository;

    /**
     * 会员登录获取token
     *
     * @param userName
     * @param captchaCode
     * @param captchaContent
     * @param clientId
     * @param clientSecret
     * @return
     */
    public ResultDo<ApiLoginDto> loginFromApi(String userName,
                                              String captchaCode,
                                              String captchaContent,
                                              String clientId,
                                              String clientSecret) {
        if (StringTools.isBlank(userName)
                || StringTools.isBlank(captchaCode)
                || StringTools.isBlank(captchaContent)
                || StringTools.isBlank(clientId)
                || StringTools.isBlank(clientSecret)) {
            log.error("登录获取token失败，入参缺失！");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        // 认证身份
        ApiPrincipalBo principal = new ApiPrincipalBo();
        principal.setUserName(userName);
        principal.setClientId(clientId);
        // 验证码业务编码
        principal.setCaptchaCode(captchaCode);
        ApiCredentialBo credentials = new ApiCredentialBo(captchaContent, clientSecret);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, credentials);
        final Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            ResultDo<ApiLoginDto> resultDo = ResultDo.build();
            // 生成token
            ApiPrincipalBo principalBo = (ApiPrincipalBo) authToken.getPrincipal();
            String accessToken = this.buildAccessToken(principalBo);
            ApiLoginDto loginDto = new ApiLoginDto(accessToken, principalBo.getMemberType());
            return resultDo.setResult(loginDto);
        } catch (GeneralSecurityException e) {
            return ResultDo.build(MessageCode.ERROR_ENCODE);
        }
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    public ResultDo<ApiPrincipalBo> getTokenInfo(String token) {
        ResultDo<ApiPrincipalBo> resultDo = ResultDo.build();
        if (StringTools.isBlank(token)) {
            log.error("token解析失败，token为空:{}，", token);
            return resultDo.setError(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        ApiPrincipalBo principalBo;
        try {
            String tokenJsonStr = CryptTools.aesDecrypt(token, tokenSecret);
            principalBo = JsonTools.decode(tokenJsonStr, ApiPrincipalBo.class);
        } catch (GeneralSecurityException e) {
            log.error("token解密失败，token={}", token, e);
            return resultDo.setError(MessageCode.ERROR_DECODE);
        } catch (Exception e) {
            log.error("token解析失败，token={}", token, e);
            return resultDo.setError(MessageCode.ERROR_SYSTEM);
        }
        if (StringTools.isBlank(principalBo.getUserName())
                || StringTools.isBlank(principalBo.getClientId())
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
     * 加载用户信息
     *
     * @param principalBo
     */
    public void loadCurrentUserInfo(ApiPrincipalBo principalBo) {
        EpMemberPo mbrInfoPo = memberService.getByMobile(Long.parseLong(principalBo.getUserName()));
        WebTools.getCurrentRequest().setAttribute(BizConstant.CURENT_USER, mbrInfoPo);
    }

    /**
     * 登录校验
     *
     * @param principalBo
     * @param credentialBo
     * @throws AuthenticationException
     */
    public void checkLogin(ApiPrincipalBo principalBo, ApiCredentialBo credentialBo) throws AuthenticationException {
        // 校验client
        EpSystemClientPo sysClientPo = systemClientRepository.getByClientId(principalBo.getClientId());
        if (sysClientPo == null
                || !sysClientPo.getArchived()
                || !sysClientPo.getLoginSource().equals(EpSystemClientLoginSource.wechat_app)) {
            log.error("客户端client鉴权异常：clientId:{} 在数据库中不存在", principalBo.getClientId());
            throw new UsernameNotFoundException("客户端身份不存在");
        }
        String pwdEncode;
        try {
            pwdEncode = CryptTools.aesEncrypt(credentialBo.getClientSecret(), sysClientPo.getSalt());
        } catch (GeneralSecurityException e) {
            log.error("客户端client鉴权异常：clientSecret:{} 加密失败", credentialBo.getClientSecret());
            throw new BadCredentialsException("客户端凭证格式错误");
        }
        if (!pwdEncode.equals(sysClientPo.getClientSecret())) {
            throw new BadCredentialsException("客户端凭证无效");
        }
        // 校验用户信息
        Long mobile = Long.valueOf(principalBo.getUserName());
        // 校验验证码
        messageCaptchaService.checkAndHandleCaptcha(mobile, principalBo.getCaptchaCode(), credentialBo.getPassword());
        // 校验会员
        EpMemberPo mbrInfoPo = memberService.checkExistAndType(mobile);
        if (mbrInfoPo.getStatus().equals(EpMemberStatus.cancel)) {
            throw new UsernameNotFoundException("账号已被注销");
        } else if (mbrInfoPo.getStatus().equals(EpMemberStatus.freeze)) {
            throw new LockedException("账号已被锁定");
        }
        // 定位角色
        principalBo.setRole(sysClientPo.getRole());
        principalBo.setMemberType(mbrInfoPo.getType());
    }

    /**
     * 客户端身份校验
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    public ResultDo checkPrincipal(String clientId, String clientSecret) {
        // 校验client
        EpSystemClientPo sysClientPo = systemClientRepository.getByClientId(clientId);
        if (sysClientPo == null
                || !sysClientPo.getArchived()
                || !sysClientPo.getLoginSource().equals(EpSystemClientLoginSource.wechat_app)) {
            log.error("客户端client鉴权异常：clientId:{} 在数据库中不存在", clientId);
            return ResultDo.build(MessageCode.ERROR_PRINCIPAL_CHECK);
        }
        String pwdEncode;
        try {
            pwdEncode = CryptTools.aesEncrypt(clientSecret, sysClientPo.getSalt());
        } catch (GeneralSecurityException e) {
            log.error("客户端client鉴权异常：clientSecret:{} 加密失败", clientSecret);
            return ResultDo.build(MessageCode.ERROR_PRINCIPAL_CHECK);
        }
        if (!pwdEncode.equals(sysClientPo.getClientSecret())) {
            log.error("客户端凭证无效");
            return ResultDo.build(MessageCode.ERROR_PRINCIPAL_CHECK);
        }
        return ResultDo.build();
    }

    /**
     * 根据角色编码
     *
     * @param roleCode
     * @return
     */
    public Collection<GrantedAuthority> getPermissionByRoleCode(String roleCode) throws AuthenticationException {
        EpSystemRolePo rolePo = systemRoleRepository.getByCode(roleCode);
        List<String> permissions = systemMenuRepository.getByRoleId(rolePo.getId());
        Collection<GrantedAuthority> authorities = Lists.newArrayList();
        if (CollectionsTools.isNotEmpty(permissions)) {
            permissions.forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
        }
        return authorities;
    }

    /**
     * 生成鉴权token
     *
     * @param principal
     * @return
     * @throws GeneralSecurityException
     */
    private String buildAccessToken(ApiPrincipalBo principal) throws GeneralSecurityException {
        Date now = DateTools.getCurrentDate();
        principal.setCreateTime(now.getTime());
        principal.setExpireTime(DateTools.addSecond(now, tokenExpiration).getTime());
        String tokenJsonStr = JsonTools.encode(principal);
        return CryptTools.aesEncrypt(tokenJsonStr, tokenSecret);
    }

}
