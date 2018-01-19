package com.ep.backend.config;


import com.ep.backend.filter.BackendSecurityTokenAuthFilter;
import com.ep.backend.security.BackendSecurityAuthProvider;
import com.ep.domain.component.SecurityAuthEntryPointForJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Description: 鉴权设置（Spring Security Config）
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BackendSecurityAuthProvider securityAuthProvider;

    @Autowired
    private BackendSecurityTokenAuthFilter securityTokenAuthFilter;

    @Bean
    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return new SecurityAuthEntryPointForJson();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) {
        //自定义AuthenticationProvider
        authenticationManagerBuilder.authenticationProvider(securityAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 不需要csrf
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
                .authorizeRequests()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 403设置
        httpSecurity.exceptionHandling().authenticationEntryPoint(getAuthenticationEntryPoint());
        // 添加鉴权filter
        httpSecurity.addFilterBefore(securityTokenAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/security/**");
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/images/**",
                "/css/**",
                "/fonts/**",
                "/img/**",
                "/js/**",
                "/plugins/**",
                "/favicon.ico");
    }

}
