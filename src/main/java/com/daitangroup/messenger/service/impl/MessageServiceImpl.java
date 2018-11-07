package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.repository.MessageRepository;
import com.daitangroup.messenger.service.MessageService;

public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMessage(MessageInfo messageInfo) {
        messageRepository.createMessage(messageInfo);
    }
}
