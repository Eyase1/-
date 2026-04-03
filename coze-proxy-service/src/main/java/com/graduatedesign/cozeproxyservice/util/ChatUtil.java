package com.graduatedesign.cozeproxyservice.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Coze智能体调用工具类
 * 用于简单访问Coze API并获取智能体回复
 */
@Component
public class ChatUtil {

    private static final Logger logger = LoggerFactory.getLogger(ChatUtil.class);
    
    @Value("${coze.api.url:https://api.coze.cn/open_api/v2/chat}")
    private String cozeApiUrl;
    
    @Value("${coze.api.key:your_api_key_here}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    // 用于存储对话历史
    private final Map<String, List<Map<String, String>>> conversationHistory = new HashMap<>();
    
    // 构造函数，通过Spring注入
    public ChatUtil(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 与智能体进行对话（实例方法，Spring管理的类使用）
     * @param message 用户发送的消息
     * @return 智能体的回复
     */
    public String chat(String message) {
        return chatWithAgent(message, "default-conversation");
    }
    
    /**
     * 与智能体进行对话，支持多轮对话
     * @param message 用户发送的消息
     * @param conversationId 对话ID，用于标识不同的对话
     * @return 智能体的回复
     */
    public String chatWithAgent(String message, String conversationId) {
        try {
            logger.info("开始与智能体对话，消息内容: {}", message);
            
            // 创建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // 准备消息列表
            List<Map<String, String>> messages = getOrCreateConversationHistory(conversationId);
            
            // 添加用户消息
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", message);
            messages.add(userMessage);
            
            // 创建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo"); // 示例模型，实际需要根据Coze API要求设置
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 1000);

            // 创建请求实体
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity(cozeApiUrl, requestEntity, String.class);

            // 处理响应
            if (response.getStatusCode().is2xxSuccessful()) {
                // 解析响应内容，提取智能体回复
                String agentResponse = parseResponse(response.getBody());
                
                // 添加智能体回复到对话历史
                Map<String, String> assistantMessage = new HashMap<>();
                assistantMessage.put("role", "assistant");
                assistantMessage.put("content", agentResponse);
                messages.add(assistantMessage);
                
                // 限制对话历史长度，避免过长
                limitConversationHistory(messages);
                
                logger.info("智能体对话完成，获得回复");
                return agentResponse;
            } else {
                String errorMsg = "API调用失败: " + response.getStatusCodeValue() + " - " + response.getBody();
                logger.error(errorMsg);
                return errorMsg;
            }
        } catch (Exception e) {
            String errorMsg = "调用智能体时发生异常: " + e.getMessage();
            logger.error(errorMsg, e);
            return errorMsg;
        }
    }
    

    
    /**
     * 获取或创建对话历史
     */
    private List<Map<String, String>> getOrCreateConversationHistory(String conversationId) {
        return conversationHistory.computeIfAbsent(conversationId, k -> new ArrayList<>());
    }
    
    /**
     * 限制对话历史长度
     */
    private void limitConversationHistory(List<Map<String, String>> messages) {
        // 保留最近10条消息（5轮对话）
        while (messages.size() > 10) {
            messages.remove(0);
        }
    }

    /**
     * 解析API响应，提取智能体回复
     * @param responseBody API响应体
     * @return 解析后的智能体回复内容
     */
    private String parseResponse(String responseBody) {
        try {
            // 使用JsonNode进行更灵活的解析
            JsonNode rootNode = objectMapper.readTree(responseBody);
            
            // 尝试几种常见的响应格式
            // 1. 尝试解析OpenAI格式的响应
            if (rootNode.has("choices") && rootNode.get("choices").isArray() && !rootNode.get("choices").isEmpty()) {
                JsonNode choiceNode = rootNode.get("choices").get(0);
                if (choiceNode.has("message") && choiceNode.get("message").has("content")) {
                    return choiceNode.get("message").get("content").asText();
                }
            }
            
            // 2. 尝试直接获取content字段
            if (rootNode.has("content")) {
                return rootNode.get("content").asText();
            }
            
            // 3. 尝试获取reply字段
            if (rootNode.has("reply")) {
                return rootNode.get("reply").asText();
            }
            
            // 4. 尝试获取text字段
            if (rootNode.has("text")) {
                return rootNode.get("text").asText();
            }
            
            // 如果都不匹配，返回原始响应（用于调试）
            logger.warn("无法解析响应格式，返回原始响应");
            return responseBody;
        } catch (Exception e) {
            logger.error("解析响应失败", e);
            return "解析响应失败: " + e.getMessage();
        }
    }
    
    /**
     * 清空对话历史
     * @param conversationId 对话ID
     */
    public void clearConversationHistory(String conversationId) {
        if (conversationHistory.containsKey(conversationId)) {
            conversationHistory.get(conversationId).clear();
        }
    }
    
    /**
     * 获取当前配置信息（用于调试）
     * @return 系统配置信息
     */
    public Map<String, String> getConfigInfo() {
        Map<String, String> config = new HashMap<>();
        config.put("apiUrl", cozeApiUrl);
        config.put("apiKeySet", apiKey != null && !apiKey.equals("your_api_key_here") ? "Yes" : "No");
        return config;
    }
}