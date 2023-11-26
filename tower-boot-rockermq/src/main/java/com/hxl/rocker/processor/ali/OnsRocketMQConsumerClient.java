package com.hxl.rocker.processor.ali;

import com.aliyun.openservices.ons.api.*;
import com.hxl.rocker.RocketConsumer;
import com.hxl.rocker.RocketProperties;
import com.hxl.rocker.enums.ConsumeMode;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/26 1:02
 */
@Configuration
public class OnsRocketMQConsumerClient {

    private RocketProperties properties;

    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    public OnsRocketMQConsumerClient(RocketProperties properties, AutowireCapableBeanFactory autowireCapableBeanFactory) {
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

    private Properties getProperties(RocketProperties rocketProperties, RocketConsumer consumer) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, rocketProperties.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, rocketProperties.getSecretKey());
        properties.put(PropertyKeyConst.NAMESRV_ADDR, rocketProperties.getNamesrvAddr());
        properties.put(PropertyKeyConst.ConsumeTimeout, 15);
        properties.put(PropertyKeyConst.ConsumeThreadNums, 10);
        // 订阅方式
        properties.put(PropertyKeyConst.MessageModel, consumer.consumeMode() == ConsumeMode.RADIO ? PropertyValueConst.BROADCASTING : PropertyValueConst.CLUSTERING);
        properties.put(PropertyKeyConst.ConsumeMessageBatchMaxSize, consumer.pullBatchSize());
        properties.put(PropertyKeyConst.MaxReconsumeTimes, consumer.maxReconsumeTimes());
        properties.put(PropertyKeyConst.MAX_BATCH_MESSAGE_COUNT, consumer.pullBatchSize());
        String consumerGroup = consumer.consumerGroup();
        properties.put(PropertyKeyConst.GROUP_ID, consumerGroup);
        return properties;
    }

    public void consumerStart(RocketConsumer rocketConsumer, MessageListener listener){
        String topic = rocketConsumer.topic();
        String group = rocketConsumer.consumerGroup();
        String tag = rocketConsumer.tag();

        Consumer consumer = ONSFactory.createConsumer(getProperties(this.properties, rocketConsumer));
        consumer.subscribe(topic, tag,listener);
        consumer.start();
        System.out.println("RocketMQ Consumer started. Topic: " + topic + ", Group: " + group);
    }

}
