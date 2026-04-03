package com.graduatedesign.calendarservice.service;

import com.graduatedesign.calendarservice.domain.CalendarEvent;
import com.graduatedesign.calendarservice.dto.CalendarEventResponse;
import com.graduatedesign.calendarservice.dto.CreateEventRequest;
import com.graduatedesign.calendarservice.dto.UpdateEventRequest;
import com.graduatedesign.calendarservice.repository.CalendarEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日程服务
 */
@Slf4j
@Service
public class CalendarService {

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    /**
     * 创建日程
     */
    @Transactional
    public CalendarEventResponse createEvent(CreateEventRequest request) {
        // 验证时间
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new RuntimeException("结束时间不能早于开始时间");
        }

        CalendarEvent event = new CalendarEvent();
        BeanUtils.copyProperties(request, event);

        if (event.getScheduleDate() == null && event.getStartTime() != null) {
            event.setScheduleDate(event.getStartTime().toLocalDate());
        }
        if (event.getCategory() == null || event.getCategory().isEmpty()) {
            event.setCategory("工作");
        }
        if (event.getPriority() == null || event.getPriority().isEmpty()) {
            event.setPriority("MEDIUM");
        }
        
        // 如果没有设置状态，默认为待办
        if (event.getStatus() == null || event.getStatus().isEmpty()) {
            event.setStatus("PENDING");
        }

        event = calendarEventRepository.save(event);
        log.info("创建日程成功: userId={}, title={}", request.getUserId(), request.getTitle());
        
        return convertToResponse(event);
    }

    /**
     * 更新日程
     */
    @Transactional
    public CalendarEventResponse updateEvent(Long eventId, Long userId, UpdateEventRequest request) {
        CalendarEvent event = calendarEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("日程不存在"));

        // 验证权限：只能修改自己的日程
        if (!event.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此日程");
        }

        // 更新字段
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getStartTime() != null) {
            event.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            event.setEndTime(request.getEndTime());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getReminderTime() != null) {
            event.setReminderTime(request.getReminderTime());
        }
        if (request.getStatus() != null) {
            event.setStatus(request.getStatus());
        }
        if (request.getScheduleDate() != null) {
            event.setScheduleDate(request.getScheduleDate());
        } else if (request.getStartTime() != null) {
            event.setScheduleDate(request.getStartTime().toLocalDate());
        }
        if (request.getCategory() != null) {
            event.setCategory(request.getCategory());
        }
        if (request.getPriority() != null) {
            event.setPriority(request.getPriority());
        }
        if (request.getEstimatedDuration() != null) {
            event.setEstimatedDuration(request.getEstimatedDuration());
        }

        // 验证时间
        if (event.getEndTime().isBefore(event.getStartTime())) {
            throw new RuntimeException("结束时间不能早于开始时间");
        }

        event = calendarEventRepository.save(event);
        log.info("更新日程成功: eventId={}, userId={}", eventId, userId);
        
        return convertToResponse(event);
    }

    /**
     * 删除日程
     */
    @Transactional
    public void deleteEvent(Long eventId, Long userId) {
        CalendarEvent event = calendarEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("日程不存在"));

        // 验证权限
        if (!event.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此日程");
        }

        calendarEventRepository.delete(event);
        log.info("删除日程成功: eventId={}, userId={}", eventId, userId);
    }

    /**
     * 根据ID查询日程
     */
    public CalendarEventResponse getEventById(Long eventId, Long userId) {
        CalendarEvent event = calendarEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("日程不存在"));

        // 验证权限
        if (!event.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此日程");
        }

        return convertToResponse(event);
    }

    /**
     * 查询用户的所有日程
     */
    public List<CalendarEventResponse> getAllEventsByUserId(Long userId) {
        List<CalendarEvent> events = calendarEventRepository.findByUserIdOrderByStartTimeAsc(userId);
        return events.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据日期范围查询日程
     */
    public List<CalendarEventResponse> getEventsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        List<CalendarEvent> events = calendarEventRepository.findByUserIdAndDateRange(
                userId, startDateTime, endDateTime);
        
        return events.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 查询某一天的日程
     */
    public List<CalendarEventResponse> getEventsByDate(Long userId, LocalDate date) {
        return getEventsByDateRange(userId, date, date);
    }

    /**
     * 根据状态查询日程
     */
    public List<CalendarEventResponse> getEventsByStatus(Long userId, String status) {
        List<CalendarEvent> events = calendarEventRepository.findByUserIdAndStatusOrderByStartTimeAsc(userId, status);
        return events.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据标题搜索日程
     */
    public List<CalendarEventResponse> searchEventsByTitle(Long userId, String title) {
        List<CalendarEvent> events = calendarEventRepository.findByUserIdAndTitleContainingOrderByStartTimeAsc(userId, title);
        return events.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取需要提醒的日程
     */
    public List<CalendarEventResponse> getUpcomingReminders(Long userId) {
        List<CalendarEvent> events = calendarEventRepository.findUpcomingReminders(userId, LocalDateTime.now());
        return events.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 转换为响应DTO
     */
    private CalendarEventResponse convertToResponse(CalendarEvent event) {
        CalendarEventResponse response = new CalendarEventResponse();
        BeanUtils.copyProperties(event, response);
        return response;
    }
}


