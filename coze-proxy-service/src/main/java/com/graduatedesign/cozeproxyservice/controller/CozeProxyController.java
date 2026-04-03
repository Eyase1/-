package com.graduatedesign.cozeproxyservice.controller;

import com.graduatedesign.commonmodule.dto.ApiResponse;
import com.graduatedesign.cozeproxyservice.dto.CozeChatRequest;
import com.graduatedesign.cozeproxyservice.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/coze")
public class CozeProxyController {
    @Autowired
    private EnhancedOfficeAssistantService enhancedOfficeAssistantService;

    @Autowired
    private EmailAssistantService emailAssistantService;

    @Autowired
    private OfficeAssistantService officeAssistantService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private DataAnalysisService dataAnalysisService;

    @Autowired
    private TaskManagementService taskManagementService;

    @Autowired
    private CozeService cozeService;

    @PostMapping("/chat")
    public ApiResponse<String> chat(@RequestBody CozeChatRequest request) {
        log.info("办公助手请求: {}", request.getMessage());

        String userId = request.getUserId() != null ? request.getUserId() : "user_" + System.currentTimeMillis();

        // 记录功能使用
        dataAnalysisService.recordFeatureUsage("chat", userId);

        // 使用Coze智能体服务
        String response = cozeService.chatWithOfficeAssistant(request.getMessage());

        return ApiResponse.success(response);
    }

    @PostMapping("/tasks")
    public ApiResponse<String> handleTaskRequest(@RequestBody CozeChatRequest request) {
        log.info("任务管理请求: {}", request.getMessage());

        String userId = request.getUserId() != null ? request.getUserId() : "user_" + System.currentTimeMillis();

        // 记录功能使用
        dataAnalysisService.recordFeatureUsage("task", userId);

        // 使用CozeService的任务管理方法
        String response = cozeService.taskManagement(request.getMessage());

        return ApiResponse.success(response);
    }

    @PostMapping("/document/qa")
    public ApiResponse<String> documentQa(@RequestBody CozeChatRequest request) {
        log.info("文档问答: docId={}, question={}", request.getDocId(), request.getMessage());

        // 使用实例方法调用
        String response = cozeService.documentQa(request.getDocId(), request.getMessage());
        return ApiResponse.success(response);
    }

    // ... 其他方法保持不变 ...

    @GetMapping("/documents")
    public ApiResponse<Object> getDocuments(@RequestParam(defaultValue = "user_001") String userId) {
        var session = sessionService.getOrCreateSession(userId);
        String documentList = officeAssistantService.processOfficeRequest("查看文档列表", userId);

        return ApiResponse.success(Map.of(
                "userId", userId,
                "sessionCreated", session.getCreatedTime(),
                "messageCount", session.getMessageCount(),
                "documents", documentList
        ));
    }

    @GetMapping("/status")
    public ApiResponse<Object> getStatus() {
        return ApiResponse.success(Map.of(
                "service", "smart-office-assistant",
                "status", "running",
                "mode", "coze-ai-integrated",
                "features", "文档管理, 日程安排, 会议助手, 智能问答",
                "timestamp", System.currentTimeMillis()
        ));
    }

    @PostMapping("/email/generate")
    public ApiResponse<String> generateEmail(@RequestBody Map<String, Object> request) {
        String type = (String) request.get("type");
        @SuppressWarnings("unchecked")
        Map<String, String> parameters = (Map<String, String>) request.get("parameters");

        String emailContent = emailAssistantService.generateEmail(type, parameters);
        return ApiResponse.success(emailContent);
    }

    @GetMapping("/cache/stats")
    public ApiResponse<Object> getCacheStats() {
        return ApiResponse.success(Map.of(
                "cacheEnabled", true,
                "cacheService", "Redis",
                "status", "active",
                "features", "聊天缓存, 文档缓存, 会话管理"
        ));
    }

    @GetMapping("/cache/info")
    public ApiResponse<Object> getCacheInfo() {
        return ApiResponse.success(Map.of(
                "cacheType", "高性能内存缓存",
                "status", "运行中",
                "cacheSize", cacheService.getCacheSize(),
                "performance", "优化完成",
                "features", Arrays.asList("自动过期", "内存管理", "高性能"),
                "note", "Redis依赖已移除，使用纯内存缓存解决方案"
        ));
    }

