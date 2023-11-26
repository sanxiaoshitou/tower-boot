package com.hxl.rocker;

import com.hxl.rocker.processor.RocketMQConsumerProcessor;
import com.hxl.rocker.processor.ali.OnsRocketMQConsumerClient;
import com.hxl.rocker.processor.ali.RocketMQConsumerClient;
import com.hxl.rocker.producer.ProducerSingleton;
import com.hxl.rocker.producer.RocketMessageProducer;
import com.hxl.rocker.producer.ali.RocketMessageProducerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/23 2:40
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RocketProperties.class)
public class RocketConfiguration {

    @Autowired
    private RocketProperties rocketProperties;

    @Autowired
    private RocketMQConsumerProcessor rocketMQConsumerProcessor;

    @PostConstruct
    public void init() {
        rocketMQConsumerProcessor.init(rocketProperties);
        log.info("enabled  Rocket ,namesrvAddr: {}", rocketProperties.getNamesrvAddr());
    }

    @Bean
    public RocketMQConsumerProcessor towerRocketMQConsumerProcessor() {
        return new RocketMQConsumerProcessor();
    }

    @Bean
    public RocketMQConsumerClient towerRocketMQConsumerClient(RocketProperties rocketProperties, AutowireCapableBeanFactory autowireCapableBeanFactory){
        return new RocketMQConsumerClient(rocketProperties, autowireCapableBeanFactory);
    }

    @Bean
    public OnsRocketMQConsumerClient towerOnsRocketMQConsumerClient(RocketProperties rocketProperties, AutowireCapableBeanFactory autowireCapableBeanFactory){
        return new OnsRocketMQConsumerClient(rocketProperties, autowireCapableBeanFactory);
    }

    @Bean
    public ProducerSingleton getProducerSingleton(RocketProperties rocketProperties) {
        return new ProducerSingleton(rocketProperties);
    }

    @Bean
    public RocketMessageProducer getRocketMessageProducer() {
        return new RocketMessageProducerImpl();
    }
}
