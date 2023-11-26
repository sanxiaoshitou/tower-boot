package com.hxl.rocker.utils;

import com.hxl.rocker.model.RocketMsg;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.message.MessageBuilder;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

/**
 * @Description Message 转换工具包
 * @Author hxl
 * @Date 2023/11/24 22:15
 */
public class MessageUtil {

    public static Message buildMessage(ClientServiceProvider provider, String topic, String tag,
                                       RocketMsg rocketMsg, String messageGroup, Long delayTime) {
        byte[] body = rocketMsg.getBody().getBytes(StandardCharsets.UTF_8);

        MessageBuilder messageBuilder = provider.newMessageBuilder();
        messageBuilder.setTopic(topic);
        messageBuilder.setTag(tag);
        messageBuilder.setBody(body);
        if (!StringUtils.isEmpty(rocketMsg.getKey())) {
            messageBuilder.setKeys(rocketMsg.getKey());
        }
        if (!StringUtils.isEmpty(messageGroup)) {
            messageBuilder.setMessageGroup(messageGroup);
        }
        if(Objects.nonNull(delayTime)){
            Duration messageDelayTime = Duration.ofSeconds(delayTime);
            messageBuilder.setDeliveryTimestamp(System.currentTimeMillis() + messageDelayTime.toMillis());
        }
        return messageBuilder.build();
    }
}
