# tower-boot-db MybatisPlus 集成

## 模块说明
MybatisPlus 简单集成

注：后面陆续增加其他内容

## 使用说明

### 一、yml配置
spring.datasource p配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    url: jdbc:mysql://${spring.datasource.ip}:${spring.datasource.port}/${spring.datasource.database}?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false&autoReconnect=true
    ip: 127.0.0.1
    port: 3306
    database: demo
    username: 123456
    password: 123456
    # 连接池的配置信息
    druid:
      # 初始化大小，最小，最大
      initial-size: 2
      min-idle: 1
      maxActive: 5
      # 配置获取连接等待超时的时间
      maxWait: 60000
      # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
      timeBetweenEvictionRunsMillis: 60000
      # 配置一个连接在池中最小生存的时间，单位是毫秒
      minEvictableIdleTimeMillis: 600000
      # 配置一个连接在池中最大生存的时间，单位是毫秒
      maxEvictableIdleTimeMillis: 900000
      connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500;druid.mysql.usePingMethod=false
```
mybatis-plus 配置
```yaml
mybatis-plus:
  #扫mapper包路径类似做到 @MapperScan("com.hxl.starter.mapper")
  base-package: com.hxl.starter.mapper.**
  mapper-locations: classpath:mapping/*.xml
  configuration:
    #开启自动驼峰命名规则（camel case）映射
    map-underscore-to-camel-case: true
    #延迟加载，需要和lazy-loading-enabled一起使用
    aggressive-lazy-loading: true
    lazy-loading-enabled: true
    #关闭一级缓存
    local-cache-scope: statement
    #关闭二级级缓存
    cache-enabled: false
```
### 二、mapper、service、po 写法示例
UserMapper
```java
public interface UserMapper extends BaseMapper<UserPo> {

}
```
service
```java
public interface UserService extends IService<UserPo> {

    Long getUserId();
}
```
ServiceImpl
```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {

    @Override
    public Long getUserId() {
        LambdaQueryWrapper<UserPo> wrapper = Wrappers.lambdaQuery();
        List<UserPo> list = list(wrapper);
        return CollectionUtil.isNotEmpty(list) ? list.get(0).getId() : 0L;
    }
}
```
po 支持注解
```java
@Getter
@Setter
@ToString(callSuper = true)
@TableName("user")
public class UserPo {

    /**
     * id
     */
    @TableId("id")
    private Integer id;


    /**
     * 账号
     */
    @TableField("user_name")
    private String userName;

    /**
     * 密码
     */
    @TableField("pass_word")
    private String passWord;

}
```
