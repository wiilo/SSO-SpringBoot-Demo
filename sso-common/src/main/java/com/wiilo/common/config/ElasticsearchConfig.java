package com.wiilo.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elasticsearch 配置
 *
 * @author Whitlock Wang
 */
@Configuration
@Slf4j
public class ElasticsearchConfig {

    private static ElasticsearchConfig instance;
    private static RestHighLevelClient client;
    private static RestClient restClient;

    public ElasticsearchConfig() {
        // 官方文档：https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/installation.html
        log.info("初始化es客户端");
        HttpHost host = new HttpHost("192.168.8.127", 9200, "http");
        restClient = RestClient.builder(host).build();
        client = new RestHighLevelClient(RestClient.builder(host));
    }

    public static synchronized ElasticsearchConfig getInstance() {
        if (instance == null) {
            instance = new ElasticsearchConfig();
        }
        return instance;
    }

    @Bean
    public RestHighLevelClient getClient() {
        return client;
    }

    @Bean
    public RestClient getRestClient() {
        return restClient;
    }

    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (restClient != null) {
            try {
                restClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
