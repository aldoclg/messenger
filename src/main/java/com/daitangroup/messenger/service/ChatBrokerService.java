package com.daitangroup.messenger.service;

import com.daitangroup.messenger.domain.MessageInfo;
import org.springframework.stereotype.Service;

@Service
public interface ChatBrokerService {
    public void sendMessage(MessageInfo messageInfo);
}
