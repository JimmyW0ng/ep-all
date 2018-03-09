package com.ep.common.component;

import com.ep.common.tool.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringComponent implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringComponent.applicationContext == null) {
            SpringComponent.applicationContext = applicationContext;
        }
    }

    /**
     * 通过name获取 Bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 获取messageSource
     *
     * @param code
     * @return
     */
    public static String messageSource(String code) {
        String temp;
        try {
            temp = SpringComponent.getBean(MessageSource.class).getMessage(code, null, null);
        } catch (Exception e) {
            log.error("找不到对应的 messageSource ", e);
            temp = code;
        }
        return temp;
    }

    /**
     * 获取messageSource的消息
     *
     * @param code
     * @return
     */
    public static String messageSource(String code, Object[] objects) {
        String temp;
        try {
            temp = SpringComponent.getBean(MessageSource.class).getMessage(code, objects, null);
        } catch (Exception e) {
            log.error("找不到对应的 messageSource ", e);
            temp = code;
        }
        return temp;
    }

    /**
     * 判断是否是生产环境
     *
     * @return
     */
    public static Boolean isProduct() {
        String[] activeProfiles = getBean(Environment.class).getActiveProfiles();
        return StringTools.equals(activeProfiles[0], "prod");
    }
}

