package com.graduatedesign.cozeproxyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SessionService {

    private final Map<String, UserSession> userSessions = new ConcurrentHashMap<>();

    public UserSession getOrCreateSession(String userId) {
        return userSessions.computeIfAbsent(userId, id -> {
            UserSession session = new UserSession();
            session.setUserId(id);
            session.setCreatedTime(System.currentTimeMillis());
            log.info("创建用户会话: {}", id);
            return session;
        });
    }

    public void updateSessionActivity(String userId) {
        UserSession session = userSessions.get(userId);
        if (session != null) {
            session.setLastActivityTime(System.currentTimeMillis());
            session.setMessageCount(session.getMessageCount() + 1);
        }
    }

    public static class UserSession {
        private String userId;
        private long createdTime;
        private long lastActivityTime;
        private int messageCount;

        // getters and setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public long getCreatedTime() { return createdTime; }
        public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
        public long getLastActivityTime() { return lastActivityTime; }
        public void setLastActivityTime(long lastActivityTime) { this.lastActivityTime = lastActivityTime; }
        public int getMessageCount() { return messageCount; }
        public void setMessageCount(int messageCount) { this.messageCount = messageCount; }
    }
}