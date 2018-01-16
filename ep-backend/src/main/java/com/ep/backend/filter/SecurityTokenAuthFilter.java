package com.ep.backend.filter;

import com.ep.backend.security.SecurityAuthComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.SecurityPrincipalBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by J.W on 2017/10/27 0027.
 */
@Slf4j
@Component
public class SecurityTokenAuthFilter extends OncePerRequestFilter {

    @Value("${token.header.name}")
    private String tokenHeaderName;

    @Value("${token.header.prefix}")
    private String tokenHeaderPrefix;

    @Autowired
    private SecurityAuthComponent securityAuthComponent;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = null;
        if (request.getSession().getAttribute(this.tokenHeaderName) != null) {
            authHeader = this.tokenHeaderPrefix + " " + request.getSession().getAttribute(this.tokenHeaderName).toString();
        }
        // 没有token
        if (StringTools.isBlank(authHeader)) {
            chain.doFilter(request, response);
            return;
        }
        log.info("鉴权:token={},完毕！", authHeader);
        if (!authHeader.startsWith(tokenHeaderPrefix)) {
            throw new ServletException("token格式错误！");
        }
        authHeader = authHeader.substring(tokenHeaderPrefix.length() + 1);
        // 解析token
        ResultDo<SecurityPrincipalBo> resultDo = securityAuthComponent.getTokenInfo(authHeader);
        if (resultDo.isError()) {
            throw new ServletException(resultDo.getErrorDescription());
        }
        SecurityPrincipalBo principalBo = resultDo.getResult();
        // 加载当前用户信息
        securityAuthComponent.loadCurrentUserInfo(request, principalBo);
        // 加载当前用户权限
        Collection<GrantedAuthority> authorities = securityAuthComponent.loadCurrentUserGrantedAuthorities(principalBo.getRole());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalBo, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
        log.info("鉴权:token={}, username={},完毕！", authHeader, principalBo.getUserName());
    }

}
