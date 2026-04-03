package com.graduatedesign.cozeproxyservice.util;

import com.graduatedesign.cozeproxyservice.client.CozeApiClient;
import com.graduatedesign.cozeproxyservice.config.RestTemplateConfig;
import com.graduatedesign.cozeproxyservice.dto.CozeChatResponse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 简单的Coze API测试类，用于验证Coze智能体调用功能
 */
public class SimpleCozeTest {

    public static void main(String[] args) {
        try {
            System.out.println("开始测试Coze API调用...");
            
            // 使用Spring上下文获取CozeApiClient实例
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RestTemplateConfig.class);
            CozeApiClient cozeApiClient = context.getBean(CozeApiClient.class);
            
            // 调用模拟的API
            CozeChatResponse response = cozeApiClient.chatWithBot("7508960118415999013", "你好，请介绍一下自己", "test_user_123");
            
            if (response.isSuccess()) {
                System.out.println("测试成功！AI回复：" + response.getContent());
            } else {
                System.out.println("测试失败：" + response.getMessage());
            }
            
            context.close();
        } catch (Exception e) {
            System.err.println("测试失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}