package com.ep.api.security;

import com.ep.common.tool.JsonTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ApiSecurityAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ResultDo resultDo = ResultDo.build(MessageCode.ERROR_ACCESS_NEED_AUTH);
        resultDo.setErrorDescription(e.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(JsonTools.encode(resultDo));
    }
}
