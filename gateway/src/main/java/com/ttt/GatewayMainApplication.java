package com.ttt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayMainApplication {
    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println(arg);
        }
        SpringApplication.run(GatewayMainApplication.class,args);
    }
}
