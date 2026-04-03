package com.graduatedesign.calendarservice.controller;

import com.graduatedesign.calendarservice.dto.CreateDailyScheduleRequest;
import com.graduatedesign.calendarservice.dto.DailyScheduleResponse;
import com.graduatedesign.calendarservice.dto.UpdateDailyScheduleRequest;
import com.graduatedesign.calendarservice.dto.WorkStatisticsResponse;
import com.graduatedesign.calendarservice.service.DailyScheduleService;
import com.graduatedesign.commonmodule.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 日常安排控制器
 * 提供智能办公系统的日常任务和工作安排管理API
 */
@Slf4j
@RestController
@RequestMapping("/calendar/daily-schedule")
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class DailyScheduleController {

    @Autowired
    private DailyScheduleService dailyScheduleService;

    /**
     * 创建日常安排
     */
    @PostMapping
    public ApiResponse<DailyScheduleResponse> createSchedule(@RequestBody CreateDailyScheduleRequest request) {
        try {
            DailyScheduleResponse response = dailyScheduleService.createSchedule(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("创建日常安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 更新日常安排
     */
    @PutMapping("/{scheduleId}")
    public ApiResponse<DailyScheduleResponse> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestParam Long userId,
            @RequestBody UpdateDailyScheduleRequest request) {
        try {
            DailyScheduleResponse response = dailyScheduleService.updateSchedule(scheduleId, userId, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("更新日常安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 删除日常安排
     */
    @DeleteMapping("/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(
            @PathVariable Long scheduleId,
            @RequestParam Long userId) {
        try {
            dailyScheduleService.deleteSchedule(scheduleId, userId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            log.error("删除日常安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据ID查询日常安排
     */
    @GetMapping("/{scheduleId}")
    public ApiResponse<DailyScheduleResponse> getScheduleById(
            @PathVariable Long scheduleId,
            @RequestParam Long userId) {
        try {
            DailyScheduleResponse response = dailyScheduleService.getScheduleById(scheduleId, userId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("查询日常安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 查询今日的日常安排
     */
    @GetMapping("/today")
    public ApiResponse<List<DailyScheduleResponse>> getTodaySchedules(@RequestParam Long userId) {
        try {
            List<DailyScheduleResponse> schedules = dailyScheduleService.getTodaySchedules(userId);
            return ApiResponse.success(schedules);
        } catch (Exception e) {
            log.error("查询今日安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 查询指定日期的日常安排
     */
    @GetMapping("/date")
    public ApiResponse<List<DailyScheduleResponse>> getSchedulesByDate(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<DailyScheduleResponse> schedules = dailyScheduleService.getSchedulesByDate(userId, date);
            return ApiResponse.success(schedules);
        } catch (Exception e) {
            log.error("查询日期安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据日期范围查询日常安排
     */
    @GetMapping("/range")
    public ApiResponse<List<DailyScheduleResponse>> getSchedulesByDateRange(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<DailyScheduleResponse> schedules = dailyScheduleService.getSchedulesByDateRange(userId, startDate, endDate);
            return ApiResponse.success(schedules);
        } catch (Exception e) {
            log.error("查询日期范围安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据状态查询日常安排
     */
    @GetMapping("/status")
    public ApiResponse<List<DailyScheduleResponse>> getSchedulesByStatus(
            @RequestParam Long userId,
            @RequestParam String status) {
        try {
            List<DailyScheduleResponse> schedules = dailyScheduleService.getSchedulesByStatus(userId, status);
            return ApiResponse.success(schedules);
        } catch (Exception e) {
            log.error("查询状态安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据分类查询日常安排
     */
    @GetMapping("/category")
    public ApiResponse<List<DailyScheduleResponse>> getSchedulesByCategory(
            @RequestParam Long userId,
            @RequestParam String category) {
        try {
            List<DailyScheduleResponse> schedules = dailyScheduleService.getSchedulesByCategory(userId, category);
            return ApiResponse.success(schedules);
        } catch (Exception e) {
            log.error("查询分类安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据优先级查询日常安排
     */
    @GetMapping("/priority")
    public ApiResponse<List<DailyScheduleResponse>> getSchedulesByPriority(
            @RequestParam Long userId,
            @RequestParam String priority) {
        try {
            List<DailyScheduleResponse> schedules = dailyScheduleService.getSchedulesByPriority(userId, priority);
            return ApiResponse.success(schedules);
        } catch (Exception e) {
            log.error("查询优先级安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据标题搜索日常安排
     */
    @GetMapping("/search")
    public ApiResponse<List<DailyScheduleResponse>> searchSchedulesByTitle(
            @RequestParam Long userId,
            @RequestParam String title) {
        try {
            List<DailyScheduleResponse> schedules = dailyScheduleService.searchSchedulesByTitle(userId, title);
            return ApiResponse.success(schedules);
        } catch (Exception e) {
            log.error("搜索安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 获取需要提醒的日常安排
     */
    @GetMapping("/reminders")
    public ApiResponse<List<DailyScheduleResponse>> getUpcomingReminders(@RequestParam Long userId) {
        try {
            List<DailyScheduleResponse> schedules = dailyScheduleService.getUpcomingReminders(userId);
            return ApiResponse.success(schedules);
        } catch (Exception e) {
            log.error("查询提醒安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 完成日常安排
     */
    @PostMapping("/{scheduleId}/complete")
    public ApiResponse<DailyScheduleResponse> completeSchedule(
            @PathVariable Long scheduleId,
            @RequestParam Long userId,
            @RequestParam(required = false) Integer actualDuration) {
        try {
            DailyScheduleResponse response = dailyScheduleService.completeSchedule(scheduleId, userId, actualDuration);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("完成安排失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 获取工作统计信息
     */
    @GetMapping("/statistics")
    public ApiResponse<WorkStatisticsResponse> getWorkStatistics(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            WorkStatisticsResponse statistics = dailyScheduleService.getWorkStatistics(userId, startDate, endDate);
            return ApiResponse.success(statistics);
        } catch (Exception e) {
            log.error("获取工作统计失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 获取本周工作统计
     */
    @GetMapping("/statistics/week")
    public ApiResponse<WorkStatisticsResponse> getThisWeekStatistics(@RequestParam Long userId) {
        try {
            WorkStatisticsResponse statistics = dailyScheduleService.getThisWeekStatistics(userId);
            return ApiResponse.success(statistics);
        } catch (Exception e) {
            log.error("获取本周统计失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 获取本月工作统计
     */
    @GetMapping("/statistics/month")
    public ApiResponse<WorkStatisticsResponse> getThisMonthStatistics(@RequestParam Long userId) {
        try {
            WorkStatisticsResponse statistics = dailyScheduleService.getThisMonthStatistics(userId);
            return ApiResponse.success(statistics);
        } catch (Exception e) {
            log.error("获取本月统计失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }
}

