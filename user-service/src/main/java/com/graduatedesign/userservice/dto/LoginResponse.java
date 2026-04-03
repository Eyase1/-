package com.graduatedesign.userservice.dto;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {
    private String token; // 简单的token（实际项目中应该使用JWT等）
    private Long userId;
    private String username;
    private String redirectUrl; // 登录后跳转的URL
}

