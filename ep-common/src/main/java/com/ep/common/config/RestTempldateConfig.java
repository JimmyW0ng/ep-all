package com.ep.common.config;

import com.ep.common.HttpMsgConverter.WeChatHttpMsgConverter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 支持微信
 * Created by JW on 17/9/4.
 */
public class RestTempldateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        restTemplate.getMessageConverters().add(new WeChatHttpMsgConverter());
        return restTemplate;
    }

}
