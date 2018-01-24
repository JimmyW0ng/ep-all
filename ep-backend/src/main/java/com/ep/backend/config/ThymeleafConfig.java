package com.ep.backend.config;

import com.ep.backend.dialect.MyDialect;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.springdata.SpringDataDialect;


public class ThymeleafConfig {
    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

    @Bean
    public MyDialect myDialect() {
        return new MyDialect();
    }
}
