package com.graduatedesign.userservice.controller;

import com.graduatedesign.commonmodule.dto.ApiResponse;
import com.graduatedesign.userservice.dto.LoginRequest;
import com.graduatedesign.userservice.dto.LoginResponse;
import com.graduatedesign.userservice.dto.RegisterRequest;
import com.graduatedesign.userservice.domain.User;
import com.graduatedesign.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        try {
            log.info("用户注册请求: username={}", request.getUsername());

            // 验证请求参数
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                ApiResponse<User> response = new ApiResponse<>();
                response.setCode(400);
                response.setMessage("用户名不能为空");
                return response;
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                ApiResponse<User> response = new ApiResponse<>();
                response.setCode(400);
                response.setMessage("密码不能为空");
                return response;
            }

            User user = userService.register(request);
            return ApiResponse.success(user);

        } catch (Exception e) {
            log.error("用户注册失败", e);
            ApiResponse<User> response = new ApiResponse<>();
            response.setCode(400);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            log.info("用户登录请求: username={}", request.getUsername());

            // 验证请求参数
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                ApiResponse<LoginResponse> response = new ApiResponse<>();
                response.setCode(400);
                response.setMessage("用户名不能为空");
                return response;
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                ApiResponse<LoginResponse> response = new ApiResponse<>();
                response.setCode(400);
                response.setMessage("密码不能为空");
                return response;
            }

            LoginResponse loginResponse = userService.login(request);
            return ApiResponse.success(loginResponse);

        } catch (Exception e) {
            log.error("用户登录失败", e);
            ApiResponse<LoginResponse> response = new ApiResponse<>();
            response.setCode(401);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 验证token
     */
    @GetMapping("/validate")
    public ApiResponse<User> validateToken(@RequestParam String token) {
        try {
            User user = userService.validateToken(token);
            if (user != null) {
                return ApiResponse.success(user);
            } else {
                ApiResponse<User> response = new ApiResponse<>();
                response.setCode(401);
                response.setMessage("Token无效");
                return response;
            }
        } catch (Exception e) {
            log.error("验证token失败", e);
            ApiResponse<User> response = new ApiResponse<>();
            response.setCode(500);
            response.setMessage("验证失败");
            return response;
        }
    }
}

