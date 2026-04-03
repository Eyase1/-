package com.graduatedesign.cozeproxyservice.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "coze")
public class CozeConfig {
    private Api api = new Api();

    @PostConstruct
    public void init() {
        log.info("========== Coze配置加载 ==========");
        if (api != null) {
            log.info("baseUrl: {}", api.getBaseUrl());
            log.info("accessToken: {}...",
                    api.getAccessToken() != null && api.getAccessToken().length() > 10
                            ? api.getAccessToken().substring(0, 10) : "null");
            log.info("url: {}", api.getUrl());
        } else {
            log.warn("Coze API配置为空！");
        }
        log.info("==================================");
    }

    @Data
    public static class Api {
        private String baseUrl;
        private String accessToken;  // 对应配置中的 access-token
        private String url;
        private BotId botId = new BotId();
    }

    @Data
    public static class BotId {
        private String officeAssistant;
        private String docAnalyst;
        private String calendarParser;
    }
}