package com.graduatedesign.calendarservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日程响应DTO
 */
@Data
public class CalendarEventResponse {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private LocalDateTime reminderTime;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDate scheduleDate;
    private String category;
    private String priority;
    private Integer estimatedDuration;
}


