package com.hxl.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author hxl
 * @description
 * @Date 2023-11-13 15:25
 **/
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /**
     * 首页 API 说明
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置标题
                .title("swagger接口文档")
                //描述
                .description("spring boot 学习 ")
                .version("1.0.0")
                .build();
    }


    /**
     * swagger api 访问
     * <a href="http://localhost:8080/swagger-ui.html">Swagger地址</a>
     * http://127.0.0.1:8080//doc.html#/home
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // endpoint 包下的所有类，生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.hxl.starter.controller"))
                .paths(PathSelectors.any())
                .build()
                ;
    }

}
