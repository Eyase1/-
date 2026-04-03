package com.ttt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imagePath = "file:C:/Users/Eyase/Desktop/spring框架实训资料/img/"; // 注意结尾斜杠
        registry.addResourceHandler("/image/**")
                .addResourceLocations(imagePath);
    }
}
