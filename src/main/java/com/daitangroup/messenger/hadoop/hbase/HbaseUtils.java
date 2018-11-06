package com.daitangroup.messenger.hadoop.hbase;

import com.daitangroup.messenger.domain.ChatInfo;
import com.daitangroup.messenger.domain.MessageInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class HbaseUtils implements InitializingBean {

    private HBaseAdmin admin;

    @Bean
    HBaseAdmin hBaseAdmin() throws IOException {
        HBaseConfiguration hBaseConfiguration = new HBaseConfiguration(new Configuration());
        return new HBaseAdmin(hBaseConfiguration);
    }

    @PostConstruct
    public void initialize() throws IOException {

        checkIfExistTable(ChatInfo.tableNameAsBytes, ChatInfo.columnFamillyChatAsBytes);

        checkIfExistTable(MessageInfo.tableNameAsBytes, MessageInfo.columnFamillyMessageAsBytes);
    }

    private void checkIfExistTable(byte[] table, byte[] columnFamily) throws IOException  {

        HBaseAdmin admin = hBaseAdmin();

        String tableAsString = Bytes.toString(table);

        if (admin.tableExists(table)) {
            if (admin.isTableDisabled(table)) {
                System.out.printf("Disabled %s.\n", tableAsString);

                System.out.println("Enabling...");

                admin.enableTable(table);

                System.out.println("Enabled.");
            }
        }
        else {
            System.out.printf("Table %s does not exists.\n", tableAsString);

            HTableDescriptor tableDescriptor = new HTableDescriptor(tableAsString);
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(columnFamily);
            tableDescriptor.addFamily(columnDescriptor);

            System.out.println("Creating...");

            admin.createTable(tableDescriptor);

            System.out.printf("Table %s created.\n", tableAsString);
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        admin = hBaseAdmin();
    }
}
