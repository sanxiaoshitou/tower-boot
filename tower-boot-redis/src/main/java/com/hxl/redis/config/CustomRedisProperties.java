package com.hxl.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author hxl
 * @description
 * @Date 2023-06-14 18:35
 **/
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class CustomRedisProperties {

    /**
     * redis key前缀,一般用项目名做区分，数据隔离
     */
    private String redisKeyPrefix = "";
}
