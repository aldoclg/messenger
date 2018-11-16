package com.daitangroup.messenger.configuration;

import com.daitangroup.messenger.repository.ChatRepository;
import com.daitangroup.messenger.repository.MessageRepository;
import com.daitangroup.messenger.repository.UserRepository;
import com.daitangroup.messenger.repository.impl.ChatRepositoryImpl;
import com.daitangroup.messenger.repository.impl.MessageRepositoryImpl;
import com.daitangroup.messenger.rest.component.ChatResourceAssembler;
import com.daitangroup.messenger.rest.component.MessageResourceAssembler;
import com.daitangroup.messenger.rest.component.UserResourceAssembler;
import com.daitangroup.messenger.rest.controller.ChatManagerController;
import com.daitangroup.messenger.rest.controller.impl.ChatManagerControllerImpl;
import com.daitangroup.messenger.service.ChatBrokerService;
import com.daitangroup.messenger.service.ChatService;
import com.daitangroup.messenger.service.MessageService;
import com.daitangroup.messenger.service.UserService;
import com.daitangroup.messenger.service.impl.ChatBrokerServiceImpl;
import com.daitangroup.messenger.service.impl.ChatServiceImpl;
import com.daitangroup.messenger.service.impl.MessageServiceImpl;
import com.daitangroup.messenger.service.impl.UserServiceImpl;
import com.daitangroup.messenger.websocket.ChatBrokerController;
import com.daitangroup.messenger.websocket.impl.ChatBrokerControllerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private UserResourceAssembler userAssembler;

    private ChatResourceAssembler chatAssembler;

    private MessageResourceAssembler messageAssembler;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private MongoTemplate mongoTemplate;

    private SimpMessagingTemplate simpMessagingTemplate;

    private Logger LOGGER = LoggerFactory.getLogger(WebConfiguration.class);

    public WebConfiguration(UserResourceAssembler userAssembler,
                            ChatResourceAssembler chatAssembler,
                            MessageResourceAssembler messageAssembler,
                            UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            MongoTemplate mongoTemplate,
                            SimpMessagingTemplate simpMessagingTemplate) {
        this.userAssembler = userAssembler;
        this.chatAssembler = chatAssembler;
        this.messageAssembler = messageAssembler;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mongoTemplate = mongoTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {}

    @Bean
    public UserService userService() {
        LOGGER.debug("Created UserService.");
        return new UserServiceImpl(userRepository, mongoTemplate, passwordEncoder);
    }

    @Bean
    ChatService chatService()   {
        LOGGER.debug("Created ChatService");
        return new ChatServiceImpl(chatRepository(), userService());
    }

    @Bean
    ChatBrokerService chatBrokerService() {
        LOGGER.debug("Created ChatBrokerService.");
        return new ChatBrokerServiceImpl(simpMessagingTemplate, chatService(), messageService());
    }

    @Bean
    MessageService messageService() {
        LOGGER.debug("Created MessageService.");
        return new MessageServiceImpl(messageRepository());
    }

    @Bean
    ChatRepository chatRepository() {
        LOGGER.debug("Created ChatRepository.");
        return new ChatRepositoryImpl();
    }

    @Bean
    MessageRepository messageRepository() {
        LOGGER.debug("Created MessageRepository.");
        return new MessageRepositoryImpl();
    }

    @Bean
    public ChatManagerController chatManagerController() {
        LOGGER.debug("Created ChatManagerController.");
        return new ChatManagerControllerImpl(userService(),
                userAssembler,
                chatAssembler,
                messageAssembler,
                chatService(),
                messageService());
    }

    @Bean
    public ChatBrokerController chatBrokerController() {
        LOGGER.debug("Created ChatBrokerController.");
        return new ChatBrokerControllerImpl(chatBrokerService());
    }

    public void addCorsMapping(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .allowedMethods("*")
                .maxAge(3600);
        LOGGER.info("Configured CORS ", registry.toString());
    }
}
