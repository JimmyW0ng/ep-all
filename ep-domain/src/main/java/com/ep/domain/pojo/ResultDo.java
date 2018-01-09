package com.ep.domain.pojo;

import com.ep.common.component.SpringComponent;
import com.ep.common.tool.StringTools;
import org.springframework.context.MessageSource;

/**
 * @Description: 返回结果封装类
 * @Author: J.W
 * @Date: 下午8:26 2018/1/6
 */
public class ResultDo<T> extends AbstractBasePojo {

    private boolean isSuccess = true;
    /**
     * 错误码
     */
    private String error;
    /**
     * 错误描述信息
     */
    private String errorDescription;

    /**
     * 返回结果对象
     */
    private T result;

    private ResultDo() {

    }

    public static <T> ResultDo<T> build() {
        return new ResultDo<> ();
    }

    public static <T> ResultDo<T> build(String messCodeConstant) {
        ResultDo<T> resultDo = ResultDo.build();
        resultDo.setError(messCodeConstant).setErrorDescription(messageSource(messCodeConstant)).setSuccess(false);
        return resultDo;
    }

    /**
     * 获取messageSource的消息
     *
     * @param code
     * @return
     */
    private static String messageSource(String code) {
        return SpringComponent.getBean(MessageSource.class).getMessage(code, null, code, null);
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public ResultDo<T> setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
        if (StringTools.isNotEmpty(this.errorDescription)) {
            this.setSuccess(false);
        }
        return this;
    }

    public String getError() {
        return error;
    }

    public T getResult() {
        return result;
    }

    public ResultDo<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public ResultDo<T> setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public boolean isError() {
        return !isSuccess;
    }

    public ResultDo<T> setError(String error) {
        this.error = error;
        this.errorDescription = messageSource(error);
        this.isSuccess = false;
        return this;
    }

}