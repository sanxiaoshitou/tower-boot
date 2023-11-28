# tower-xxljob-module xxlJob组件

## 模块说明
xxlJob应用封装

## 使用说明

### 一、yml配置 示例
executor.appname: 声明执行器名称
```yaml
xxl:
  job:
    addresses: http://127.0.0.1:8090/xxl-job-admin
#    userName: admin
#    password: 123456
    accessToken:
    i18n: zh_CN
    logretentiondays: 30
    triggerpool.fast.max: 200
    triggerpool.slow.max: 100
    executor:
      appname: study
```

### 二、基础用法
```java
@Slf4j
@Component
public class TestJob extends IJobHandler {

    @Override
    @XxlJob("studyTestJob")
    public void execute() throws Exception {
        log.info("执行:" + System.currentTimeMillis());
    }
}
```
