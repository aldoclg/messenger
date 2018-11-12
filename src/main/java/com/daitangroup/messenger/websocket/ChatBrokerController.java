package com.daitangroup.messenger.websocket;

import com.daitangroup.messenger.domain.MessageInfo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public interface ChatBrokerController {
    @MessageMapping("/chat")
    void sendMessage(MessageInfo messageInfo);
}
