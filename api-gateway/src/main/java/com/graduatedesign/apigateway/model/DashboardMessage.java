package com.graduatedesign.apigateway.model;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardMessage {
    private String messageId;
    private Long userId;
    private String module;
    private Map<String, Object> params;
    private Instant timestamp;
    private String replyTo;
}