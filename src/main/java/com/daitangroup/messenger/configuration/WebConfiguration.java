package com.daitangroup.messenger.configuration;

import com.daitangroup.messenger.repository.ChatRepository;
import com.daitangroup.messenger.repository.UserRepository;
import com.daitangroup.messenger.repository.impl.ChatRepositoryImpl;
import com.daitangroup.messenger.rest.component.UserResourceAssembler;
import com.daitangroup.messenger.rest.controller.ChatManagerController;
import com.daitangroup.messenger.rest.controller.impl.ChatManagerControllerImpl;
import com.daitangroup.messenger.service.ChatService;
import com.daitangroup.messenger.service.UserService;
import com.daitangroup.messenger.service.impl.ChatServiceImpl;
import com.daitangroup.messenger.service.impl.UserServiceImpl;
import org.junit.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private UserResourceAssembler assembler;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private MongoTemplate mongoTemplate;

    public WebConfiguration(UserResourceAssembler assembler, UserRepository userRepository, PasswordEncoder passwordEncoder, MongoTemplate mongoTemplate) {
        this.assembler = assembler;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mongoTemplate = mongoTemplate;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {}

    @Bean
    public UserService userService() {
        return new UserServiceImpl(userRepository, mongoTemplate, passwordEncoder);
    }

    @Bean
    ChatService chatService()   {
        return new ChatServiceImpl(chatRepository(), userService());
    }

    @Bean
    ChatRepository chatRepository() {
        return new ChatRepositoryImpl();
    }

    @Bean
    public ChatManagerController chatManagerController() {
        return new ChatManagerControllerImpl(userService(), chatService(), assembler);
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
