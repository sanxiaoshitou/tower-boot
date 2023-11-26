package com.hxl.rocker.producer.ali;

import com.hxl.rocker.constants.RockerConstants;
import com.hxl.rocker.model.RocketMsg;
import com.hxl.rocker.producer.ProducerSingleton;
import com.hxl.rocker.producer.RocketMessageProducer;
import com.hxl.rocker.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @Description 消息生产实现
 * @Author hxl
 * @Date 2023/11/24 0:04
 */
@Slf4j
public class RocketMessageProducerImpl implements RocketMessageProducer {

    @Autowired
    private ProducerSingleton producerSingleton;

    @Override
    public Boolean sendMessage(String topic, String tag, RocketMsg rocketMsg) {
        return sendMessage(topic, tag, null, null, rocketMsg);
    }

    @Override
    public Boolean sendMessage(String topic, String tag, String messageGroup, RocketMsg rocketMsg) {
        return sendMessage(topic, tag, messageGroup, null, rocketMsg);
    }

    @Override
    public Boolean sendMessage(String topic, String tag, Long delayTime, RocketMsg rocketMsg) {
        return sendMessage(topic, tag, null, delayTime, rocketMsg);
    }

    @Override
    public Boolean sendMessage(String topic, String tag, String messageGroup, Long delayTime, RocketMsg rocketMsg) {
        try {
            ClientServiceProvider provider = ClientServiceProvider.loadService();
            Producer producer = producerSingleton.getInstance(topic);
            if(StringUtils.isEmpty(tag)){
                tag = RockerConstants.FILTER_EXPRESSION;
            }
            Message message = MessageUtil.buildMessage(provider, topic, tag, rocketMsg, messageGroup, delayTime);
            SendReceipt sendReceipt = producer.send(message);
            log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
