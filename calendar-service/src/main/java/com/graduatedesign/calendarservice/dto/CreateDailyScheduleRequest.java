package com.graduatedesign.calendarservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 创建日常安排请求DTO
 */
@Data
public class CreateDailyScheduleRequest {
    private Long userId;  // 用户ID
    private String title;  // 标题
    private String description;  // 描述
    private LocalDate scheduleDate;  // 安排日期
    private LocalTime startTime;  // 开始时间（可选）
    private LocalTime endTime;  // 结束时间（可选）
    private String category;  // 分类：工作、会议、学习、休息、其他
    private String priority;  // 优先级：HIGH(高)、MEDIUM(中)、LOW(低)
    private Integer estimatedDuration;  // 预计时长（分钟）
    private Boolean isRecurring;  // 是否重复安排
    private String recurringPattern;  // 重复模式：DAILY(每天)、WEEKLY(每周)、MONTHLY(每月)
    private LocalDateTime reminderTime;  // 提醒时间
    private String status;  // 状态（可选，默认为PENDING）
}

