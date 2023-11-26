package com.hxl.rocker;

import com.alibaba.fastjson.parser.Feature;
import com.hxl.rocker.enums.ConsumeMode;
import com.hxl.rocker.enums.ConsumerMessageMode;

import java.lang.annotation.*;
import java.lang.reflect.Type;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RocketConsumer {

    String topic();


    String consumerGroup();

    /**
     * 订阅表达式
     * 支持${}进行注入
     * <p>可使用 {tag1,tag2} 对多个{@link #topic()}关联，topic下标1关联tag下标1,topic2关联tag2以此类推，
     * 如果tag2没有设置，默认为{@code "*"}
     * <p>subscription expression.it only support or operation such as "tag1 || tag2 || tag3" <br>
     * if null or * expression,meaning subscribe all
     */
    String tag() default "*";

    /**
     * 消费模式
     */
    ConsumeMode consumeMode() default ConsumeMode.CLUSTER;

    /**
     * 消费失败最大重试次数
     *
     * @return 默认返回-1  （-1代表16次）
     */
    int maxReconsumeTimes() default -1;

    /**
     * 批量拉的最大消息量（虽然Rocket有推模式，但本质上底层依旧是拉模式的实现）
     * <p>该项指的是一次请求拉取的消息量
     *
     * @return 默认16条  阈值1~1024
     */
    int pullBatchSize() default 16;



//    /**
//     * 消息拉取模式
//     * push 推模式 实现 MessageListener
//     * pull 拉模式 实现 SimplePullConsumerListener
//     */
//    ConsumerMessageMode consumerMessageMode() default ConsumerMessageMode.PUSH;

//    /**
//     * 长轮询 等待时间
//     * @return
//     */
//    long awaitSecond() default 30L;
//
//    /**
//     * 设置消息收到后不可见的持续时间。
//     * Set message invisible duration after it is received.
//     */
//    long invisibleSeconds() default 15L;

}
