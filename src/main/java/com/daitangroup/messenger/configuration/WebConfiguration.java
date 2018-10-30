package com.daitangroup.messenger.configuration;

import com.daitangroup.messenger.rest.controller.ChatManagerController;
import com.daitangroup.messenger.rest.controller.impl.ChatManagerControllerImpl;
import com.daitangroup.messenger.service.UserService;
import com.daitangroup.messenger.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    public void addResourceHandlers(ResourceHandlerRegistry registry) {}

    @Bean
    public ChatManagerController chatManagerController() {
        return new ChatManagerControllerImpl();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    public void addCorsMapping(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .allowedMethods("*")
                .maxAge(3600);
    }
}
