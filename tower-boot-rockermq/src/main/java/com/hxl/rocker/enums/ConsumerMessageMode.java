package com.hxl.rocker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 消费组 获取消息模式
 * @Author hxl
 * @Date 2023/11/25 10:57
 */
@AllArgsConstructor
@Getter
public enum ConsumerMessageMode {

    PUSH(1, "推模式"),
    PULL(2, "拉模式");

    private final Integer code;

    private final String desc;
}
