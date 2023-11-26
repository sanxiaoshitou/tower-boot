package com.hxl.rocker.processor.ali;

import com.hxl.rocker.RocketConsumer;
import com.hxl.rocker.RocketProperties;
import com.hxl.rocker.constants.RockerConstants;
import org.apache.rocketmq.client.apis.*;
import org.apache.rocketmq.client.apis.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Collections;


/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/23 17:09
 */
@Configuration
public class RocketMQConsumerClient {

    private RocketProperties properties;

    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    public RocketMQConsumerClient(RocketProperties properties, AutowireCapableBeanFactory autowireCapableBeanFactory) {
        this.properties = properties;
        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
    }


    public void start(BeanDefinition beanDefinition) {
        try {
            //动态监听器 支持注入bean
            Class<?> annotatedClass = Class.forName(beanDefinition.getBeanClassName());
            RocketConsumer consumerAnnotation = annotatedClass.getAnnotation(RocketConsumer.class);
            MessageListener listener = (MessageListener) autowireCapableBeanFactory.createBean(annotatedClass);
            consumerStart(consumerAnnotation, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClientConfiguration initClientConfiguration(RocketProperties properties) {
        ClientConfigurationBuilder configBuilder = ClientConfiguration.newBuilder().setEndpoints(properties.getNamesrvAddr());
        configBuilder.setCredentialProvider(
                new StaticSessionCredentialsProvider(properties.getAccessKey(), properties.getSecretKey())
        );
        return configBuilder.build();
    }

    public void consumerStart(RocketConsumer rocketConsumer, MessageListener listener) throws ClientException {
        ClientConfiguration clientConfiguration = initClientConfiguration(this.properties);
        String TOPIC_NAME = rocketConsumer.topic();
        String CONSUMER_GROUP_ID = rocketConsumer.consumerGroup();
        String TAG = StringUtils.isEmpty(rocketConsumer.tag()) ? RockerConstants.FILTER_EXPRESSION : rocketConsumer.tag();
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        // 订阅消息的过滤规则。* 代表订阅全部消息。
        FilterExpression filterExpression = new FilterExpression(TAG, FilterExpressionType.TAG);

        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(clientConfiguration)
                .setConsumerGroup(CONSUMER_GROUP_ID)
                .setSubscriptionExpressions(Collections.singletonMap(TOPIC_NAME, filterExpression))
                .setMessageListener(listener)
                .build();
//            Thread.sleep(Long.MAX_VALUE);
//
//            pushConsumer.close();
    }
}
