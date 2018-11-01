package com.daitangroup.messenger.repository;

import com.daitangroup.messenger.domain.ChatInfo;

import java.util.List;

public interface ChatRepository {

     void createChat(String chatName, String... userId);

     void createChat(String chatName, String userId);

     void insertUserToChat(String chatId, String... userId);

     void updateChat(String chatId, String chatName);

     void removeUserByChatIdAndUserId(String chatId, String userId);

     void removeUser(String uniqueId);

     void removeChat(String chatId);

     List<ChatInfo> findChat(String chatId);

     List<ChatInfo> findChatByUserId(String userId);
}
