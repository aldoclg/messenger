package com.daitangroup.messenger.websocket.impl;

import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.service.ChatBrokerService;
import com.daitangroup.messenger.websocket.ChatBrokerController;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;

public class ChatBrokerControllerImpl implements ChatBrokerController {

    private ChatBrokerService chatBrokerService;

    public ChatBrokerControllerImpl(ChatBrokerService chatBrokerService) {
        this.chatBrokerService = chatBrokerService;
    }

    @Override
    public void sendMessage(@Payload MessageInfo messageInfo) {
        System.out.println(messageInfo);
        chatBrokerService.sendMessage(messageInfo);
    }

    @Override
    public void subscribing(@DestinationVariable String userId) {
        System.out.println("Subscribed " + userId);
    }
}
