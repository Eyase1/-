package com.graduatedesign.userservice.service;

import com.graduatedesign.userservice.domain.User;
import com.graduatedesign.userservice.dto.LoginRequest;
import com.graduatedesign.userservice.dto.LoginResponse;
import com.graduatedesign.userservice.dto.RegisterRequest;
import com.graduatedesign.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户服务
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // token暂时使用内存存储（后续可以改成Redis）
    private final Map<String, String> tokenMap = new ConcurrentHashMap<>(); // token -> username

    /**
     * 用户注册
     */
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // 实际项目中应该加密
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setCreateTime(LocalDateTime.now());

        // 保存用户到数据库
        user = userRepository.save(user);
        log.info("用户注册成功: {}", user.getUsername());

        return user;
    }

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        // 从数据库查询用户
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 验证密码
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成简单的token（实际项目中应该使用JWT）
        String token = generateToken(user.getUsername());
        tokenMap.put(token, user.getUsername());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRedirectUrl("http://localhost:8083"); // 跳转到前端页面

        log.info("用户登录成功: {}", user.getUsername());
        return response;
    }

    /**
     * 验证token
     */
    public User validateToken(String token) {
        String username = tokenMap.get(token);
        if (username == null) {
            return null;
        }
        // 从数据库查询用户
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * 生成token（简单实现）
     */
    private String generateToken(String username) {
        return "token_" + username + "_" + System.currentTimeMillis();
    }
}



