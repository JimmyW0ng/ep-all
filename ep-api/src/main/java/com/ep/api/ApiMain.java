package com.ep.api;

import com.ep.api.config.SecurityConfig;
import com.ep.common.config.JsonConfig;
import com.ep.common.config.RestTempldateConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description: api启动类
 * @Author: J.W
 * @Date: 下午1:08 2018/1/6
 */
@Import({
        // 持久层配置
        //JooqConfig.class,
        // 资源文件配置
        MessageSourceAutoConfiguration.class,
        // http相关配置
        RestTempldateConfig.class,
        // MVC、拦截器相关配置
        // WebConfig.class,
        // 鉴权
        SecurityConfig.class,
        // json配置
        JsonConfig.class
        // swagger
        // Swagger2Config.class
})
@ComponentScan(basePackages = {"com.ep"})
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class ApiMain {

    public static void main(String[] args) {
        SpringApplication.run(ApiMain.class, args);
    }

}
