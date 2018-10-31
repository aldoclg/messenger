package com.daitangroup.messenger.domain;

import com.daitangroup.messenger.constants.ConstantsUtils;
import org.apache.hadoop.hbase.util.Bytes;


public class MessageInfo {

    public static final byte[] tableNameAsBytes = Bytes.toBytes(ConstantsUtils.MESSAGE_TABLE);
    public static final String columnFamillyMessageInfo = "CF_MESSAGE_INFO";
    public static final byte[] columnFamillyMessageAsBytes = Bytes.toBytes(columnFamillyMessageInfo);
    public static final byte[] chatIdAsBytes = Bytes.toBytes("chatId");
    public static final byte[] contentAsBytes = Bytes.toBytes("content");
    public static final byte[] fromUserIdAsBytes = Bytes.toBytes("fromUserId");

    public MessageInfo(byte[] content, byte[] fromUser, byte[] chatId, long timestamp) {
        this.content = Bytes.toString(content);
        this.fromUserId = Bytes.toString(fromUser);
        this.chatId = Bytes.toString(chatId);
        this.timestamp = timestamp;
    }

    public MessageInfo(String content, String fromUser, String chatId) {
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
}
