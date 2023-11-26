package com.hxl.rocker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/22 22:26
 */
@ConfigurationProperties(RocketProperties.ROCKET_PREFIX)
@Data
public class RocketProperties {

    public static final String ROCKET_PREFIX = "tower.rocket";

    private static final String CUE = "RocketMq配置";

    /**
     * 命名服务地址
     */
    private String namesrvAddr;

    /**
     * 开源版：默认超时时间
     * 商用版：商用版的api没有单个消息的API超时时间，故此配置在商业版RocketMQ是全局
     */
    private long sendTimeout = 3000;

    /**
     *
     */
    private String accessKey;

    /**
     *
     */
    private String secretKey;

    /**
     * 扫描包 列：com.hxl
     */
    private String packageName;

    /**
     * @see com.hxl.rocker.enums.RockerMqVersions
     * 阿里版本号  目前支持 4.x 5.x 版本
     */
    private Integer versions;
}
