package com.daitangroup.messenger.configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

@Configuration
@EnableConfigurationProperties(HbaseProperties.class)
public class HbaseConfig {

    @Autowired
    private HbaseProperties hbaseProperties;

    private Logger LOGGER = LoggerFactory.getLogger(HbaseConfig.class);

    @Bean
    public HbaseTemplate hbaseTemplate() {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", this.hbaseProperties.getZkQuorum());
        configuration.set("hbase.rootdir", this.hbaseProperties.getRootDir());
        configuration.set("zookeeper.znode.parent", this.hbaseProperties.getZkBasePath());
        LOGGER.info("Configured HBASE ", configuration.toString());
        return new HbaseTemplate(configuration);
    }
}
