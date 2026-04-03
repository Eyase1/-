package com.graduatedesign.apigateway.service;
import com.graduatedesign.apigateway.model.DashboardMessage;
import com.graduatedesign.apigateway.model.DashboardResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class DashboardProducerService {
    @Autowired private RabbitTemplate rabbitTemplate;
    @Value("${app.rabbitmq.timeout:30000}") private long timeout;
    private final Map<String, CompletableFuture<DashboardResult>> pendingRequests = new ConcurrentHashMap<>();

    public CompletableFuture<DashboardResult> requestUserStats(Long userId) { return sendAsyncRequest("user_stats", userId, "dashboard.user.stats"); }
    public CompletableFuture<DashboardResult> requestDocStats(Long userId) { return sendAsyncRequest("doc_stats", userId, "dashboard.doc.stats"); }
    public CompletableFuture<DashboardResult> requestScheduleStats(Long userId) { return sendAsyncRequest("schedule_stats", userId, "dashboard.schedule.stats"); }
    public CompletableFuture<DashboardResult> requestCozeStats(Long userId) { return sendAsyncRequest("coze_stats", userId, "dashboard.coze.stats"); }

    private CompletableFuture<DashboardResult> sendAsyncRequest(String module, Long userId, String routingKey) {
        String messageId = UUID.randomUUID().toString();
        CompletableFuture<DashboardResult> future = new CompletableFuture<>();
        DashboardMessage message = new DashboardMessage(messageId, userId, module, Map.of("userId", userId), Instant.now(), "dashboard.results");
        pendingRequests.put(messageId, future);
        CompletableFuture.runAsync(() -> { try { Thread.sleep(timeout); if (!future.isDone()) { future.complete(new DashboardResult(messageId, module, null, false, "请求超时", Instant.now())); pendingRequests.remove(messageId); } } catch (InterruptedException e) { Thread.currentThread().interrupt(); } });
        try { rabbitTemplate.convertAndSend("dashboard.exchange", routingKey, message); log.info("发送请求: {} - 用户: {}", module, userId); } catch (Exception e) { future.complete(new DashboardResult(messageId, module, null, false, "发送失败: " + e.getMessage(), Instant.now())); pendingRequests.remove(messageId); }
        return future;
    }

    public void completeRequest(DashboardResult result) { CompletableFuture<DashboardResult> future = pendingRequests.remove(result.getMessageId()); if (future != null) future.complete(result); }
}