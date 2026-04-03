package com.graduatedesign.apigateway.controller;
import com.graduatedesign.apigateway.service.DashboardProducerService;
import com.graduatedesign.apigateway.model.DashboardResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/dashboard")
@Slf4j
public class DashboardController {
    @Autowired private DashboardProducerService dashboardProducerService;

    @GetMapping("/async/full")
    public CompletableFuture<ResponseEntity<?>> getFullDashboardData(@RequestHeader("X-User-Id") Long userId) {
        log.info("获取完整仪表板数据 - 用户: {}", userId);
        CompletableFuture<DashboardResult> userStatsFuture = dashboardProducerService.requestUserStats(userId);
        CompletableFuture<DashboardResult> docStatsFuture = dashboardProducerService.requestDocStats(userId);
        CompletableFuture<DashboardResult> scheduleStatsFuture = dashboardProducerService.requestScheduleStats(userId);
        CompletableFuture<DashboardResult> cozeStatsFuture = dashboardProducerService.requestCozeStats(userId);

        return CompletableFuture.allOf(userStatsFuture, docStatsFuture, scheduleStatsFuture, cozeStatsFuture).thenApply(v -> {
            Map<String, Object> result = new HashMap<>();
            try {
                result.put("userStats", getDataOrFallback(userStatsFuture));
                result.put("documentStats", getDataOrFallback(docStatsFuture));
                result.put("scheduleStats", getDataOrFallback(scheduleStatsFuture));
                result.put("cozeStats", getDataOrFallback(cozeStatsFuture));
                result.put("timestamp", System.currentTimeMillis());
                result.put("success", true);
                return ResponseEntity.ok(result);
            } catch (Exception e) {
                log.error("处理数据失败", e);
                return ResponseEntity.ok(getFallbackData());
            }
        });
    }

    private Object getDataOrFallback(CompletableFuture<DashboardResult> future) {
        try { DashboardResult result = future.get(); return result.isSuccess() ? result.getData() : createFallbackData(result.getModule()); }
        catch (Exception e) { return createFallbackData("unknown"); }
    }

    private Object createFallbackData(String module) { return Map.of("module", module, "fallback", true, "message", "数据加载中"); }

    private Map<String, Object> getFallbackData() {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("userStats", createFallbackData("user_stats"));
        fallback.put("documentStats", createFallbackData("doc_stats"));
        fallback.put("scheduleStats", createFallbackData("schedule_stats"));
        fallback.put("cozeStats", createFallbackData("coze_stats"));
        fallback.put("timestamp", System.currentTimeMillis());
        fallback.put("fallback", true);
        return fallback;
    }
}