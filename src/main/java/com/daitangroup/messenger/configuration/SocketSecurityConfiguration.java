package com.daitangroup.messenger.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

   @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry registry) {
        registry.simpDestMatchers("/messenger/**").permitAll()//.hasAnyRole("ADMIN", "AUDIT", "USER"
                .simpSubscribeDestMatchers("/queue/**").permitAll()//.hasAnyRole("ADMIN", "AUDIT", "USER")
                .simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT, SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).permitAll()
                .anyMessage().permitAll();//.hasAnyRole("ADMIN", "AUDIT", "USER");
    }

   @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
