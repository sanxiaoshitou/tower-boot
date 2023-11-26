package com.hxl.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hxl.redis.config.CustomRedisProperties;
import com.hxl.redis.load.DefaultRedisServiceImpl;
import com.hxl.redis.load.RedisService;
import com.hxl.redis.serializer.KeyStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

/**
 * @Author hxl
 * @description
 * @Date 2023-06-14 17:17
 **/
@Slf4j
@EnableCaching
@Configuration
@EnableConfigurationProperties(CustomRedisProperties.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class ExecuteRedisAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @PostConstruct
    public void init() {
        log.info("enabled  Redis ,ip: {}", redisProperties.getHost());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       ObjectMapper objectMapper, KeyStringSerializer keyStringSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //key序列化方式
        RedisSerializer<String> defalutSerializer = template.getStringSerializer();
        //值序列化方式
        RedisSerializer<Object> jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        //设置key 的序列化方式
        template.setKeySerializer(keyStringSerializer);
        template.setHashKeySerializer(keyStringSerializer);
        //设置值 的序列化方式
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        //设置默认的序列化方式
        template.setDefaultSerializer(defalutSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public KeyStringSerializer keyStringSerializer() {
        return new KeyStringSerializer();
    }

    @Bean
    public RedisService redisService(ObjectMapper objectMapper, RedisTemplate<String, Object> redisTemplate) {
        return new DefaultRedisServiceImpl(objectMapper, redisTemplate);
    }
}
