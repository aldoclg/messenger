package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.repository.MessageRepository;
import com.daitangroup.messenger.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageServiceImpl implements MessageService {

    private Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    private MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMessage(MessageInfo messageInfo) {
        LOGGER.info("Called createMessage {}", messageInfo);
        messageRepository.createMessage(messageInfo);
    }
}
