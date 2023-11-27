# tower-boot-redis redis基础能力组件

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



