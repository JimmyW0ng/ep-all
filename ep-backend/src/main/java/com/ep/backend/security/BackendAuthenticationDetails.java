package com.ep.backend.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 后台AuthenticationDetails
 * @Author: J.W
 * @Date: 下午7:51 2018/1/20
 */
public class BackendAuthenticationDetails extends WebAuthenticationDetails {

    private static String CAPTCHA_CODE = "captchaCode";

    private final String captchaCode;

    public BackendAuthenticationDetails(HttpServletRequest request) {
        super(request);
        captchaCode = request.getParameter(CAPTCHA_CODE);
    }

    public String getCaptchaCode() {
        return this.captchaCode;
    }
}
