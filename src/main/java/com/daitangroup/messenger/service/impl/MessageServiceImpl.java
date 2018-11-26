package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.repository.MessageRepository;
import com.daitangroup.messenger.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class MessageServiceImpl implements MessageService {

    private Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    private MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMessage(MessageInfo messageInfo) {
        LOGGER.info("Called createMessage {}", messageInfo);
        String messageId = UUID.randomUUID().toString();
        messageInfo.setMessageId(messageId);
        messageRepository.createMessage(messageInfo);
    }

    @Override
    public List<MessageInfo> findMessageByChatId(String chatId, int init, int end) {
        LOGGER.info("Called findLastMessageByChatId {}", chatId);
        return messageRepository.findMessageByChatId(chatId, init, end);
    }
}
