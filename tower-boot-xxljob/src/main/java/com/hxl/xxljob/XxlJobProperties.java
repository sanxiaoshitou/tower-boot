package com.hxl.xxljob;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @Author hxl
 * @description
 * @Date 2023-06-09 16:35
 **/
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties implements InitializingBean, EnvironmentAware {

    /**
     * 调度中心部署跟地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔。 执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
     */
    private String addresses;

    private Environment environment;

    private XxlExecutorProperties executor = new XxlExecutorProperties();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 若是没有设置appname 则取 application Name
        if (!StringUtils.hasText(executor.getAppname())) {
            executor.setAppname(environment.getProperty("spring.application.name"));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
