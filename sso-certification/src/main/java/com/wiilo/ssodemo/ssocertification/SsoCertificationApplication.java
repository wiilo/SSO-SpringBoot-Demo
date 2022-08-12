package com.wiilo.ssodemo.ssocertification;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.wiilo.ssodemo")
@ComponentScan(basePackages = {"com.wiilo.ssodemo", "com.wiilo.common"})
@MapperScan("com.wiilo.ssodemo.ssocertification.dao")
public class SsoCertificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoCertificationApplication.class, args);
    }

}
