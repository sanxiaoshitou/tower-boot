package com.hxl.starter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author hxl
 * @description
 * @Date 2023-11-13 13:25
 **/
@SpringBootApplication(scanBasePackages = {"com.hxl.starter"})
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }
}
