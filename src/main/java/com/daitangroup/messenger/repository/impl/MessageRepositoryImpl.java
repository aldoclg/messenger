package com.daitangroup.messenger.repository.impl;

import com.daitangroup.messenger.constants.ConstantsUtils;
import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.repository.MessageRepository;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MessageRepositoryImpl implements MessageRepository {

    private Logger LOGGER = LoggerFactory.getLogger(MessageRepositoryImpl.class);

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    public void createMessage(MessageInfo messageInfo) {
        LOGGER.info("Called createMessage method {}", messageInfo);
        saveMessage(messageInfo);
    }

    @Override
    public void deleteMessage(String uniqueId) {

    }

    @Override
    public void updateMessage(String rowId, String content) {
        LOGGER.info("Called updateMessage method {} {}", rowId, content);
        Put put = new Put(Bytes.toBytes(rowId));
        put.add(MessageInfo.columnFamillyMessageAsBytes,
                MessageInfo.contentAsBytes,
                Bytes.toBytes(content));
        saveMessage(put);
    }

    @Override
    public List<MessageInfo> findMessageByChatId(String chatId, long startDate, long endDate) throws IOException {
        LOGGER.info("Called findMessageByChatId method {} {} {}", chatId, startDate, endDate);
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(MessageInfo.columnFamillyMessageAsBytes,
                MessageInfo.chatIdAsBytes,
                CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes(chatId)));
        return findMessageInfoByScan(createScan(singleColumnValueFilter, startDate, endDate));
    }

    @Override
    public List<MessageInfo> findMessage(long startDate, long endDate) throws IOException {
        return null;
    }

    @Override
    public List<MessageInfo> findMessageByUserId(String userId) {
        LOGGER.info("Called findMessageByUserId method {}", userId);
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(MessageInfo.columnFamillyMessageAsBytes,
                MessageInfo.fromUserIdAsBytes,
                CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes(userId)));

        return null;
    }

    private void saveMessage(MessageInfo messageInfo) {
        Put put = new Put(Bytes.toBytes(UUID.randomUUID().toString()));
        mapperMessageInfo(messageInfo, put);
        saveMessage(put);
    }

    private void saveMessage(Put put) {
        hbaseTemplate.execute(ConstantsUtils.MESSAGE_TABLE, hTableInterface -> {
                hTableInterface.put(put);
                return null;
        });
    }

    private void mapperMessageInfo(MessageInfo messageInfo, Put put) {
        put.add(MessageInfo.columnFamillyMessageAsBytes,
                MessageInfo.chatIdAsBytes,
                Bytes.toBytes(messageInfo.getChatId()));
        put.add(MessageInfo.columnFamillyMessageAsBytes,
                MessageInfo.fromUserIdAsBytes,
                Bytes.toBytes(messageInfo.getFromUserId()));
        put.add(MessageInfo.columnFamillyMessageAsBytes,
                MessageInfo.contentAsBytes,
                Bytes.toBytes(messageInfo.getContent()));
    }

    private Scan createScan() {
        Scan scan = new Scan();
        scan.addFamily(MessageInfo.columnFamillyMessageAsBytes);
        return scan;
    }

    private Scan createScan(Filter filter) {
        Scan scan = createScan();
        scan.setFilter(filter);
        return scan;
    }

    private Scan createScan(long startDate, long endDate) throws IOException {
        Scan scan = createScan();
        scan.setTimeRange(endDate, startDate);
        return scan;
    }

    private Scan createScan(Filter filter, long startDate, long endDate) throws IOException {
        Scan scan = createScan(filter);
        scan.setTimeRange(endDate, startDate);
        return scan;
    }

    private List<MessageInfo> findMessageInfoByScan(Scan scan) {
        return hbaseTemplate.find(ConstantsUtils.MESSAGE_TABLE, scan, (result, i) ->
                MessageInfo.bytesToMessageInfo(result.getValue(MessageInfo.columnFamillyMessageAsBytes, MessageInfo.chatIdAsBytes),
                    result.getValue(MessageInfo.columnFamillyMessageAsBytes, MessageInfo.contentAsBytes),
                    result.getValue(MessageInfo.columnFamillyMessageAsBytes, MessageInfo.fromUserIdAsBytes),
                    result.raw()[0].getTimestamp())
        );
    }

}
