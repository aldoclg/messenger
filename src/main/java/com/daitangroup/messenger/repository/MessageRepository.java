package com.daitangroup.messenger.repository;

import com.daitangroup.messenger.domain.MessageInfo;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface MessageRepository {

    public void createMessage(MessageInfo messageInfo);

    public void deleteMessage(String uniqueId);

    public void updateMessage(String UniqueId, String content);

    public List<MessageInfo> findMessageByChatId(String chatId, long startDate, long endDate) throws IOException;

    public List<MessageInfo> findMessage(long startDate, long endDate) throws IOException;

    public List<MessageInfo> findMessageByUserId(String userId);

}
