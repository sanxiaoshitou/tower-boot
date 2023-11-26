package com.hxl.starter.mq.v5;

import com.hxl.rocker.RocketConsumer;
import com.hxl.starter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.MessageListener;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/23 17:58
 */
@Slf4j
//@Component
//@RocketConsumer(topic = "PRODUCER_TOPIC", consumerGroup = "PRODUCER_GROUP")
public class TestMQConsumer implements MessageListener {

    @Autowired
    private UserService userService;

    @Override
    public ConsumeResult consume(MessageView messageView) {
        String body = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();
        log.info("TestMQConsumer:" + body + "user:" + userService.getUserId());
        return ConsumeResult.SUCCESS;
    }
}
