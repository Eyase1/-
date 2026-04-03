package com.ttt.util.ai;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个示例类，展示了如何使用ArkService来完成聊天功能。
 */
public class ChatCompletionsUtil {
    public static List<Object> chatCompletion(String content) {
        // 从环境变量中获取API密钥
        String apiKey = System.getenv("ARK_API_KEY");
        List<Object>result=new ArrayList<>();
        // 创建ArkService实例
        ArkService arkService = ArkService.builder().apiKey(apiKey).build();
        
        // 初始化消息列表
        List<ChatMessage> chatMessages = new ArrayList<>();
        
        // 创建用户消息
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER) // 设置消息角色为用户
                .content(content) // 设置消息内容
                .build();
        
        // 将用户消息添加到消息列表
        chatMessages.add(userMessage);
        
        // 创建聊天完成请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("doubao-1.5-vision-lite-250315")// 需要替换为Model ID
                .messages(chatMessages) // 设置消息列表
                .build();
        
        // 发送聊天完成请求并打印响应
        try {
            arkService.createChatCompletion(chatCompletionRequest)
                     .getChoices()
                     .forEach(choice -> result.add(choice.getMessage().getContent()));
        } catch (Exception e) {
            System.out.println("请求失败: " + e.getMessage());
        } finally {
            // 关闭服务执行器
            arkService.shutdownExecutor();
        }
        return result;
    }
}