package com.hxl.xxljob;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author hxl
 * @description
 * @Date 2023-06-09 16:43
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = "xxl.job",name = "enabled",matchIfMissing = true)
public class XxlJobAutoConfiguration {

    /**
     * 配置xxl-job 执行器
     * @param xxlJobProperties xxl 配置
     * @return
     */
    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getExecutor().getAppname());
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getExecutor().getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getExecutor().getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAddresses());
        return xxlJobSpringExecutor;
    }
}
