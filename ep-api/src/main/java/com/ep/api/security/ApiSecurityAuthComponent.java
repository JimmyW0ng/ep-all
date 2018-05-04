package com.ep.api.security;

import com.ep.common.tool.*;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.ApiCredentialBo;
import com.ep.domain.pojo.bo.ApiPrincipalBo;
import com.ep.domain.pojo.dto.ApiLoginDto;
import com.ep.domain.pojo.po.*;
import com.ep.domain.repository.*;
import com.ep.domain.repository.domain.enums.EpMemberStatus;
import com.ep.domain.repository.domain.enums.EpOrganAccountStatus;
import com.ep.domain.repository.domain.enums.EpSystemClientLoginSource;
import com.ep.domain.repository.domain.enums.EpTokenType;
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
import java.util.List;
import java.util.Optional;

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
    private MemberRepository memberRepository;
    @Autowired
    private OrganAccountRepository organAccountRepository;
    @Autowired
    private MessageCaptchaService messageCaptchaService;
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    @Autowired
    private SystemMenuRepository systemMenuRepository;
    @Autowired
    private TokenRepository tokenRepository;

    /**
     * 会员登录获取token
     *
     * @param userName
     * @param captchaCode
     * @param captchaContent
     * @param clientId
     * @param clientSecret
     * @param type
     * @param ognId
     * @return
     */
    public ResultDo<ApiLoginDto> loginFromApi(String userName,
                                              String captchaCode,
                                              String captchaContent,
                                              String clientId,
                                              String clientSecret,
                                              String type,
                                              Long ognId) {
        if (StringTools.isBlank(userName)
                || StringTools.isBlank(captchaCode)
                || StringTools.isBlank(captchaContent)
                || StringTools.isBlank(clientId)
                || StringTools.isBlank(clientSecret)
                || (BizConstant.WECHAT_APP_ORGAN_CLIENT.equals(type) && ognId == null)) {
            log.error("登录获取token失败，入参缺失！");
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        // 认证身份
        ApiPrincipalBo principal = new ApiPrincipalBo();
        principal.setUserName(userName);
        principal.setClientId(clientId);
        principal.setType(type);
        principal.setOgnId(ognId);
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
            ApiLoginDto loginDto = new ApiLoginDto(accessToken);
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
    public ResultDo<EpTokenPo> getTokenInfo(String token, String ip) {
        ResultDo<EpTokenPo> resultDo = ResultDo.build();
        if (StringTools.isBlank(token)) {
            log.error("token解析失败，token为空:{}，", token);
            return resultDo.setError(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        Long tokenId = null;
        try {
            tokenId = StringTools.decodeShortUrl(token, tokenSecret, BizConstant.TOKEN_MIN_LENGTH)[0];
        } catch (Exception e) {
            log.error("token解析失败，token={}", token, e);
            return resultDo.setError(MessageCode.ERROR_SYSTEM);
        }
        EpTokenPo tokenPo = tokenRepository.getById(tokenId);
        if (tokenPo == null) {
            return resultDo.setError(MessageCode.ERROR_SESSION_TOKEN);
        }
        if (tokenPo.getDelFlag() || DateTools.getCurrentDateTime().after(tokenPo.getExpireTime())) {
            tokenRepository.delete(tokenId);
            return resultDo.setError(MessageCode.ERROR_SESSION_TOKEN);
        }
        // 更新访问ip
        tokenRepository.updateLastAccessIpById(tokenId, ip);
        return resultDo.setResult(tokenPo);
    }

    /**
     * 加载用户信息
     *
     * @param tokenPo
     */
    public ResultDo loadCurrentUserInfo(EpTokenPo tokenPo) {
        if (EpTokenType.wechat_app_member_clinet.equals(tokenPo.getType())) {
            EpMemberPo mbrInfoPo = memberRepository.getByMobile(tokenPo.getMobile());
            if (mbrInfoPo == null) {
                log.error("当前Token会员不存在, mobile={}", tokenPo.getMobile());
                return ResultDo.build(MessageCode.ERROR_MEMBER_NOT_EXISTS);
            } else if (mbrInfoPo.getDelFlag() || EpMemberStatus.cancel.equals(mbrInfoPo.getStatus())) {
                log.error("当前Token会员已注销, mobile={}", tokenPo.getMobile());
                return ResultDo.build(MessageCode.ERROR_MEMBER_IS_CANCEL);
            } else if (EpMemberStatus.freeze.equals(mbrInfoPo.getStatus())) {
                log.error("当前Token会员已冻结, mobile={}", tokenPo.getMobile());
                return ResultDo.build(MessageCode.ERROR_MEMBER_IS_FREEZE);
            }
            WebTools.getCurrentRequest().setAttribute(BizConstant.CURENT_USER, mbrInfoPo);
        } else if (EpTokenType.wechat_app_organ_client.equals(tokenPo.getType())) {
            Optional<EpOrganAccountPo> existOrganAccount = organAccountRepository.getByOgnIdAndReferMobile(tokenPo.getOgnId(), tokenPo.getMobile());
            if (!existOrganAccount.isPresent()) {
                log.error("当前Token机构账户不存在, mobile={}, ognId={}", tokenPo.getMobile(), tokenPo.getOgnId());
                return ResultDo.build(MessageCode.ERROR_ORGAN_ACCOUNT_NOT_EXISTS);
            }
            EpOrganAccountPo organAccountPo = existOrganAccount.get();
            if (EpOrganAccountStatus.cancel.equals(organAccountPo.getStatus())) {
                log.error("当前Token机构账户已注销, mobile={}, ognId={}", tokenPo.getMobile(), tokenPo.getOgnId());
                return ResultDo.build(MessageCode.ERROR_ORGAN_ACCOUNT_IS_CANCEL);
            } else if (EpOrganAccountStatus.freeze.equals(organAccountPo.getStatus())) {
                log.error("当前Token机构账户已冻结, mobile={}, ognId={}", tokenPo.getMobile(), tokenPo.getOgnId());
                return ResultDo.build(MessageCode.ERROR_ORGAN_ACCOUNT_IS_FREEZE);
            }
            WebTools.getCurrentRequest().setAttribute(BizConstant.CURENT_ORGAN_ACCOUNT, organAccountPo);
        }
        return ResultDo.build();
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
        principalBo.setMobile(mobile);
        // 校验验证码
        messageCaptchaService.checkAndHandleCaptcha(mobile, principalBo.getType(), principalBo.getCaptchaCode(), credentialBo.getPassword());
        // 校验会员
        this.checkExistAndType(principalBo);
        // 定位角色
        principalBo.setRole(sysClientPo.getRole());
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
        EpTokenPo tokenPo = new EpTokenPo();
        tokenPo.setMobile(principal.getMobile());
        if (BizConstant.WECHAT_APP_MEMBER_CLIENT.equals(principal.getType())) {
            tokenPo.setType(EpTokenType.wechat_app_member_clinet);
        } else if (BizConstant.WECHAT_APP_ORGAN_CLIENT.equals(principal.getType())) {
            tokenPo.setType(EpTokenType.wechat_app_organ_client);
            tokenPo.setOgnId(principal.getOgnId());
        }
        tokenPo.setRole(principal.getRole());
        tokenPo.setExpireTime(DateTools.addSecond(DateTools.getCurrentDateTime(), tokenExpiration));
        tokenRepository.insert(tokenPo);
        String token = StringTools.generateShortUrl(tokenPo.getId(), tokenSecret, BizConstant.TOKEN_MIN_LENGTH);
        // 本地保存token
        tokenRepository.updateCodeById(token, tokenPo.getId());
        // 删除其他token
        tokenRepository.deleteByMobileAndId(principal.getMobile(), tokenPo.getId(), tokenPo.getType());
        return token;
    }

    /**
     * 校验登录主体的身份
     *
     * @param principalBo
     * @throws AuthenticationException
     */
    private void checkExistAndType(ApiPrincipalBo principalBo) throws AuthenticationException {
        if (BizConstant.WECHAT_APP_MEMBER_CLIENT.equals(principalBo.getType())) {
            // 客户端
            EpMemberPo memberPo = memberRepository.getByMobile(principalBo.getMobile());
            // 新增会员信息
            if (memberPo == null) {
                EpMemberPo insertPo = new EpMemberPo();
                insertPo.setMobile(principalBo.getMobile());
                insertPo.setStatus(EpMemberStatus.normal);
                memberRepository.insert(insertPo);
                return;
            }
            if (memberPo.getDelFlag() || EpMemberStatus.cancel.equals(memberPo.getStatus())) {
                throw new UsernameNotFoundException("会员已被注销");
            } else if (EpMemberStatus.freeze.equals(memberPo.getStatus())) {
                throw new LockedException("会员已被锁定");
            }
        } else if (BizConstant.WECHAT_APP_ORGAN_CLIENT.equals(principalBo.getType())) {
            // 机构端
            Optional<EpOrganAccountPo> existOrganAccount = organAccountRepository.getByOgnIdAndReferMobile(principalBo.getOgnId(), principalBo.getMobile());
            if (!existOrganAccount.isPresent()) {
                throw new UsernameNotFoundException("机构账户不存在");
            }
            EpOrganAccountPo organAccountPo = existOrganAccount.get();
            if (organAccountPo.getDelFlag() || EpOrganAccountStatus.cancel.equals(organAccountPo.getStatus())) {
                throw new UsernameNotFoundException("机构账户已被注销");
            } else if (EpOrganAccountStatus.freeze.equals(organAccountPo.getStatus())) {
                throw new LockedException("机构账户已被锁定");
            }
        } else {
            throw new UsernameNotFoundException("登录类型不合法");
        }
    }

}
