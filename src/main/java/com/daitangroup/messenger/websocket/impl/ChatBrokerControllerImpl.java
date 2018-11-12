package com.daitangroup.messenger.websocket.impl;

import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.service.ChatBrokerService;
import com.daitangroup.messenger.websocket.ChatBrokerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Payload;

public class ChatBrokerControllerImpl implements ChatBrokerController {

    private Logger LOGGER = LoggerFactory.getLogger(ChatBrokerControllerImpl.class);

    private ChatBrokerService chatBrokerService;

    public ChatBrokerControllerImpl(ChatBrokerService chatBrokerService) {
        this.chatBrokerService = chatBrokerService;
    }

    @Override
    public void sendMessage(@Payload MessageInfo messageInfo) {
        LOGGER.info("Called sendMessage method {}", messageInfo);
        chatBrokerService.sendMessage(messageInfo);
    }
}
