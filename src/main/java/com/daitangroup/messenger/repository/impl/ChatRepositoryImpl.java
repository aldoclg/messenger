package com.daitangroup.messenger.repository.impl;

import com.daitangroup.messenger.constants.ConstantsUtils;
import com.daitangroup.messenger.domain.ChatInfo;
import com.daitangroup.messenger.repository.ChatRepository;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatRepositoryImpl implements ChatRepository {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    public void createChat(String chatName, String... userId) {
        List<Put> putList = new ArrayList<>();
        String chatId = UUID.randomUUID().toString();
        for (String u: userId) {
            ChatInfo chatInfo = new ChatInfo(chatId, chatName, u);
            Put put = new Put(Bytes.toBytes(UUID.randomUUID().toString()));
            mapperChatInfo(chatInfo, put);
            putList.add(put);
        }

        hbaseTemplate.execute(ConstantsUtils.CHAT_TABLE, new TableCallback<Void>() {

            @Override
            public Void doInTable(HTableInterface hTableInterface) throws Throwable {
                hTableInterface.put(putList);
                return null;
            }
        });
    }

    @Override
    public void createChat(ChatInfo chatInfo) {
        saveChat(chatInfo);
    }

    @Override
    public void insertUserToChat(String chatId, String... userId) {

    }

    @Override
    public void updateChat(String chatId, String chatName) {

    }

    @Override
    public void removeUserByChatIdAndUserId(String chatId, String userId) {

    }

    @Override
    public void removeUser(String uniqueId) {

    }

    @Override
    public void removeChat(String chatId) {

    }

    private void saveChat(ChatInfo chatInfo) {
        Put put = new Put(Bytes.toBytes(UUID.randomUUID().toString()));
        mapperChatInfo(chatInfo, put);
        hbaseTemplate.execute(ConstantsUtils.CHAT_TABLE, new TableCallback<Void>() {

            @Override
            public Void doInTable(HTableInterface hTableInterface) throws Throwable {
                hTableInterface.put(put);
                return null;
            }
        });
    }

    private void mapperChatInfo(ChatInfo chatInfo, Put put) {
        put.add(ChatInfo.columnFamillyChatAsBytes,
                ChatInfo.chatIdAsBytes,
                Bytes.toBytes(chatInfo.getChatId()));
        put.add(ChatInfo.columnFamillyChatAsBytes,
                ChatInfo.chatNameAsBytes,
                Bytes.toBytes(chatInfo.getChatName()));
        put.add(ChatInfo.columnFamillyChatAsBytes,
                ChatInfo.userIdAsBytes,
                Bytes.toBytes(chatInfo.getUserId()));
    }

    @Override
    public List<ChatInfo> findChat(String chatId) {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(ChatInfo.columnFamillyChatAsBytes,
                ChatInfo.chatIdAsBytes,
                CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes(chatId)));
        return findChatInfoByFilter(filter);
    }

    @Override
    public List<ChatInfo> findChatByUserId(String userId) {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(ChatInfo.columnFamillyChatAsBytes,
                ChatInfo.userIdAsBytes,
                CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes(userId)));
        filter.setFilterIfMissing(true);

        return findChatInfoByFilter(filter);
    }

    private List<ChatInfo> findChatInfoByFilter(Filter filter) {
        Scan scan = new Scan();
        scan.setFilter(filter);
        scan.addFamily(ChatInfo.columnFamillyChatAsBytes);
        return findChatInfoByScan(scan);
    }

    private List<ChatInfo> findChatInfoByScan(Scan scan) {
        return hbaseTemplate.find(ConstantsUtils.CHAT_TABLE, scan, new RowMapper<ChatInfo>() {
            @Override
            public ChatInfo mapRow(Result result, int i) throws Exception {

                return ChatInfo.bytesToChatInfo(result.getValue(ChatInfo.columnFamillyChatAsBytes, ChatInfo.chatIdAsBytes),
                        result.getValue(ChatInfo.columnFamillyChatAsBytes, ChatInfo.chatNameAsBytes),
                        result.getValue(ChatInfo.columnFamillyChatAsBytes, ChatInfo.userIdAsBytes),
                        result.raw()[0].getTimestamp());
            }
        });
    }
}
