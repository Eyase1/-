package com.graduatedesign.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class RateLimitConfig {

    /**
     * 基于用户ID的限流解析器 - 主要bean
     * 添加@Primary注解指定为主要bean
     */
    @Bean
    @Primary
    public KeyResolver userKeyResolver() {
        return exchange -> {
            // 从header中获取用户ID
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
            if (userId != null && !userId.trim().isEmpty()) {
                return Mono.just("user_" + userId);
            }

            // 从token中解析用户ID
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                return Mono.just("token_" + token.substring(7, Math.min(20, token.length())));
            }

            // 使用IP地址作为fallback
            String ip = Objects.requireNonNull(exchange.getRequest().getRemoteAddress())
                    .getAddress().getHostAddress();
            return Mono.just("ip_" + ip);
        };
    }

    /**
     * 基于IP的限流解析器（备用）
     * 移除@Bean注解，或者使用@Qualifier区分
     */
    // @Bean
    // public KeyResolver ipKeyResolver() {
    //     return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest()
    //                     .getRemoteAddress())
    //             .getAddress()
    //             .getHostAddress());
    // }
}