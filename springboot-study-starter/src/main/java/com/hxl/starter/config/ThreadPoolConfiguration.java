package com.hxl.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author hxl
 * @description
 * @Date 2023-11-13 14:25
 **/
@Configuration
public class ThreadPoolConfiguration {

    /**
     * spring 线程池
     *
     * @return
     */
    @Bean("defaultThreadPool")
    public ThreadPoolTaskExecutor executor() {
        int coreCount = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(coreCount * 2);
        // 设置最大线程数
        executor.setMaxPoolSize(coreCount * 4);
        // 设置队列容量
        executor.setQueueCapacity(coreCount * 50);
        // 设置线程饱和时间（秒）
        executor.setKeepAliveSeconds(300);
        // 设置默认线程名称
        executor.setThreadNamePrefix("default-Thread-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 关机时优雅下线，等待线程执行完成再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 关机时设置优雅下线的最长等待时间
        executor.setAwaitTerminationSeconds(60);
        executor.afterPropertiesSet();
        return executor;
    }

    /**
     * java 线程池
     *
     * @return
     */
    @Bean("jdkDefaultThreadPool")
    public ThreadPoolExecutor jdkExecutor() {
        int coreCount = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                coreCount * 4,
                coreCount * 8,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                ((r) -> new Thread(r, "jdk-thread-" + r.hashCode())),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
