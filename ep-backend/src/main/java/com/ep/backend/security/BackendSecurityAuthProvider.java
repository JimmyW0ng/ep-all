package com.ep.backend.security;

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


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = authentication.getPrincipal().toString();
        String credentials = authentication.getCredentials().toString();
        String captchaCode = ((BackendAuthenticationDetails) authentication.getDetails()).getCaptchaCode();
        securityAuthComponent.checkLogin(principal, credentials, captchaCode);

        Collection<GrantedAuthority> authorities = securityAuthComponent.loadCurrentUserGrantedAuthorities(principal);
        //授权
        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}
