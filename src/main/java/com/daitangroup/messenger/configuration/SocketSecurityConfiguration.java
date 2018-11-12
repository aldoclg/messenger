package com.daitangroup.messenger.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    private Logger LOGGER = LoggerFactory.getLogger(SocketSecurityConfiguration.class);

   @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry registry) {
        registry.simpDestMatchers("/messenger/**").permitAll()//.hasAnyRole("ADMIN", "AUDIT", "USER"
                .simpSubscribeDestMatchers("/queue/**").permitAll()//.hasAnyRole("ADMIN", "AUDIT", "USER")
                .simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT, SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).permitAll()
                .anyMessage().permitAll();//.hasAnyRole("ADMIN", "AUDIT", "USER");
       LOGGER.info("Configured MessageSecuritMetadataSourceRegistry ", registry);
    }

   @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
