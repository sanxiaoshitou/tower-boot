package com.hxl.rocker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConsumeStatus {

    RETRY(0, "重试"),

    COMMIT(1, "提交");

    private final int code;

    private final String desc;
}
