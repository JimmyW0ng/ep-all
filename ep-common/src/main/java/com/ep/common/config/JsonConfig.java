package com.ep.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;

/**
 * @Author J.W on 2017/11/8 0008.
 */
public class JsonConfig {

    @Bean
    @Primary
    public ObjectMapper createJsonDeson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        AnnotationIntrospector intr = new JacksonAnnotationIntrospector();
        mapper.setAnnotationIntrospector(intr);
        return mapper;
    }

}
