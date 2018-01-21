package com.ep.backend.security;

import com.ep.common.tool.JsonTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: RESTful Spring Security鉴权失败返回封装
 * @Author: J.W
 * @Date: 下午8:29 2018/1/6
 */
@Data
@Slf4j
public class BackendSecurityAuthEntryPoint implements AuthenticationEntryPoint {

    private String loginUrl;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.POST.name())) {
            ResultDo resultDo = ResultDo.build(MessageCode.ERROR_ACCESS_NEED_AUTH).setErrorDescription(e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.println(JsonTools.encode(resultDo));
        } else {
            response.sendRedirect(loginUrl);
        }
    }
}
