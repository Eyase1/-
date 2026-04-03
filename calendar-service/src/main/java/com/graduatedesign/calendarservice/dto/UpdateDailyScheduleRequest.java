package com.graduatedesign.calendarservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 更新日常安排请求DTO
 */
@Data
public class UpdateDailyScheduleRequest {
    private String title;  // 标题
    private String description;  // 描述
    private LocalDate scheduleDate;  // 安排日期
    private LocalTime startTime;  // 开始时间
    private LocalTime endTime;  // 结束时间
    private String category;  // 分类
    private String priority;  // 优先级
    private Integer estimatedDuration;  // 预计时长（分钟）
    private Integer actualDuration;  // 实际时长（分钟）
    private Boolean isRecurring;  // 是否重复安排
    private String recurringPattern;  // 重复模式
    private LocalDateTime reminderTime;  // 提醒时间
    private String status;  // 状态
}

