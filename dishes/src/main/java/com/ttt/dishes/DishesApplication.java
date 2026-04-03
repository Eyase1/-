package com.ttt.dishes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@MapperScan("com.ttt.dishes.mapper")
public class DishesApplication {
    public static void main(String[] args) {
        SpringApplication.run(DishesApplication.class, args);
    }

}
