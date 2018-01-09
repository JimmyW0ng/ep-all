package com.ep.common.tool;

import com.ep.common.component.SpringComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public abstract class WebTools {


    private WebTools() {
    }

    public static HttpServletRequest getCurrentRequest() throws IllegalStateException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前线程中不存在 Request 上下文");
        }
        return attrs.getRequest();
    }

    public static void writeJsonResponse(HttpServletResponse response, Object oAuthResponse) {
        try {
            // CORS setting
            String encode = SpringComponent.getBean(ObjectMapper.class).writeValueAsString(oAuthResponse);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");    //json
            response.setStatus(HttpServletResponse.SC_OK);
            final PrintWriter out = response.getWriter();
            out.print(encode);
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Write OAuthResponse error", e);
        }
    }

}
