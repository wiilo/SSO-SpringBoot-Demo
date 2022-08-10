package com.wiilo.ssodemo.ssocertification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.wiilo.ssodemo")
@ComponentScan(basePackages = {"com.wiilo.ssodemo", "com.wiilo.common"})
public class SsoCertificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoCertificationApplication.class, args);
    }

}
