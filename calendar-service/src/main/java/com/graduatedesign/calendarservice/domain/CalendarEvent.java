package com.graduatedesign.calendarservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日程事件实体
 */
@Data
@Entity
@Table(name = "t_calendar_event")
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;  // 用户ID
    
    @Column(nullable = false, length = 200)
    private String title;  // 日程标题
    
    @Column(columnDefinition = "TEXT")
    private String description;  // 日程描述
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;  // 开始时间
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;  // 结束时间
    
    @Column(length = 200)
    private String location;  // 地点
    
    @Column(name = "reminder_time")
    private LocalDateTime reminderTime;  // 提醒时间
    
    @Column(length = 20)
    private String status;  // 状态：PENDING(待办)、IN_PROGRESS(进行中)、COMPLETED(已完成)、CANCELLED(已取消)

    @Column(name = "schedule_date")
    private LocalDate scheduleDate; // 计划日期

    @Column(length = 50)
    private String category; // 分类

    @Column(length = 20)
    private String priority; // 优先级

    @Column(name = "estimated_duration")
    private Integer estimatedDuration; // 预计时长（分钟）
    
    @Column(name = "create_time")
    private LocalDateTime createTime;  // 创建时间
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;  // 更新时间
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}


