package com.ep.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by fcc on 2018/1/10.
 */
@ComponentScan(basePackages = {"com.ep.backend"})
//@EnableScheduling
//@EnableAsync
@SpringBootApplication
public class BackendMain {
    public static void main(String[] args) {
        SpringApplication.run(BackendMain.class, args);
    }

}
