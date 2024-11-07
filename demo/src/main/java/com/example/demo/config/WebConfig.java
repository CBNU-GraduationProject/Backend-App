package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://127.0.0.1:5500", "http://172.30.64.236:5000","https://heron-good-curiously.ngrok-free.app") // 필요한 출처 추가
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH")
                .allowCredentials(true);
    }
}
