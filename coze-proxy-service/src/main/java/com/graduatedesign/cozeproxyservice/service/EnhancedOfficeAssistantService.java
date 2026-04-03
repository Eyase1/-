package com.graduatedesign.cozeproxyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EnhancedOfficeAssistantService {

    @Autowired
    private OfficeAssistantService officeAssistantService;

    @Autowired
    private CacheService cacheService;

    public String processOfficeRequestWithCache(String message, String userId) {
        // 生成缓存键
        String cacheKey = CacheService.generateChatCacheKey(userId, message);

        // 尝试从缓存获取
        Object cachedResponse = cacheService.get(cacheKey);
        if (cachedResponse != null) {
            log.info("缓存命中: userId={}, message={}", userId, message);
            return (String) cachedResponse;
        }

        // 缓存未命中，处理请求
        log.info("缓存未命中，处理新请求: userId={}, message={}", userId, message);
        String response = officeAssistantService.processOfficeRequest(message, userId);

        // 缓存结果（只缓存成功的、可复用的响应）
        if (shouldCacheResponse(response)) {
            cacheService.set(cacheKey, response, 1800); // 缓存30分钟
            log.debug("响应已缓存: key={}", cacheKey);
        }

        return response;
    }

    private boolean shouldCacheResponse(String response) {
        // 不缓存错误响应和个性化响应
        return !response.contains("抱歉") &&
                !response.contains("错误") &&
                !response.contains("user_") &&
                response.length() > 10; // 只缓存有意义的响应
    }
}