package com.daitangroup.messenger.websocket.impl;

import static com.daitangroup.messenger.constants.ConstantsUtils.SUBSCRIBE_TO_RECEIVE_MESSAGE_URN;

import com.daitangroup.messenger.domain.MessageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatBrokerControllerImplTest {

    private static final String SEND_MESSAGE = "/messenger/chat";

    @Value("${local.server.port}")
    private int port;
    private String URL;

    private MessageInfo messageInfo;

    private CompletableFuture<MessageInfo> completableFuture;

    @Before
    public void setUp() throws Exception {
        completableFuture = new CompletableFuture<>();
        URL = "ws://localhost:" + port + "/socket/";
        messageInfo = new MessageInfo("Hello, hello!!!", "5bd8879779fe6c6519c19c85", "54322c1c-600c-48ef-ac80-668b2baf307c");
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    public void sendMessage() throws InterruptedException, ExecutionException, TimeoutException {

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(1, TimeUnit.SECONDS);

        System.out.println("URL " + URL);//+ "5bd9d69179fe6c209ae90bc1"

        stompSession.subscribe(SUBSCRIBE_TO_RECEIVE_MESSAGE_URN + "5bd9d69179fe6c209ae90bc1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                System.out.println(stompHeaders.toString());
                return MessageInfo.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                System.out.println((MessageInfo) o + " RECEIVED!!!");
                completableFuture.complete((MessageInfo) o);
            }
        });

        stompSession.send(SEND_MESSAGE, messageInfo);

        MessageInfo messageInfo1 = completableFuture.get(5, TimeUnit.SECONDS);

        assertNotNull(messageInfo1);
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }
}