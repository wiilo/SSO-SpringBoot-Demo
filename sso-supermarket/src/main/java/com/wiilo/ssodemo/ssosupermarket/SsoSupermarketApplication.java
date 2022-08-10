package com.wiilo.ssodemo.ssosupermarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.wiilo.ssodemo", "com.wiilo.common"})
@SpringBootApplication
public class SsoSupermarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoSupermarketApplication.class, args);
    }

}
