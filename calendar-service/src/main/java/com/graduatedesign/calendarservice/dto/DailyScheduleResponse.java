package com.graduatedesign.calendarservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 日常安排响应DTO
 */
@Data
public class DailyScheduleResponse {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String category;
    private String priority;
    private String status;
    private Integer estimatedDuration;
    private Integer actualDuration;
    private Boolean isRecurring;
    private String recurringPattern;
    private LocalDateTime reminderTime;
    private LocalDateTime completionTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

