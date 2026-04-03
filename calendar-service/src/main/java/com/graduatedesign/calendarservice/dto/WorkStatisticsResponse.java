package com.graduatedesign.calendarservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 工作统计响应DTO
 */
@Data
public class WorkStatisticsResponse {
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalSchedules;  // 总安排数
    private Long completedSchedules;  // 已完成数
    private Long pendingSchedules;  // 待办数
    private Long inProgressSchedules;  // 进行中数
    private Double completionRate;  // 完成率（百分比）
    private Integer totalEstimatedMinutes;  // 总预计时长（分钟）
    private Integer totalActualMinutes;  // 总实际时长（分钟）
    private List<CategoryStatistics> categoryStatistics;  // 分类统计
    
    @Data
    public static class CategoryStatistics {
        private String category;
        private Long count;
        private Long completedCount;
        private Double completionRate;
    }
}

