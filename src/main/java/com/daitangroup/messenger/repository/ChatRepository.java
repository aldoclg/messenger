package com.daitangroup.messenger.repository;

import com.daitangroup.messenger.domain.ChatInfo;

import java.util.List;

public interface ChatRepository {

    public void createChat(String chatName, String... userId);

    public void createChat(String chatName, String userId);

    public void insertUserToChat(String chatId, String... userId);

    public void updateChat(String chatId, String chatName);

    public void removeUserByChatIdAndUserId(String chatId, String userId);

    public void removeUser(String uniqueId);

    public void removeChat(String chatId);

    public List<ChatInfo> findChat(String chatId);

    public List<ChatInfo> findChatByUserId(String userId);
}
