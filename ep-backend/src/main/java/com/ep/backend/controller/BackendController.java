package com.ep.backend.controller;

import com.ep.backend.security.SecurityAuthComponent;
import com.ep.domain.constant.BizConstant;
import com.ep.domain.pojo.ResultDo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    private SecurityAuthComponent securityAuthComponent;

    @Value("${token.header.name}")
    private String tokenHeaderName;

    @ApiOperation(value = "首页")
    @GetMapping("/index")
    public String index() {
        System.out.println("ccccccccc");
        return "index";
    }

    /**
     * 获取登录短信验证码
     *
     * @return
     */
    @ApiOperation(value = "后台登录页")
    @GetMapping("/login")
    public String getCaptcha() {
        return "login";
    }


    /**
     * 后台登录
     *
     * @param mobile
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    @PostMapping("/token")
    public String login(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam("mobile") Long mobile,
                        @RequestParam("password") String password,
                        @RequestParam("clientId") String clientId,
                        @RequestParam("clientSecret") String clientSecret,
                        @RequestParam("captchaCode") String captchaCode

    ) {
        HttpSession session = request.getSession();
        Object sessionCaptcha = session.getAttribute(BizConstant.CAPTCHA_SESSION_KEY);
        if (sessionCaptcha != null) {
            if (!sessionCaptcha.toString().toLowerCase().equals(captchaCode.toLowerCase())) {
                throw new BadCredentialsException("验证码错误");
            }
        } else {
            throw new BadCredentialsException("验证码无效，请重新获取");
        }

        ResultDo<String> resultDo = securityAuthComponent.loginFromBackend(null, mobile.toString(), password, clientId, clientSecret, captchaCode);
        session.setAttribute(tokenHeaderName, resultDo.getResult());
        return "/index";
    }


    /**
     * 生成验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/captcha/newCode")
    public void newCode(HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession();
//        long time = System.currentTimeMillis();
//        response.setContentType("image/png");
//        response.setHeader("Cache-Control", "no-cache, no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Last-Modified", time);
//        response.setDateHeader("Date", time);
//        response.setDateHeader("Expires", time);
//        ServletOutputStream outputStream = null;
//        try {
//            outputStream = response.getOutputStream();
//            Captcha captcha = new GifCaptcha(100, 40, 4);//   gif格式动画验证码
//            captcha.out(outputStream);
//            session.setAttribute(BizConstant.CAPTCHA_SESSION_KEY, captcha.text());
//            log.info(session.getId() + "生成验证码[{}]", captcha.text());
//        } catch (Exception e) {
//            log.error("生成验证码失败！", e);
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//
//                }
//            }
//        }
    }

}
