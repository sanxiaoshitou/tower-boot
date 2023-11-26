package com.hxl.starter.mq.v4;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.hxl.rocker.RocketConsumer;
import com.hxl.starter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/26 2:19
 */
@Slf4j
@Component
@RocketConsumer(topic = "PRODUCER_TOPIC", consumerGroup = "PRODUCER_GROUP")
public class Push4MQConsumer implements MessageListener {

    @Autowired
    private UserService userService;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        String body = new String(message.getBody());
        log.info("TestMQConsumer:" + body + "user:" + userService.getUserId());
        return Action.CommitMessage;
    }
}
