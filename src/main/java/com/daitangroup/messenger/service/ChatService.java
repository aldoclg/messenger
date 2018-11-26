package com.daitangroup.messenger.service;

import com.daitangroup.messenger.domain.ChatInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {

    void createChatOneToOne(List<ChatInfo> chats);

    List<ChatInfo> findChat(String chatId);

    List<ChatInfo> findChatByUserId(String userId);
}
