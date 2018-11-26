package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.ChatInfo;
import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.repository.ChatRepository;
import com.daitangroup.messenger.service.ChatService;
import com.daitangroup.messenger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChatServiceImpl implements ChatService {

    private Logger LOGGER = LoggerFactory.getLogger(ChatServiceImpl.class);

    ChatRepository chatRepository;

    UserService userService;

    public ChatServiceImpl(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    @Override
    public void createChatOneToOne(List<ChatInfo> chatInfos) {
        LOGGER.info("Called createdChatOneToOne method", chatInfos);
        for(ChatInfo chat: chatInfos) {
            Optional<User> optionalUser = userService.find(chat.getUserId());
            LOGGER.debug("User {} exists : {}", chat.getUserId(), optionalUser.isPresent());
            if (!optionalUser.isPresent()) {
                throw new IllegalArgumentException("User does not exists...");
            }
        }
        String chatId = UUID.randomUUID().toString();
        for(ChatInfo chat: chatInfos) {
            LOGGER.debug("Creating chat {}", chat);
            chat.setChatId(chatId);
            chatRepository.createChat(chat);
        }
    }

    @Override
    public List<ChatInfo> findChat(String chatId) {
        LOGGER.info("Called findChat method {}", chatId);
        return chatRepository.findChat(chatId);
    }

    @Override
    public List<ChatInfo> findChatByUserId(String userId) {
        LOGGER.info("Called findChatByUserId method {}", userId);
        return chatRepository.findChatByUserId(userId);
    }
}
