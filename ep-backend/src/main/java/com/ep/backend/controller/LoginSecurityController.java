package com.ep.backend.controller;

import com.ep.common.captcha.Captcha;
import com.ep.common.captcha.GifCaptcha;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import com.ep.domain.pojo.dto.CreateOrganCourseDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("security")
@Controller
@Api(value = "api-security", description = "backend开放接口")
public class LoginSecurityController extends BackendController {

    /**
     * 后台登录
     *
     * @return
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        Object authException = request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (authException != null) {
            AuthenticationException authenticationException = (AuthenticationException) authException;
            model.addAttribute("errorMsg", authenticationException.getMessage());
        } else {
            model.addAttribute("errorMsg", null);
        }
        return "login";
    }

    /**
     * 生成验证码
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/captcha/newCode")
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

    @PostMapping("/merchantCreate")
    @ResponseBody
    public ResultDo merchantCreate(CreateOrganCourseDto dto) {
        ResultDo resultDo = ResultDo.build();
        return resultDo;
    }
}
