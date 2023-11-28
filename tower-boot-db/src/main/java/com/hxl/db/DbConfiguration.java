package com.hxl.db;

import com.hxl.db.config.MapperScannerConfiguration;
import com.hxl.db.config.MybatisConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

/**
 * @Author hxl
 * @Description 加载
 * 1、MapperScannerConfiguration mapper包扫描包
 * 2、MybatisConfiguration 引用MybatisPlus 插件
 * 3、@EnableTransactionManagement 开启声明式事务
 * @Date 2023/11/28 10:42
 **/
@Slf4j
@Import({
        MapperScannerConfiguration.class,
        MybatisConfiguration.class,
})
@EnableTransactionManagement
@AutoConfigureBefore(name = {"com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration"})
public class DbConfiguration {

    @PostConstruct
    public void init() {
        log.info("enabled  database");
    }
}
