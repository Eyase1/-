package com.graduatedesign.cozeproxyservice.controller;

import com.graduatedesign.commonmodule.dto.ApiResponse;
import com.graduatedesign.cozeproxyservice.config.CozeConfig;
import com.graduatedesign.cozeproxyservice.service.CozeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private CozeConfig cozeConfig;

    @Autowired
    private CozeService cozeService;

    /**
     * 配置状态检查
     */
    @GetMapping("/config")
    public ApiResponse<Map<String, Object>> getConfigStatus() {
        Map<String, Object> result = new HashMap<>();

        try {
            if (cozeConfig != null && cozeConfig.getApi() != null) {
                result.put("configLoaded", true);
                result.put("baseUrl", cozeConfig.getApi().getBaseUrl());

                String token = cozeConfig.getApi().getAccessToken();
                result.put("tokenConfigured", token != null && !token.isEmpty());
                result.put("tokenLength", token != null ? token.length() : 0);
                result.put("tokenStartsWithPat", token != null && token.startsWith("pat_"));

                if (cozeConfig.getApi().getBotId() != null) {
                    String botId = cozeConfig.getApi().getBotId().getOfficeAssistant();
                    result.put("botId", botId);
                    result.put("botIdConfigured", botId != null && !botId.isEmpty());
                }
            } else {
                result.put("configLoaded", false);
            }

            result.put("status", "success");
            return ApiResponse.success(result);

        } catch (Exception e) {
            log.error("获取配置状态失败", e);
            result.put("status", "error");
            result.put("error", e.getMessage());
            // 修复：创建错误响应
            ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("获取配置状态失败: " + e.getMessage());
            errorResponse.setData(result);
            return errorResponse;
        }
    }

    /**
     * 直接API测试
     */
    @PostMapping("/direct-api-test")
    public ApiResponse<Map<String, Object>> directApiTest(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            message = "这是一次直接API测试，请回复确认收到。";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("testMessage", message);

        try {
            // 使用CozeService进行测试
            String response = cozeService.chatWithOfficeAssistant(message);

            result.put("apiCallSuccess", true);
            result.put("aiResponse", response);
            result.put("timestamp", System.currentTimeMillis());

            return ApiResponse.success(result);

        } catch (Exception e) {
            log.error("直接API测试异常", e);
            result.put("apiCallSuccess", false);
            result.put("error", e.getMessage());
            // 修复：创建错误响应
            ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("直接API测试失败: " + e.getMessage());
            errorResponse.setData(result);
            return errorResponse;
        }
    }

    /**
     * 业务服务测试
     */
    @PostMapping("/service-test")
    public ApiResponse<Map<String, Object>> serviceTest(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            message = "这是一次业务服务测试，请回复确认服务正常。";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("testMessage", message);

        try {
            String response = cozeService.chatWithOfficeAssistant(message);

            result.put("serviceCallSuccess", true);
            result.put("serviceResponse", response);
            result.put("timestamp", System.currentTimeMillis());

            return ApiResponse.success(result);

        } catch (Exception e) {
            log.error("业务服务测试异常", e);
            result.put("serviceCallSuccess", false);
            result.put("error", e.getMessage());
            // 修复：创建错误响应
            ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("业务服务测试失败: " + e.getMessage());
            errorResponse.setData(result);
            return errorResponse;
        }
    }

    /**
     * 完整功能测试
     */
    @GetMapping("/full-test")
    public ApiResponse<Map<String, Object>> fullIntegrationTest() {
        Map<String, Object> result = new HashMap<>();

        try {
            log.info("开始完整集成测试...");

            // 1. 测试配置
            Map<String, Object> configTest = new HashMap<>();
            if (cozeConfig != null && cozeConfig.getApi() != null) {
                configTest.put("status", "success");
                configTest.put("baseUrl", cozeConfig.getApi().getBaseUrl());
                configTest.put("botId", cozeConfig.getApi().getBotId().getOfficeAssistant());
            } else {
                configTest.put("status", "error");
                configTest.put("message", "配置加载失败");
            }
            result.put("configTest", configTest);

            // 2. 测试业务服务
            Map<String, Object> serviceTest = new HashMap<>();
            try {
                String response = cozeService.chatWithOfficeAssistant("完整测试消息");
                serviceTest.put("status", "success");
                serviceTest.put("response", response);
            } catch (Exception e) {
                serviceTest.put("status", "error");
                serviceTest.put("message", e.getMessage());
            }
            result.put("serviceTest", serviceTest);

            // 3. 测试文档问答
            Map<String, Object> docTest = new HashMap<>();
            try {
                String response = cozeService.documentQa("test_doc_001", "这是一个测试问题");
                docTest.put("status", "success");
                docTest.put("response", response);
            } catch (Exception e) {
                docTest.put("status", "error");
                docTest.put("message", e.getMessage());
            }
            result.put("documentQaTest", docTest);

            result.put("overallStatus", "success");
            result.put("timestamp", System.currentTimeMillis());

            log.info("完整集成测试完成");

            return ApiResponse.success(result);

        } catch (Exception e) {
            log.error("完整集成测试失败", e);
            result.put("overallStatus", "error");
            result.put("error", e.getMessage());
            // 修复：创建错误响应
            ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("完整集成测试失败: " + e.getMessage());
            errorResponse.setData(result);
            return errorResponse;
        }
    }

    /**
     * 服务健康检查
     */
    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> healthCheck() {
        Map<String, Object> result = new HashMap<>();

        try {
            result.put("service", "coze-proxy-service");
            result.put("status", "running");
            result.put("cozeConfigLoaded", cozeConfig != null);
            result.put("cozeServiceAvailable", cozeService != null);
            result.put("timestamp", System.currentTimeMillis());

            return ApiResponse.success(result);

        } catch (Exception e) {
            log.error("健康检查失败", e);
            result.put("status", "error");
            result.put("error", e.getMessage());
            // 修复：创建错误响应
            ApiResponse<Map<String, Object>> errorResponse = new ApiResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("健康检查失败: " + e.getMessage());
            errorResponse.setData(result);
            return errorResponse;
        }
    }
}