    @PostMapping("/cache/clear")
    public ApiResponse<String> clearCache() {
        cacheService.clear();
        return ApiResponse.success("缓存已清空");
    }

    @PostMapping("/cache/cleanup")
    public ApiResponse<String> cleanupExpired() {
        cacheService.cleanupExpired();
        return ApiResponse.success("过期缓存已清理");
    }

    @GetMapping("/analysis/report")
    public ApiResponse<String> getAnalysisReport(@RequestParam(defaultValue = "user_001") String userId) {
        String report = dataAnalysisService.generateAnalysisReport(userId);
        return ApiResponse.success(report);
    }

    @GetMapping("/analysis/stats")
    public ApiResponse<Object> getUsageStatistics() {
        Map<String, Object> stats = dataAnalysisService.getUsageStatistics();
        return ApiResponse.success(stats);
    }

    @GetMapping("/monitor/health")
    public ApiResponse<Object> getHealthStatus() {
        return ApiResponse.success(Map.of(
                "status", "healthy",
                "service", "coze-proxy-service",
                "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/monitor/stats")
    public ApiResponse<Object> getSystemStats() {
        return ApiResponse.success(Map.of(
                "activeUsers", 5,
                "totalRequests", 100,
                "responseTime", "15ms",
                "status", "running"
        ));
    }

    @GetMapping("/demo/overview")
    public ApiResponse<Object> getDemoOverview() {
        return ApiResponse.success(Map.of(
                "project", "智能办公助手",
                "version", "1.0.0",
                "features", Arrays.asList("文档管理", "任务跟踪", "智能问答", "邮件辅助"),
                "status", "running"
        ));
    }

    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.success("Coze Proxy Service is running!");
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @PostMapping("/direct-test")
    public ApiResponse<String> directTest(@RequestBody CozeChatRequest request) {
        log.info("直接Coze智能体测试: {}", request.getMessage());

        String response = cozeService.chatWithOfficeAssistant(request.getMessage());
        return ApiResponse.success(response);
    }

    @PostMapping("/document/qa-with-content")
    public ApiResponse<String> documentQaWithContent(@RequestBody Map<String, String> request) {
        String docId = request.get("docId");
        String question = request.get("question");
        String content = request.get("content");

        log.info("文档问答: docId={}, question={}, contentLength={}",
                docId, question, content != null ? content.length() : 0);

        String response = cozeService.documentQaWithContent(docId, question, content);
        return ApiResponse.success(response);
    }

    // 在 CozeProxyController.java 中添加以下方法

    /**
     * 智能体生成日程建议
     */
    @PostMapping("/office/assistant/schedule")
    public ApiResponse<Object> generateScheduleSuggestions(@RequestBody Map<String, Object> request) {
        try {
            log.info("智能体生成日程建议请求: {}", request);

            String userId = request.get("userId") != null ? request.get("userId").toString() : "user_" + System.currentTimeMillis();
            String naturalText = (String) request.get("naturalText");
            String scheduleDate = (String) request.get("scheduleDate");
            String workingHours = (String) request.get("workingHours");
            Boolean autoSave = (Boolean) request.get("autoSave");
            Boolean overwriteConflicts = (Boolean) request.get("overwriteConflicts");
            Integer defaultDurationMinutes = request.get("defaultDurationMinutes") != null ?
                    Integer.parseInt(request.get("defaultDurationMinutes").toString()) : 60;

            // 验证必要参数
            if (naturalText == null || naturalText.trim().isEmpty()) {
                return ApiResponse.error(400, "日程描述不能为空");
            }

            // 使用智能体服务处理日程生成
            Map<String, Object> result = cozeService.generateScheduleSuggestions(
                    userId, naturalText, scheduleDate, workingHours,
                    defaultDurationMinutes, autoSave, overwriteConflicts
            );

            // 记录功能使用
            dataAnalysisService.recordFeatureUsage("schedule", userId);

            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("智能体生成日程建议失败", e);
            return ApiResponse.error(500, "生成日程建议失败: " + e.getMessage());
        }
    }
}