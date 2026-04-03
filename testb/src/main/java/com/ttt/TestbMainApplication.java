package com.ttt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TestbMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestbMainApplication.class,args);
    }
}
