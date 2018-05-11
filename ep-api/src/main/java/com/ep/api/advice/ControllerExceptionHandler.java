package com.ep.api.advice;

import com.ep.common.tool.WebTools;
import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: api控制类异常封装
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResultDo returnExceptionWithUrl(Exception ex) {
        log.error("接口访问异常!", ex);
        if (ex.getClass().equals(MissingServletRequestParameterException.class)
                || ex.getClass().equals(MethodArgumentTypeMismatchException.class)) {
            // 接口入参缺失或者格式不正确
            HttpServletRequest request = WebTools.getCurrentRequest();
            log.error("接口访问地址：url={}", request.getRequestURI());
            return ResultDo.build(MessageCode.ERROR_SYSTEM_PARAM_FORMAT);
        } else if (ex.getClass().equals(HttpRequestMethodNotSupportedException.class)) {
            HttpServletRequest request = WebTools.getCurrentRequest();
            log.error("接口访问地址：url={}", request.getRequestURI());
        } else if (ex instanceof AuthenticationException) {
            // 接口入参缺失或者格式不正确
            return ResultDo.build(MessageCode.ERROR_SYSTEM).setErrorDescription(ex.getMessage());
        }
        return ResultDo.build(MessageCode.ERROR_SYSTEM);
    }

}
