package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5500", "http://localhost:3000",
                        "http://172.30.64.236:5000", "https://heron-good-curiously.ngrok-free.app",
                        "https://kickx2-app-g3dkagdbhuaqgbh3.koreacentral-01.azurewebsites.net") // Azure 도메인 추가
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowCredentials(true);

    }
}
