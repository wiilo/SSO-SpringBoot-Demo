package com.wiilo.ssodemo.ssomember;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.wiilo.ssodemo.ssomember.feign")
@ComponentScan(basePackages = {"com.wiilo.ssodemo", "com.wiilo.common"})
@MapperScan("com.wiilo.ssodemo.ssomember.dao")
public class SsoMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoMemberApplication.class, args);
    }

}
