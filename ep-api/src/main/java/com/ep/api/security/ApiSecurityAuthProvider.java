package com.ep.api.security;

import com.ep.domain.pojo.bo.ApiCredentialBo;
import com.ep.domain.pojo.bo.ApiPrincipalBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 *
 * @author J.W
 * @date 2017/10/26 0026
 */
@Slf4j
@Component
public class ApiSecurityAuthProvider implements AuthenticationProvider {

    @Autowired
    private ApiSecurityAuthComponent securityAuthComponent;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        ApiPrincipalBo principalBo = (ApiPrincipalBo) token.getPrincipal();
        ApiCredentialBo credentialsBo = (ApiCredentialBo) token.getCredentials();
        securityAuthComponent.checkLogin(principalBo, credentialsBo);
        Collection<GrantedAuthority> authorities = securityAuthComponent.getPermissionByRoleCode(principalBo.getRole());
        //授权
        return new UsernamePasswordAuthenticationToken(principalBo, null, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}
