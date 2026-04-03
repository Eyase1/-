package com.graduatedesign.calendarservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 日常安排实体
 * 用于智能办公系统的日常任务和工作安排管理
 */
@Data
@Entity
@Table(name = "t_daily_schedule")
public class DailySchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;  // 用户ID
    
    @Column(nullable = false, length = 200)
    private String title;  // 安排标题
    
    @Column(columnDefinition = "TEXT")
    private String description;  // 安排描述
    
    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;  // 安排日期
    
    @Column(name = "start_time")
    private LocalTime startTime;  // 开始时间（可选，用于时间段安排）
    
    @Column(name = "end_time")
    private LocalTime endTime;  // 结束时间（可选）
    
    @Column(length = 50)
    private String category;  // 分类：工作、会议、学习、休息、其他
    
    @Column(length = 20)
    private String priority;  // 优先级：HIGH(高)、MEDIUM(中)、LOW(低)
    
    @Column(length = 20)
    private String status;  // 状态：PENDING(待办)、IN_PROGRESS(进行中)、COMPLETED(已完成)、CANCELLED(已取消)
    
    @Column(name = "estimated_duration")
    private Integer estimatedDuration;  // 预计时长（分钟）
    
    @Column(name = "actual_duration")
    private Integer actualDuration;  // 实际时长（分钟）
    
    @Column(name = "is_recurring")
    private Boolean isRecurring;  // 是否重复安排
    
    @Column(name = "recurring_pattern")
    private String recurringPattern;  // 重复模式：DAILY(每天)、WEEKLY(每周)、MONTHLY(每月)
    
    @Column(name = "reminder_time")
    private LocalDateTime reminderTime;  // 提醒时间
    
    @Column(name = "completion_time")
    private LocalDateTime completionTime;  // 完成时间
    
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
        if (priority == null) {
            priority = "MEDIUM";
        }
        if (category == null) {
            category = "工作";
        }
        if (isRecurring == null) {
            isRecurring = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
        // 如果状态变为已完成，记录完成时间
        if ("COMPLETED".equals(status) && completionTime == null) {
            completionTime = LocalDateTime.now();
        }
    }
}

