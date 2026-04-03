package com.ttt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;

@EnableFeignClients
@SpringBootApplication
@MapperScan("com.ttt.mapper")
public class TestaMainApplication {
    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private static String nacosServerAddr;

    public static void main(String[] args) {
        System.out.println(nacosServerAddr);
        SpringApplication.run(TestaMainApplication.class,args);
    }



}
