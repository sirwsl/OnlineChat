package com.wsl.web;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableMethodCache(basePackages = {"com.wsl.service.serviceImpl*"})
@EnableCreateCacheAnnotation
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.wsl.common","com.wsl.web.config", "com.wsl.service.*","com.wsl.web.api"
        , "com.wsl.socket.server"})
@MapperScan("com.wsl.dao.mapper")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
