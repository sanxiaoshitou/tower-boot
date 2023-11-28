package com.hxl.db.config;
import com.hxl.db.constants.DbConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.util.StringUtils;

/**
 * @Author hxl
 * @Description mapper 配置扫描包 mybatis-plus.base-package
 * @Date 2023/11/28 10:55
 **/
@Slf4j
public class MapperScannerConfiguration implements EnvironmentAware {

    private String mapperPackage;

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //设置sqlSession工厂
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        //设置mapper接口扫码包
        String basePackage= DbConstants.MYBATIS_BASE_PACKAGE;
        if(!StringUtils.isEmpty(mapperPackage)){
            //设置默认值
            basePackage += ","+mapperPackage;
        }
        log.info("mybatis basePackage:{}",basePackage);
        mapperScannerConfigurer.setBasePackage(basePackage);
        return mapperScannerConfigurer;
    }

    @Override
    public void setEnvironment(Environment environment) {
        String property = environment.getProperty("mybatis-plus.base-package");
        //String placeholders = environment.resolvePlaceholders("${mybatis-plus.base-package}");
        if(StringUtils.isEmpty(property)){
            property=null;
        }
        mapperPackage=property;
    }
}
