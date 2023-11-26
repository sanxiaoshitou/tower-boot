package com.hxl.rocker.enums;

import lombok.Getter;

@Getter
public enum ConsumeMode {
    /**
     * 广播模式（同一消费组所有机器都可消费一次）
     * <p>
     * 使用场景：期望在同一消费组内所有机器都能消费一次
     * <p>
     * 例：清除本地缓存
     */
    RADIO(1, "广播"),

    /**
     * 集群模式（只能有一个机器消费一次 ps：不同消费组也不可以再次消费）
     * <p>
     * 使用场景：N个消费组监听同一个主题，相同消费组只有一个机器能消费
     * <p>
     * 例：下单异步推送订单
     */
    CLUSTER(2, "集群");

    ConsumeMode(Integer code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    private Integer code;

    private String desc;
}
