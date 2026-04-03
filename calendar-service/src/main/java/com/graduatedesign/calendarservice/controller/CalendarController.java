package com.graduatedesign.calendarservice.controller;

import com.graduatedesign.calendarservice.dto.CalendarEventResponse;
import com.graduatedesign.calendarservice.dto.CreateEventRequest;
import com.graduatedesign.calendarservice.dto.UpdateEventRequest;
import com.graduatedesign.calendarservice.service.CalendarService;
import com.graduatedesign.commonmodule.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 日程管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    /**
     * 创建日程
     */
    @PostMapping("/events")
    public ApiResponse<CalendarEventResponse> createEvent(@RequestBody CreateEventRequest request) {
        try {
            CalendarEventResponse response = calendarService.createEvent(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("创建日程失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 更新日程
     */
    @PutMapping("/events/{eventId}")
    public ApiResponse<CalendarEventResponse> updateEvent(
            @PathVariable Long eventId,
            @RequestParam Long userId,
            @RequestBody UpdateEventRequest request) {
        try {
            CalendarEventResponse response = calendarService.updateEvent(eventId, userId, request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("更新日程失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 删除日程
     */
    @DeleteMapping("/events/{eventId}")
    public ApiResponse<Void> deleteEvent(
            @PathVariable Long eventId,
            @RequestParam Long userId) {
        try {
            calendarService.deleteEvent(eventId, userId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            log.error("删除日程失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据ID查询日程
     */
    @GetMapping("/events/{eventId}")
    public ApiResponse<CalendarEventResponse> getEventById(
            @PathVariable Long eventId,
            @RequestParam Long userId) {
        try {
            CalendarEventResponse response = calendarService.getEventById(eventId, userId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("查询日程失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 查询用户的所有日程
     */
    @GetMapping("/events")
    public ApiResponse<List<CalendarEventResponse>> getAllEvents(@RequestParam Long userId) {
        try {
            List<CalendarEventResponse> events = calendarService.getAllEventsByUserId(userId);
            return ApiResponse.success(events);
        } catch (Exception e) {
            log.error("查询日程列表失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据日期范围查询日程
     */
    @GetMapping("/events/range")
    public ApiResponse<List<CalendarEventResponse>> getEventsByDateRange(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<CalendarEventResponse> events = calendarService.getEventsByDateRange(userId, startDate, endDate);
            return ApiResponse.success(events);
        } catch (Exception e) {
            log.error("查询日程范围失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 查询某一天的日程
     */
    @GetMapping("/events/day")
    public ApiResponse<List<CalendarEventResponse>> getEventsByDate(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<CalendarEventResponse> events = calendarService.getEventsByDate(userId, date);
            return ApiResponse.success(events);
        } catch (Exception e) {
            log.error("查询日程失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据状态查询日程
     */
    @GetMapping("/events/status")
    public ApiResponse<List<CalendarEventResponse>> getEventsByStatus(
            @RequestParam Long userId,
            @RequestParam String status) {
        try {
            List<CalendarEventResponse> events = calendarService.getEventsByStatus(userId, status);
            return ApiResponse.success(events);
        } catch (Exception e) {
            log.error("查询日程失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据标题搜索日程
     */
    @GetMapping("/events/search")
    public ApiResponse<List<CalendarEventResponse>> searchEventsByTitle(
            @RequestParam Long userId,
            @RequestParam String title) {
        try {
            List<CalendarEventResponse> events = calendarService.searchEventsByTitle(userId, title);
            return ApiResponse.success(events);
        } catch (Exception e) {
            log.error("搜索日程失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 获取需要提醒的日程
     */
    @GetMapping("/events/reminders")
    public ApiResponse<List<CalendarEventResponse>> getUpcomingReminders(@RequestParam Long userId) {
        try {
            List<CalendarEventResponse> events = calendarService.getUpcomingReminders(userId);
            return ApiResponse.success(events);
        } catch (Exception e) {
            log.error("查询提醒日程失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }
}


