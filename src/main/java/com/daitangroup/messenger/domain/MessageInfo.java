package com.daitangroup.messenger.domain;

import org.apache.hadoop.hbase.util.Bytes;

import java.util.Date;
import java.util.Objects;


public class Message {

    public static final byte[] collumnFamillyMessageAsBytes = Bytes.toBytes("CF_MESSAGE");
    public static final byte[] messageIdAsBytes = Bytes.toBytes("messageId");
    public static final byte[] messageValueAsBytes = Bytes.toBytes("messageValue");
    public static final byte[] fromUserAsBytes = Bytes.toBytes("fromUser");
    public static final byte[] timestampAsBytes = Bytes.toBytes("timestamp");
    public static final byte[] statusAsBytes = Bytes.toBytes("status");

    private long messageId;

    privaye long ownerId;

    private String messageValue;

    private String fromUser;

    private Date timestamp;

    private int status;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessageValue() {
        return messageValue;
    }

    public void setMessageValue(String messageValue) {
        this.messageValue = messageValue;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return messageId == message.messageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
}
