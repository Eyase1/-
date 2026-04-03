package com.graduatedesign.todoservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 待办事项实体
 */
@Data
@Entity
@Table(name = "t_todo_item")
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TodoStatus status = TodoStatus.PENDING;

    @Column
    @Enumerated(EnumType.STRING)
    private TodoPriority priority = TodoPriority.MEDIUM;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public enum TodoStatus {
        PENDING,    // 待办
        IN_PROGRESS, // 进行中
        COMPLETED,  // 已完成
        CANCELLED   // 已取消
    }

    public enum TodoPriority {
        LOW,       // 低
        MEDIUM,    // 中
        HIGH,      // 高
        URGENT     // 紧急
    }
}

