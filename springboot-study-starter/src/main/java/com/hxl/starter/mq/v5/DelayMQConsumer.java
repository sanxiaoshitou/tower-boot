package com.hxl.starter.mq.v5;

import com.hxl.rocker.RocketConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/25 21:58
 */
@Slf4j
//@Component
//@RocketConsumer(topic = "DELAY_TOPIC", consumerGroup = "DELAY_GROUP")
public class DelayMQConsumer implements MessageListener {
    @Override
    public ConsumeResult consume(MessageView messageView) {
        String body = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();
        log.info("TestMQConsumer:" + body);
        return ConsumeResult.SUCCESS;
    }
}
