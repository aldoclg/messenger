package com.daitangroup.messenger.repository;

import com.daitangroup.messenger.domain.MessageInfo;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface MessageRepository {

     void createMessage(MessageInfo messageInfo);

     void deleteMessage(String uniqueId);

     void updateMessage(String UniqueId, String content);

     List<MessageInfo> findMessageByChatId(String chatId, long startDate, long endDate) throws IOException;

     List<MessageInfo> findMessage(long startDate, long endDate) throws IOException;

     List<MessageInfo> findMessageByUserId(String userId);

     List<MessageInfo> findMessageByChatId(String chatId, int init, int end);
}
