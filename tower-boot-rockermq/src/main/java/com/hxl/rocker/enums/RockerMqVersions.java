package com.hxl.rocker.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 版本号
 * @Author hxl
 * @Date 2023/11/26 1:21
 */
@AllArgsConstructor
@Getter
public enum RockerMqVersions {

    ALI_4(4,"阿里云4.x"),
    ALI_5(5,"阿里云5.x"),
    ;

    private final Integer code;

    private final String desc;
}
