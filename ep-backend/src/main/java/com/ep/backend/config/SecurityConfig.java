package com.ep.backend.config;


import com.ep.backend.security.BackendAuthenticationDetailsSource;
import com.ep.backend.security.BackendSecurityAuthEntryPoint;
import com.ep.backend.security.BackendSecurityAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;

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

    @Bean
    public BackendAuthenticationDetailsSource getBackendAuthenticationDetailsSource() {
        return new BackendAuthenticationDetailsSource();
    }

    @Bean
    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        BackendSecurityAuthEntryPoint entryPoint = new BackendSecurityAuthEntryPoint();
        entryPoint.setLoginUrl("/security/login");
        return entryPoint;
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
                // 除上面外的所有请求全部需要鉴权认证
                .authorizeRequests().antMatchers("/security/**").permitAll()
                .anyRequest().authenticated().and()
                .formLogin()
                .loginPage("/security/login")
                .loginProcessingUrl("/security/login")
                .failureUrl("/security/login?error")
                .defaultSuccessUrl("/auth/index")
                .authenticationDetailsSource(getBackendAuthenticationDetailsSource())
                .permitAll().and()
                .logout().permitAll();

        // 403设置
        httpSecurity.exceptionHandling().authenticationEntryPoint(getAuthenticationEntryPoint());
        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
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
