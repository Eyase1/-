package com.graduatedesign.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApiGatewayApplication.class);

        // 添加环境准备监听器
        app.addListeners(event -> {
            if (event instanceof org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent) {
                ConfigurableEnvironment env = ((org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent) event).getEnvironment();

                System.out.println("=== 配置诊断 ===");
                System.out.println("应用名称: " + env.getProperty("spring.application.name"));
                System.out.println("服务器端口: " + env.getProperty("server.port"));
                System.out.println("活跃配置文件: " + Arrays.toString(env.getActiveProfiles()));

                // 打印所有属性源
                System.out.println("=== 属性源列表 ===");
                env.getPropertySources().forEach(propertySource -> {
                    System.out.println("属性源: " + propertySource.getName());
                });
            }
        });

        ConfigurableApplicationContext context = app.run(args);

        // 启动后再次确认配置
        String actualPort = context.getEnvironment().getProperty("server.port");
        String actualAppName = context.getEnvironment().getProperty("spring.application.name");
        System.out.println("=== 最终配置 ===");
        System.out.println("实际应用名称: " + actualAppName);
        System.out.println("实际服务器端口: " + actualPort);
    }
}