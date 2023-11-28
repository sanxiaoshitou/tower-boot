## 开源RocketMQ和阿里云rockerMq 4.x和5.x集成

### 一、云平台创建实例
参考文档：
[阿里云api](https://help.aliyun.com/zh/apsaramq-for-rocketmq/cloud-message-queue-rocketmq-5-x-series/developer-reference/sdk-reference/?spm=a2c4g.11186623.0.0.1b9c62391rWCjn)

### 二、skd集成思路
公司用的RocketMQ一般是自建开源apache的RocketMQ和上aliyun的RocketMQ，目前阿里云支持4.x和5.x版本   
项目集成思路：   
1、集成阿里RocketMQ 两个版本 4.x和5.x 支持版本配置和开源apache的RocketMQ集成    
2、RocketProperties 单配置适用多版本集成   
3、RocketConsumer 消费者注解，支持多版本集成(消费组监听器继承实现可以不一样，注解一致）    
4、RocketMessageProducer 生产者接口，支持多版本集成，根据配置版本自动适配

适用用途：    
1、公司测试和生产环境不一致(节约资源),支持多版本 例如：测试环境自建开源RocketMQ，线上环境阿里云    
2、根据公司技术选型情况抽离

注：这个是一个持续集成优化过程，需要不停磨练

### 三、yaml 配置示例
```yaml
tower:
  rocket:
    namesrvAddr: rocker实例地址
    accessKey: accessKey
    secretKey: accessKey
    packageName: com.hxl
    versions: 4
```

### 四、根据RocketConsumer注解，动态监听器实现
1、核心逻辑代码:
扫描包注解，根据配置版本号走不同的，消费组创建
```java
        String packageName = properties.getPackageName(); // 扫描包路径
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RocketConsumer.class));
        Set<BeanDefinition> candidates = scanner.findCandidateComponents(packageName);
        for (BeanDefinition beanDefinition : candidates) {
            try {
                Integer versions = properties.getVersions();
                if (Objects.equals(RockerMqVersions.ALI_4.getCode(), versions)) {
                    onsRocketMQConsumerClient.start(beanDefinition);
                }

                if (Objects.equals(RockerMqVersions.ALI_5.getCode(), versions)) {
                    rocketMQConsumerClient.start(beanDefinition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
```

2、消费组push 用法示例
版本4.* 写法
```java
@Slf4j
@Component
@RocketConsumer(topic = "PRODUCER_TOPIC", consumerGroup = "PRODUCER_GROUP")
public class Push4MQConsumer implements MessageListener {

    @Autowired
    private UserService userService;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        String body = new String(message.getBody());
        log.info("TestMQConsumer:" + body + "user:" + userService.getUserId());
        return Action.CommitMessage;
    }
}
```
版本5.* 写法
```java
@Slf4j
@Component
@RocketConsumer(topic = "PRODUCER_TOPIC", consumerGroup = "PRODUCER_GROUP")
public class TestMQConsumer implements MessageListener {

    @Autowired
    private UserService userService;

    @Override
    public ConsumeResult consume(MessageView messageView) {
        String body = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();
        log.info("TestMQConsumer:" + body + "user:" + userService.getUserId());
        return ConsumeResult.SUCCESS;
    }
}
```
### 五、RocketMessageProducer 生产发送
目前只写5.* 写法,后面持续优化
普通消息
```java
RocketMsg rocketMsg = new RocketMsg();
rocketMsg.setBody("hxl测试发送");
rocketMessageProducer.sendMessage("PRODUCER_TOPIC", null, rocketMsg);
return ApiResult.success();
```
延迟消息
```java
RocketMsg rocketMsg = new RocketMsg();
rocketMsg.setBody("延迟消息发送发送");
rocketMessageProducer.sendMessage("DELAY_TOPIC", null, 5 * 60L, rocketMsg);
```






