package com.ep.api.security;

import com.ep.common.tool.CryptTools;
import com.ep.common.tool.DateTools;
import com.ep.common.tool.JsonTools;
import com.ep.common.tool.StringTools;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.ApiCredentialBo;
import com.ep.domain.pojo.bo.ApiPrincipalBo;
import com.ep.domain.pojo.po.EpMemberPo;
import com.ep.domain.pojo.po.EpSystemClientPo;
import com.ep.domain.repository.MemberRepository;
import com.ep.domain.repository.SystemClientRepository;
import com.ep.domain.repository.domain.enums.EpMemberStatus;
import com.ep.domain.repository.domain.enums.EpSystemClientLoginSource;
import com.ep.domain.service.MessageCaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.util.Date;

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
    private MessageCaptchaService messageCaptchaService;

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
    public ResultDo<String> loginFromApi(String userName,
                                         String captchaCode,
                                         String captchaContent,
                                         String clientId,
                                         String clientSecret) {
        ResultDo<String> resultDo = ResultDo.build();
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
            // 生成token
            String accessToken = this.buildAccessToken((ApiPrincipalBo) authToken.getPrincipal());
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
    public ResultDo<ApiPrincipalBo> getTokenInfo(String token) {
        ResultDo<ApiPrincipalBo> resultDo = ResultDo.build();
        if (StringTools.isBlank(token)) {
            log.error("token解析失败，token为空:{}，", token);
            return resultDo.setError(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        }
        try {
            String tokenJsonStr = CryptTools.aesDecrypt(token, tokenSecret);
            ApiPrincipalBo principalBo = JsonTools.decode(tokenJsonStr, ApiPrincipalBo.class);
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
        } catch (GeneralSecurityException e) {
            log.error("token解密失败，token={}", token, e);
            return resultDo.setError(MessageCode.ERROR_DECODE);
        } catch (Exception e) {
            log.error("token解析失败，token={}", token, e);
            return resultDo.setError(MessageCode.ERROR_SYSTEM);
        }
    }

    /**
     * 加载用户信息
     *
     * @param request
     * @param principalBo
     */
    public void loadCurrentUserInfo(HttpServletRequest request, ApiPrincipalBo principalBo) {
        EpMemberPo mbrInfoPo = memberRepository.getByMobile(Long.parseLong(principalBo.getUserName()));
        request.setAttribute(BizConstant.CURENT_USER, mbrInfoPo);
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
            throw new UsernameNotFoundException("客户端凭证格式错误");
        }
        if (!pwdEncode.equals(sysClientPo.getClientSecret())) {
            throw new UsernameNotFoundException("客户端凭证无效");
        }
        // 校验用户信息
        Long mobile = Long.valueOf(principalBo.getUserName());
        // 校验验证码
        messageCaptchaService.checkAndHandleCaptcha(mobile, principalBo.getCaptchaCode(), credentialBo.getPassword());
        // 校验会员
        EpMemberPo mbrInfoPo = memberRepository.getByMobile(mobile);
        if (mbrInfoPo == null) {
            // 保存用户
            EpMemberPo insertPo = new EpMemberPo();
            insertPo.setMobile(mobile);
            insertPo.setStatus(EpMemberStatus.normal);
            memberRepository.insert(insertPo);
        } else if (mbrInfoPo.getStatus().equals(EpMemberStatus.cancel)) {
            throw new UsernameNotFoundException("账号已被注销");
        } else if (mbrInfoPo.getStatus().equals(EpMemberStatus.freeze)) {
            throw new LockedException("账号已被锁定");
        }
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
