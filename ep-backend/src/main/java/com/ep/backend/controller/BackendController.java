package com.ep.backend.controller;

import com.ep.backend.security.BackendSecurityAuthComponent;
import com.ep.common.captcha.Captcha;
import com.ep.common.captcha.GifCaptcha;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Description: backend开放控制类
 * @Author: J.W
 * @Date: 下午4:41 2018/1/9
 */
@Slf4j
@RequestMapping("security/backend")
@Controller
@Api(value = "api-security", description = "backend开放接口")
public class BackendController {

    @Autowired
    private BackendSecurityAuthComponent securityAuthComponent;

    @Value("${token.header.name}")
    private String tokenHeaderName;

    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index() {
        System.out.println("ccccccccc");
        return "index";
    }

    /**
     * 后台登录页面
     *
     * @return
     */
    @ApiOperation(value = "后台登录页面")
    @GetMapping("/login")
    public String getCaptcha() {
        return "login";
    }


    /**
     * 后台登录
     *
     * @param mobile
     * @param password
     * @return
     */
    @PostMapping("/token")
    @ResponseBody
    public ResultDo<String> login(HttpServletRequest request,
                        @RequestParam("mobile") Long mobile,
                        @RequestParam("password") String password,
                        @RequestParam("captchaCode") String captchaCode
    ) {
        HttpSession session = request.getSession();
        ResultDo<String> resultDo = ResultDo.build();
        try {
            resultDo = securityAuthComponent.loginFromBackend(mobile.toString(), password, captchaCode);

        } catch (AuthenticationServiceException e) {
            resultDo.setSuccess(false);
            resultDo.setError("error_captcha");
            resultDo.setErrorDescription(e.getMessage());
        } catch (BadCredentialsException e) {
            resultDo.setSuccess(false);
            resultDo.setError("error_password");
            resultDo.setErrorDescription(e.getMessage());
        } catch (UsernameNotFoundException e) {
            resultDo.setSuccess(false);
            resultDo.setError("error_loginname");
            resultDo.setErrorDescription(e.getMessage());
        }
        // session中加token
        session.setAttribute(tokenHeaderName, resultDo.getResult());
        return resultDo;
    }


    /**
     * 生成验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/captcha/newCode")
    public void newCode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        long time = System.currentTimeMillis();
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // gif格式动画验证码
            Captcha captcha = new GifCaptcha(100, 40, 4);
            captcha.out(outputStream);
            session.setAttribute(BizConstant.CAPTCHA_SESSION_KEY, captcha.text());
            log.info(session.getId() + "生成验证码[{}]", captcha.text());
        } catch (Exception e) {
            log.error("生成验证码失败！", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {

                }
            }
        }
    }

}
