package com.daitangroup.messenger.domain;

import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Chat {

    public static final byte[] collumnFamillyChatAsBytes = Bytes.toBytes("CF_CHAT");
    public static final byte[] chatIdAsBytes = Bytes.toBytes("chatId");
    public static final byte[] chatNameAsBytes = Bytes.toBytes("chatName");
    public static final byte[] membersAsBytes = Bytes.toBytes("members");

    private long chatId;

    private String chatName;

    private List<Long> members;

    public Chat(long chatId, String chatName, List<Long> members) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.members = members;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public List<Long> getMembers() {
        return members;
    }

    public void setMembers(List<Long> members) {
        this.members = members;
    }

    public void addMember(long userId) {
        members.add(userId);
    }

    public void removeMember(long userId) {
        members.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return chatId == chat.chatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}
