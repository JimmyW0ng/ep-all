package com.ep.backend.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 后台AuthenticationDetailsSource
 * @Author: J.W
 * @Date: 下午7:55 2018/1/20
 */
public class BackendAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new BackendAuthenticationDetails(context);
    }
}
