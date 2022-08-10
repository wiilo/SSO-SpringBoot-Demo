package com.wiilo.ssodemo.ssomember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.wiilo.ssodemo.ssomember.feign")
@ComponentScan(basePackages = {"com.wiilo.ssodemo", "com.wiilo.common"})
public class SsoMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoMemberApplication.class, args);
    }

}
