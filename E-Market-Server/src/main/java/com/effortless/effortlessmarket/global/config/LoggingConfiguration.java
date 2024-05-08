package com.effortless.effortlessmarket.global.config;


import com.effortless.effortlessmarket.global.annotaion.EnableLogging;
import com.effortless.effortlessmarket.global.interceptor.LoggingHandlerInterceptor;
import com.effortless.effortlessmarket.global.interceptor.MdcHandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnBean(annotation = EnableLogging.class)
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingConfiguration implements WebMvcConfigurer {


    @Autowired
    private final ObjectMapper objectMapper;

    public LoggingConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);

        registry.addInterceptor(new LoggingHandlerInterceptor(objectMapper)).order(1);

    }
}