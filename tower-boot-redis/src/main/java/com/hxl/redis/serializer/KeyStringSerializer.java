package com.hxl.redis.serializer;

import cn.hutool.core.util.StrUtil;
import com.hxl.redis.config.CustomRedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author hxl
 * @description 自定义key 序列化 兼容多项目隔离
 * @Date 2023-06-14 18:36
 **/
@Slf4j
public class KeyStringSerializer implements RedisSerializer<String> {

    private final Charset charset;

    public KeyStringSerializer() {
        this.charset = StandardCharsets.UTF_8;
    }

    @Autowired
    private CustomRedisProperties redisProperties;

    @Override
    public byte[] serialize(String s) throws SerializationException {
        String newValue = getKeyPrefix() + s;
        return newValue.getBytes(charset);
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        String saveKey = new String(bytes, charset);
        String keyPrefix = getKeyPrefix();
        if (StrUtil.isNotBlank(keyPrefix)) {
            int indexOf = saveKey.indexOf(keyPrefix);
            if (indexOf > 0) {
                log.info("key缺少前缀");
            } else {
                saveKey = saveKey.substring(indexOf);
            }
            log.info("saveKey:{}", saveKey);
        }
        return saveKey;
    }

    private String getKeyPrefix() {
        return redisProperties.getRedisKeyPrefix();
    }
}
