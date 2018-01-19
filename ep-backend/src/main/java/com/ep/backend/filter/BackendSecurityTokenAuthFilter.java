package com.ep.backend.filter;

import com.ep.backend.security.BackendSecurityAuthComponent;
import com.ep.common.tool.StringTools;
import com.ep.domain.component.SecurityAuth;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.bo.BackendPrincipalBo;
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
 * @Description: backend鉴权过滤器
 * @Author: J.W
 * @Date: 下午4:41 2018/1/9
 */
@Slf4j
@Component
public class BackendSecurityTokenAuthFilter extends OncePerRequestFilter {

    @Value("${token.header.name}")
    private String tokenHeaderName;

    @Autowired
    private BackendSecurityAuthComponent securityAuthComponent;

    @Autowired
    private SecurityAuth securityAuth;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Object authHeader = request.getSession().getAttribute(this.tokenHeaderName);
        // 没有token
        if (authHeader == null) {
            chain.doFilter(request, response);
            return;
        }
        String token = authHeader.toString();
        if (StringTools.isBlank(token)) {
            chain.doFilter(request, response);
            return;
        }
        log.info("鉴权:token={},完毕！", token);
        // 解析token
        ResultDo<BackendPrincipalBo> resultDo = securityAuthComponent.getTokenInfo(token);
        if (resultDo.isError()) {
            log.error("验证token不通过, error={}, desc={}", resultDo.getError(), resultDo.getErrorDescription());
            chain.doFilter(request, response);
            return;
        }
        BackendPrincipalBo principalBo = resultDo.getResult();
        // 加载当前用户信息
        securityAuthComponent.loadCurrentUserInfo(principalBo);
        // 加载当前用户权限
        String role = securityAuthComponent.getCurrentUserRole().getRole();
        Collection<GrantedAuthority> authorities = securityAuth.loadCurrentUserGrantedAuthorities(role);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalBo, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
        log.info("鉴权:token={}, username={},完毕！", authHeader, principalBo.getUserName());
    }

}
