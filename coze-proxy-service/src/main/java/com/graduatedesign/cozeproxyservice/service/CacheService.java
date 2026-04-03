package com.graduatedesign.cozeproxyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CacheService {

    // 内存缓存存储
    private final Map<String, CacheEntry> memoryCache = new ConcurrentHashMap<>();
    private static final long DEFAULT_TTL = 3600; // 1小时

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        set(key, value, DEFAULT_TTL);
    }

    /**
     * 设置带过期时间的缓存
     */
    public void set(String key, Object value, long ttlSeconds) {
        long expireTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        memoryCache.put(key, new CacheEntry(value, expireTime));
        log.debug("缓存设置: key={}, ttl={}s", key, ttlSeconds);
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        CacheEntry entry = memoryCache.get(key);
        if (entry != null) {
            if (System.currentTimeMillis() > entry.getExpireTime()) {
                // 缓存过期，自动删除
                memoryCache.remove(key);
                log.debug("缓存过期: key={}", key);
                return null;
            }
            log.debug("缓存命中: key={}", key);
            return entry.getValue();
        }
        log.debug("缓存未命中: key={}", key);
        return null;
    }

    /**
     * 删除缓存
     */
    public boolean delete(String key) {
        boolean existed = memoryCache.remove(key) != null;
        log.debug("缓存删除: key={}, existed={}", key, existed);
        return existed;
    }

    /**
     * 清空所有缓存
     */
    public void clear() {
        int size = memoryCache.size();
        memoryCache.clear();
        log.debug("缓存清空: 清除了{}个条目", size);
    }

    /**
     * 获取缓存大小
     */
    public int getCacheSize() {
        return memoryCache.size();
    }

    /**
     * 清理过期缓存
     */
    public void cleanupExpired() {
        int beforeSize = memoryCache.size();
        memoryCache.entrySet().removeIf(entry ->
                System.currentTimeMillis() > entry.getValue().getExpireTime()
        );
        int afterSize = memoryCache.size();
        log.debug("过期缓存清理: {} -> {} (清理了{}个)", beforeSize, afterSize, beforeSize - afterSize);
    }

    // ==================== 缓存键生成方法 ====================

    public static String generateChatCacheKey(String userId, String message) {
        return "chat:" + userId + ":" + Integer.toHexString(message.hashCode());
    }

    public static String generateDocumentCacheKey(String docId, String operation) {
        return "doc:" + docId + ":" + operation;
    }

    public static String generateUserSessionKey(String userId) {
        return "session:" + userId;
    }

    public static String generateTaskCacheKey(String userId, String taskType) {
        return "task:" + userId + ":" + taskType;
    }

    // ==================== 内部缓存条目类 ====================

    private static class CacheEntry {
        private final Object value;
        private final long expireTime;

        public CacheEntry(Object value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        public Object getValue() {
            return value;
        }

        public long getExpireTime() {
            return expireTime;
        }
    }
}