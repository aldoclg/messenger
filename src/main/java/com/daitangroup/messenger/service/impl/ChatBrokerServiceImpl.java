package com.daitangroup.messenger.service.impl;

import static com.daitangroup.messenger.constants.ConstantsUtils.SUBSCRIBE_TO_RECEIVE_MESSAGE_URN;

import com.daitangroup.messenger.domain.ChatInfo;
import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.service.ChatBrokerService;
import com.daitangroup.messenger.service.ChatService;
import com.daitangroup.messenger.service.MessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class ChatBrokerServiceImpl implements ChatBrokerService {

    private SimpMessagingTemplate simpMessagingTemplate;

    private ChatService chatService;

    private MessageService messageService;

    public ChatBrokerServiceImpl(SimpMessagingTemplate simpMessagingTemplate, ChatService chatService, MessageService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
        this.messageService = messageService;
    }

    @Override
    public void sendMessage(MessageInfo messageInfo) {

        String chatId = messageInfo.getChatId();
        List<ChatInfo> chats = chatService.findChat(chatId);

        List<ChatInfo> toSendChatInfo = chats.stream()
                .filter(chatInfo -> !chatInfo.getUserId().equals(messageInfo.getFromUserId()))
                .collect(Collectors.toList());

        for (ChatInfo c: toSendChatInfo) {
            simpMessagingTemplate.convertAndSend(SUBSCRIBE_TO_RECEIVE_MESSAGE_URN + c.getUserId(), messageInfo);
        }

    }
}
