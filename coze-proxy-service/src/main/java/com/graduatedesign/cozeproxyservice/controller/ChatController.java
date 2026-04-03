package com.graduatedesign.cozeproxyservice.controller;

import com.graduatedesign.commonmodule.dto.ApiResponse;
import com.graduatedesign.cozeproxyservice.util.ChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Coze智能体对话控制器
 * 提供HTTP接口用于与Coze智能体进行简单对话交互
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatUtil chatUtil;
    
    // 构造函数注入
    @Autowired
    public ChatController(ChatUtil chatUtil) {
        this.chatUtil = chatUtil;
    }

    /**
     * 与智能体进行对话的接口（支持多轮对话）
     * @param request 包含用户消息和对话ID的请求体
     * @return 智能体的回复
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendMessage(@RequestBody ChatRequest request) {
        try {
            // 验证请求参数
            if (request == null || request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                ApiResponse<String> response = new ApiResponse<>();
                response.setCode(400);
                response.setMessage("消息内容不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 使用对话ID，默认为default-conversation
            String conversationId = request.getConversationId() != null ? 
                                    request.getConversationId() : "default-conversation";
            
            // 调用ChatUtil与智能体对话
            String agentResponse = chatUtil.chatWithAgent(request.getMessage(), conversationId);
            
            // 使用ApiResponse构建成功响应
            return ResponseEntity.ok(ApiResponse.success(agentResponse));
        } catch (Exception e) {
            // 异常处理
            ApiResponse<String> response = new ApiResponse<>();
            response.setCode(500);
            response.setMessage("对话失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 清空对话历史的接口
     * @param conversationId 对话ID
     * @return 操作结果
     */
    @DeleteMapping("/conversation/{conversationId}")
    public ResponseEntity<ApiResponse<String>> clearConversation(@PathVariable String conversationId) {
        try {
            chatUtil.clearConversationHistory(conversationId);
            return ResponseEntity.ok(ApiResponse.success("对话历史已清空"));
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>();
            response.setCode(500);
            response.setMessage("清空对话历史失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 获取配置信息的接口（用于调试）
     * @return 当前配置信息
     */
    @GetMapping("/config")
    public ResponseEntity<ApiResponse<Map<String, String>>> getConfigInfo() {
        try {
            Map<String, String> configInfo = chatUtil.getConfigInfo();
            return ResponseEntity.ok(ApiResponse.success(configInfo));
        } catch (Exception e) {
            ApiResponse<Map<String, String>> response = new ApiResponse<>();
            response.setCode(500);
            response.setMessage("获取配置信息失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 聊天请求DTO类
     */
    static class ChatRequest {
        private String message;
        private String conversationId;
        
        // Getter and Setter
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getConversationId() {
            return conversationId;
        }
        
        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }
    }
}