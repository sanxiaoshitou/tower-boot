package com.hxl.rocker.producer;

import com.hxl.rocker.model.RocketMsg;

/**
 * @Description 生产者 发送消息接口
 * @Author hxl
 * @Date 2023/11/23 17:09
 */
public interface RocketMessageProducer {

    /**
     * 普通消息
     *
     * @param topic     topic
     * @param tag       tag
     * @param rocketMsg 消息体信息
     * @return boolean
     */
    Boolean sendMessage(String topic, String tag, RocketMsg rocketMsg);

    /**
     * 顺序消息，指定消费组
     *
     * @param topic     topic
     * @param tag       tag
     * @param messageGroup 消费组
     * @param rocketMsg 消息体信息
     * @return boolean
     */
    Boolean sendMessage(String topic, String tag, String messageGroup, RocketMsg rocketMsg);

    /**
     * 普通延迟消息
     *
     * @param topic     topic
     * @param tag       tag
     * @param delayTime 延迟时间 单位：秒
     * @param rocketMsg 消息体信息
     * @return boolean
     */
    Boolean sendMessage(String topic, String tag, Long delayTime, RocketMsg rocketMsg);

    /**
     * 顺序延迟消息 指定消费组
     * @param topic     topic
     * @param tag       tag
     * @param messageGroup 消费组
     * @param delayTime 延迟时间 单位：秒
     * @param rocketMsg 消息体信息
     * @return boolean
     */
    Boolean sendMessage(String topic, String tag, String messageGroup, Long delayTime, RocketMsg rocketMsg);
}
