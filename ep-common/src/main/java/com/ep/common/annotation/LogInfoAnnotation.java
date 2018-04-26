package com.ep.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: CC.F
 * @Date: 21:33 2018/4/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface LogInfoAnnotation {
    //模块名
    String moduleName();

    //操作内容
    String desc();
}
