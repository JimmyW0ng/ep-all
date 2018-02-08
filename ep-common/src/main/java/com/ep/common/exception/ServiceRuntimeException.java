package com.ep.common.exception;

/**
 * @Description: Ep运行时异常
 * @Author: J.W
 * @Date: 上午9:43 2018/2/8
 */
public class ServiceRuntimeException extends RuntimeException {

    private ServiceRuntimeException() {
    }

    public ServiceRuntimeException(String errorMsg) {
        super(errorMsg);
    }

}
