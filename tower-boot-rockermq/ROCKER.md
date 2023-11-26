## 开源RocketMQ和阿里云rockerMq 4.x和5.x集成

### 一、云平台创建实例
参考文档：阿里云rocketmq

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

### 三、根据RocketConsumer消费组注解，动态监听器实现




