package com.graduatedesign.cozeproxyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CozeProxyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CozeProxyServiceApplication.class, args);
    }
}