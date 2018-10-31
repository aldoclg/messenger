package com.daitangroup.messenger.domain;

import com.daitangroup.messenger.constants.ConstantsUtils;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Date;

public class ChatInfo {

    public static final byte[] tableNameAsBytes = Bytes.toBytes(ConstantsUtils.CHAT_TABLE);
    public static final String columnFamillyChat = "CF_CHAT";
    public static final byte[] columnFamillyChatAsBytes = Bytes.toBytes(columnFamillyChat);
    public static final byte[] chatIdAsBytes = Bytes.toBytes("chatId");
    public static final byte[] chatNameAsBytes = Bytes.toBytes("chatName");
    public static final byte[] userIdAsBytes = Bytes.toBytes("userId");

    private String chatId;

    private String chatName;

    private String userId;

    private long timestamp;

    public ChatInfo(byte[] chatId, byte[] chatName, byte[] userId, long timestamp) {
        this.chatId = Bytes.toString(chatId);
        this.chatName = Bytes.toString(chatName);
        this.userId = Bytes.toString(userId);
        this.timestamp = timestamp;
    }

    public ChatInfo(String chatId, String chatName, String userId) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.userId = userId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
