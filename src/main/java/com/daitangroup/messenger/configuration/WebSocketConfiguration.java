package com.daitangroup.messenger.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    private Logger LOGGER = LoggerFactory.getLogger(WebSocketConfiguration.class);

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/socket")
                .setAllowedOrigins("*")
                .withSockJS();
        LOGGER.info("Configured StompEndpointRegistry ", stompEndpointRegistry);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/messenger")
                .enableSimpleBroker("/queue");
        LOGGER.info("Configured Message Broker ", registry);
    }

}
