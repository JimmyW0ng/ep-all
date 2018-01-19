package com.ep.backend.security;

import com.ep.domain.component.SecurityAuth;
import com.ep.domain.pojo.bo.BackendCredentialBo;
import com.ep.domain.pojo.bo.BackendPrincipalBo;
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
 * @author J.W
 * @date 2017/10/26 0026
 */
@Slf4j
@Component
public class BackendSecurityAuthProvider implements AuthenticationProvider {

    @Autowired
    private BackendSecurityAuthComponent securityAuthComponent;

    @Autowired
    private SecurityAuth securityAuth;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        BackendPrincipalBo principalBo = (BackendPrincipalBo) token.getPrincipal();
        BackendCredentialBo credentialsBo = (BackendCredentialBo) token.getCredentials();
        securityAuthComponent.checkLogin(principalBo, credentialsBo);
        Collection<GrantedAuthority> authorities = securityAuth.loadCurrentUserGrantedAuthorities(principalBo.getRole());
        //授权
        return new UsernamePasswordAuthenticationToken(principalBo, null, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}
