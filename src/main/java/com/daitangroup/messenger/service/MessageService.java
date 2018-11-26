package com.daitangroup.messenger.service;

import com.daitangroup.messenger.domain.MessageInfo;

import java.util.List;

public interface MessageService {

    void createMessage(MessageInfo messageInfo);

    List<MessageInfo> findMessageByChatId(String chatId, int init, int end);
}
