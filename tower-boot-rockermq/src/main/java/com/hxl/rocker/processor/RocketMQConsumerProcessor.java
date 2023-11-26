package com.hxl.rocker.processor;

import com.hxl.rocker.RocketConsumer;
import com.hxl.rocker.RocketProperties;
import com.hxl.rocker.constants.RockerConstants;
import com.hxl.rocker.enums.RockerMqVersions;
import com.hxl.rocker.processor.ali.OnsRocketMQConsumerClient;
import com.hxl.rocker.processor.ali.RocketMQConsumerClient;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.StaticSessionCredentialsProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @Description 消费组监听器处理器
 * @Author hxl
 * @Date 2023/11/23 17:09
 */
@Configuration
public class RocketMQConsumerProcessor {

    @Autowired
    private RocketMQConsumerClient rocketMQConsumerClient;

    @Autowired
    private OnsRocketMQConsumerClient onsRocketMQConsumerClient;

    public RocketMQConsumerProcessor() {

    }

    public void init(RocketProperties properties) {
        String packageName = properties.getPackageName(); // 扫描包路径
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RocketConsumer.class));
        Set<BeanDefinition> candidates = scanner.findCandidateComponents(packageName);
        for (BeanDefinition beanDefinition : candidates) {
            try {
                Integer versions = properties.getVersions();
                if (Objects.equals(RockerMqVersions.ALI_4.getCode(), versions)) {
                    onsRocketMQConsumerClient.start(beanDefinition);
                }

                if (Objects.equals(RockerMqVersions.ALI_5.getCode(), versions)) {
                    rocketMQConsumerClient.start(beanDefinition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
