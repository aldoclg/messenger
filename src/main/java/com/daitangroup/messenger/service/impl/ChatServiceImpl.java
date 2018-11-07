package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.ChatInfo;
import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.repository.ChatRepository;
import com.daitangroup.messenger.service.ChatService;
import com.daitangroup.messenger.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChatServiceImpl implements ChatService {

    ChatRepository chatRepository;

    UserService userService;

    public ChatServiceImpl(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Override
    public void createChatOneToOne(List<ChatInfo> chatInfos) {
        for(ChatInfo chat: chatInfos) {
            Optional<User> optionalUser = userService.find(chat.getUserId());
            if (!optionalUser.isPresent()) {
                throw new IllegalArgumentException("User does not exists...");
            }
        }
        String chatId = UUID.randomUUID().toString();
        for(ChatInfo chat: chatInfos) {
            chat.setChatId(chatId);
            chatRepository.createChat(chat);
        }
    }

    @Override
    public List<ChatInfo> findChat(String chatId) {
        return chatRepository.findChat(chatId);
    }
}
