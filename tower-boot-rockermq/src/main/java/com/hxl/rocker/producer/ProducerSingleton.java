package com.hxl.rocker.producer;

import com.hxl.rocker.RocketProperties;
import org.apache.rocketmq.client.apis.*;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.ProducerBuilder;
import org.apache.rocketmq.client.apis.producer.TransactionChecker;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/23 22:29
 */
public class ProducerSingleton {

    private RocketProperties properties;
    private static volatile Producer PRODUCER;
    private static volatile Producer TRANSACTIONAL_PRODUCER;

    public ProducerSingleton(RocketProperties properties){
        this.properties = properties;
    }

    private Producer buildProducer(TransactionChecker checker, String... topics) throws ClientException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        SessionCredentialsProvider sessionCredentialsProvider =
                new StaticSessionCredentialsProvider(properties.getAccessKey(), properties.getSecretKey());
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(properties.getNamesrvAddr())
                .setCredentialProvider(sessionCredentialsProvider)
                .build();
        final ProducerBuilder builder = provider.newProducerBuilder()
                .setClientConfiguration(clientConfiguration)
                .setTopics(topics);
        if (checker != null) {
            // Set the transaction checker.
            builder.setTransactionChecker(checker);
        }
        return builder.build();
    }

    public Producer getInstance(String... topics) throws ClientException {
        if (null == PRODUCER) {
            synchronized (ProducerSingleton.class) {
                if (null == PRODUCER) {
                    PRODUCER = buildProducer(null, topics);
                }
            }
        }
        return PRODUCER;
    }

    public Producer getTransactionalInstance(TransactionChecker checker, String... topics) throws ClientException {
        if (null == TRANSACTIONAL_PRODUCER) {
            synchronized (ProducerSingleton.class) {
                if (null == TRANSACTIONAL_PRODUCER) {
                    TRANSACTIONAL_PRODUCER = buildProducer(checker, topics);
                }
            }
        }
        return TRANSACTIONAL_PRODUCER;
    }
}
