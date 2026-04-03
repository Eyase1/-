package com.ttt.util.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 验证码缓存管理器
 */
public class VerificationCodeCache {
    private static final Map<String, CodeEntry> cache = new ConcurrentHashMap<>();
    private static final long EXPIRATION_MINUTES = 5; // 验证码有效期5分钟

    /**
     * 存储验证码
     * @param email 邮箱地址
     * @param code 验证码
     */
    public static void put(String email, String code) {
        cache.put(email, new CodeEntry(code, System.currentTimeMillis()));
    }

    /**
     * 验证验证码
     * @param email 邮箱地址
     * @param code 用户输入的验证码
     * @return 是否验证通过
     */
    public static boolean validate(String email, String code) {
        CodeEntry entry = cache.get(email);
        if (entry == null) {
            return false; // 没有找到验证码
        }

        // 检查验证码是否过期
        long currentTime = System.currentTimeMillis();
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - entry.timestamp);
        if (elapsedMinutes > EXPIRATION_MINUTES) {
            cache.remove(email); // 移除过期验证码
            return false;
        }

        // 验证码匹配
        if (entry.code.equals(code)) {
            cache.remove(email); // 验证成功后移除验证码
            return true;
        }

        return false;
    }
    public static boolean validateAndNotRemote(String email, String code) {
        CodeEntry entry = cache.get(email);
        if (entry == null) {
            return false; // 没有找到验证码
        }

        // 检查验证码是否过期
        long currentTime = System.currentTimeMillis();
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - entry.timestamp);
        if (elapsedMinutes > EXPIRATION_MINUTES) {
            cache.remove(email); // 移除过期验证码
            return false;
        }

        // 验证码匹配
        if (entry.code.equals(code)) {
            return true;
        }

        return false;
    }

    /**
     * 验证码条目内部类
     */
    private static class CodeEntry {
        final String code;
        final long timestamp;

        CodeEntry(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }
    }
}
