package com.graduatedesign.apigateway.service;
import com.graduatedesign.apigateway.model.DashboardResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardConsumerService {
    @Autowired private DashboardProducerService dashboardProducerService;
    @RabbitListener(queues = "dashboard.results")
    public void handleDashboardResult(DashboardResult result) { log.info("收到结果: {} - 成功: {}", result.getModule(), result.isSuccess()); dashboardProducerService.completeRequest(result); }
}