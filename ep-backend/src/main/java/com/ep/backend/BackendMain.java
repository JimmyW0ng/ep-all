package com.ep.backend;

import com.ep.backend.config.SecurityConfig;
import com.ep.backend.config.ThymeleafConfig;
import com.ep.common.config.JsonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by fcc on 2018/1/10.
 */
@Import({
        // 持久层配置
        //JooqConfig.class,
        // 资源文件配置
//        MessageSourceAutoConfiguration.class,
//        // http相关配置
//        RestTempldateConfig.class,
//        // MVC、拦截器相关配置
        // WebConfig.class,
        // 鉴权
        SecurityConfig.class,
        // json配置
        JsonConfig.class,
//        // swagger
//        Swagger2Config.class
        ThymeleafConfig.class
})
@ComponentScan(basePackages = {"com.ep"})
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class BackendMain {
    public static void main(String[] args) {
        SpringApplication.run(BackendMain.class, args);
    }

}
