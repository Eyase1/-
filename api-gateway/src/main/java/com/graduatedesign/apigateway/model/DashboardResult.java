package com.graduatedesign.apigateway.model;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResult {
    private String messageId;
    private String module;
    private Object data;
    private boolean success;
    private String errorMsg;
    private Instant processedAt;
}