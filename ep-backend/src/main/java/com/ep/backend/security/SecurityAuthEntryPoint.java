package com.ep.backend.security;

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
 * @Description:RESTful鉴权失败返回封装
 * @Author: J.W
 * @Date: 下午8:29 2018/1/6
 */
@Slf4j
public class SecurityAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResultDo resultDo = ResultDo.build(MessageCode.ERROR_ACCESS_DENIED).setErrorDescription(e.getMessage());
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.println(JsonTools.encode(resultDo));
    }
}
