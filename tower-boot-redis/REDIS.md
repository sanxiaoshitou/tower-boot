# tower-boot-redis redis基础能力组件

## redis 介绍
Redis是一种支持key-value等多种数据结构的存储系统。
可用于缓存，事件发布或订阅，高速队列等场景。支持网络，提供字符串，哈希，列表，队列，集合结构直接存取，基于内存，可持久化。

## 模块说明
redis基础能力封装

## 使用说明
### 一、yml配置 示例
redisKeyPrefix 项目前缀,会默认拼接key前缀
```yaml
spring:
    redis:
      host: 127.0.0.1
      port: 6379
      database: 0
      password:
      timeout: 5000
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          max-wait: -1
          min-idle: 0
      redisKeyPrefix: tower
```

### 二、基础用法
RedisService redis操作类

```java
@Autowired
private RedisService redisService;
```
```java
redisService.set(key,"ok");
System.out.println(redisService.get(key));
```



