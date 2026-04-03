package com.graduatedesign.calendarservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 更新日程请求DTO
 */
@Data
public class UpdateEventRequest {
    private String title;  // 标题
    private String description;  // 描述
    private LocalDateTime startTime;  // 开始时间
    private LocalDateTime endTime;  // 结束时间
    private String location;  // 地点
    private LocalDateTime reminderTime;  // 提醒时间
    private String status;  // 状态
    private LocalDate scheduleDate; // 计划日期
    private String category; // 分类
    private String priority; // 优先级
    private Integer estimatedDuration; // 预计时长
}


