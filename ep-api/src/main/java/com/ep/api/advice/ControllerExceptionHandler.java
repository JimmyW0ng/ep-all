package com.ep.api.advice;

import com.ep.domain.constant.MessageCode;
import com.ep.domain.pojo.ResultDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return ResultDo.build(MessageCode.ERROR_SYSTEM).setErrorDescription(ex.getMessage());
    }

}
