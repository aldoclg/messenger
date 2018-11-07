package com.daitangroup.messenger.domain;

import com.daitangroup.messenger.constants.ConstantsUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Objects;


public class MessageInfo {

    public static final byte[] tableNameAsBytes = Bytes.toBytes(ConstantsUtils.MESSAGE_TABLE);
    public static final String columnFamillyMessageInfo = "CF_MESSAGE_INFO";
    public static final byte[] columnFamillyMessageAsBytes = Bytes.toBytes(columnFamillyMessageInfo);
    public static final byte[] chatIdAsBytes = Bytes.toBytes("chatId");
    public static final byte[] contentAsBytes = Bytes.toBytes("content");
    public static final byte[] fromUserIdAsBytes = Bytes.toBytes("fromUserId");

    public static final MessageInfo bytesToMessageInfo(byte[] content, byte[] fromUser, byte[] chatId, long timestamp) {
        MessageInfo messageInfo = new MessageInfo(Bytes.toString(content), Bytes.toString(fromUser), Bytes.toString(chatId));
        messageInfo.setTimestamp(timestamp);
        return messageInfo;
    }

    @JsonCreator
    public MessageInfo(@JsonProperty(value = "content", required = false) String content,
                       @JsonProperty(value = "fromUser", required = false) String fromUser,
                       @JsonProperty(value = "chatId", required = false) String chatId) {
        this.content = content;
        this.fromUserId = fromUser;
        this.chatId = chatId;
    }

    private String content;

    private String fromUserId;

    private String chatId;

    private long timestamp;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageInfo that = (MessageInfo) o;
        return timestamp == that.timestamp &&
                Objects.equals(fromUserId, that.fromUserId) &&
                Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUserId, chatId, timestamp);
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "content='" + content + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", chatId='" + chatId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
