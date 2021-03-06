package com.daitangroup.messenger.domain;

import com.daitangroup.messenger.constants.ConstantsUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Objects;

public class ChatInfo {

    public static final byte[] tableNameAsBytes = Bytes.toBytes(ConstantsUtils.CHAT_TABLE);
    public static final String columnFamillyChatInfo = "CF_CHAT_INFO";
    public static final byte[] columnFamillyChatAsBytes = Bytes.toBytes(columnFamillyChatInfo);
    public static final byte[] chatIdAsBytes = Bytes.toBytes("chatId");
    public static final byte[] chatNameAsBytes = Bytes.toBytes("chatName");
    public static final byte[] userIdAsBytes = Bytes.toBytes("userId");

    private String chatId;

    private String chatName;

    private String userId;

    private long timestamp;

    @JsonCreator
    public ChatInfo(@JsonProperty(value = "chatId", required = false) String chatId,
                    @JsonProperty(value = "chatName", required = false) String chatName,
                    @JsonProperty(value = "userId", required = false) String userId) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.userId = userId;
    }

    public static final ChatInfo  bytesToChatInfo(byte[] chatId, byte[] chatName, byte[] userId, long timestamp) {
        ChatInfo chatInfo = new ChatInfo(Bytes.toString(chatId), Bytes.toString(chatName), Bytes.toString(userId));
        chatInfo.setTimestamp(timestamp);
        return chatInfo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatInfo chatInfo = (ChatInfo) o;
        return timestamp == chatInfo.timestamp &&
                Objects.equals(chatId, chatInfo.chatId) &&
                Objects.equals(chatName, chatInfo.chatName) &&
                Objects.equals(userId, chatInfo.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, chatName, userId, timestamp);
    }

    @Override
    public String toString() {
        return "ChatInfo{" +
                "chatId='" + chatId + '\'' +
                ", chatName='" + chatName + '\'' +
                ", userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
