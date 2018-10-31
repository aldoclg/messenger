package com.daitangroup.messenger.hadoop.hbase;

import static com.daitangroup.messenger.constants.ConstantsUtils.MESSAGE_TABLE;

import com.daitangroup.messenger.domain.MessageInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

//@Component
public class HbaseUtils //implements InitializingBean
{

    private byte[] tableNameAsBytes = MessageInfo.tableNameAsBytes;

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Resource(name = "hbaseConfig")
    private Configuration conf = hbaseTemplate.getConfiguration();

    private HBaseAdmin admin;

    public void initialize() throws IOException {
        if (admin.tableExists(tableNameAsBytes)) {
            if (admin.isTableDisabled(tableNameAsBytes)) {
                System.out.printf("Disabled %s.\n", MESSAGE_TABLE);

                System.out.println("Enabling...");

                admin.enableTable(tableNameAsBytes);

                System.out.println("Enabled.");
            }
        }
        else {
            System.out.printf("Table %s does not exists.\n", MESSAGE_TABLE);

            //HTableDescriptor tableDescriptor = new HTableDescriptor(MESSAGE_TABLE);

            System.out.println("Creating...");

            //admin.createTable(tableDescriptor);

            System.out.printf("Table %s created.\n", MESSAGE_TABLE);
        }
    }



    //@Override
    public void afterPropertiesSet() throws Exception {
        admin = new HBaseAdmin(conf);
    }
}
