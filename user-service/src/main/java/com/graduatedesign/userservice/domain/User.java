package com.graduatedesign.userservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password; // 密码（实际项目中应该加密存储）

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}



