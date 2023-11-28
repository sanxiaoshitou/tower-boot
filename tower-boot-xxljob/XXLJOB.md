# tower-boot-xxljob xxlJob组件

## xxljob 安装介绍

下载地址:    
github:[https://github.com/xuxueli/xxl-job/tree/2.3.0](https://github.com/xuxueli/xxl-job/tree/2.3.0)    
gitee: [https://gitee.com/xuxueli0323/xxl-job/tree/2.3.0/](https://gitee.com/xuxueli0323/xxl-job/tree/2.3.0/)


1、创建db, 目录: /doc/db/tables_xxl_job.sql    
2、启动xxl-job-admin,登录界面 http://localhost:8080/xxl-job-admin 账号: admin 密码: 123456
3、设置执行器，设置任务

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
