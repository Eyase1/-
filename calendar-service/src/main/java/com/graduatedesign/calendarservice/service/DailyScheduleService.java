package com.graduatedesign.calendarservice.service;

import com.graduatedesign.calendarservice.domain.DailySchedule;
import com.graduatedesign.calendarservice.dto.CreateDailyScheduleRequest;
import com.graduatedesign.calendarservice.dto.DailyScheduleResponse;
import com.graduatedesign.calendarservice.dto.UpdateDailyScheduleRequest;
import com.graduatedesign.calendarservice.dto.WorkStatisticsResponse;
import com.graduatedesign.calendarservice.repository.DailyScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 日常安排服务
 * 提供智能办公系统的日常任务和工作安排管理功能
 */
@Slf4j
@Service
public class DailyScheduleService {

    @Autowired
    private DailyScheduleRepository dailyScheduleRepository;

    /**
     * 创建日常安排
     */
    @Transactional
    public DailyScheduleResponse createSchedule(CreateDailyScheduleRequest request) {
        // 验证时间
        if (request.getStartTime() != null && request.getEndTime() != null) {
            if (request.getEndTime().isBefore(request.getStartTime())) {
                throw new RuntimeException("结束时间不能早于开始时间");
            }
        }

        DailySchedule schedule = new DailySchedule();
        BeanUtils.copyProperties(request, schedule);
        
        // 如果没有设置状态，默认为待办
        if (schedule.getStatus() == null || schedule.getStatus().isEmpty()) {
            schedule.setStatus("PENDING");
        }

        schedule = dailyScheduleRepository.save(schedule);
        log.info("创建日常安排成功: userId={}, title={}, date={}", 
                request.getUserId(), request.getTitle(), request.getScheduleDate());
        
        return convertToResponse(schedule);
    }

    /**
     * 更新日常安排
     */
    @Transactional
    public DailyScheduleResponse updateSchedule(Long scheduleId, Long userId, UpdateDailyScheduleRequest request) {
        DailySchedule schedule = dailyScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("日常安排不存在"));

        // 验证权限：只能修改自己的安排
        if (!schedule.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此安排");
        }

        // 更新字段
        if (request.getTitle() != null) {
            schedule.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            schedule.setDescription(request.getDescription());
        }
        if (request.getScheduleDate() != null) {
            schedule.setScheduleDate(request.getScheduleDate());
        }
        if (request.getStartTime() != null) {
            schedule.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            schedule.setEndTime(request.getEndTime());
        }
        if (request.getCategory() != null) {
            schedule.setCategory(request.getCategory());
        }
        if (request.getPriority() != null) {
            schedule.setPriority(request.getPriority());
        }
        if (request.getEstimatedDuration() != null) {
            schedule.setEstimatedDuration(request.getEstimatedDuration());
        }
        if (request.getActualDuration() != null) {
            schedule.setActualDuration(request.getActualDuration());
        }
        if (request.getIsRecurring() != null) {
            schedule.setIsRecurring(request.getIsRecurring());
        }
        if (request.getRecurringPattern() != null) {
            schedule.setRecurringPattern(request.getRecurringPattern());
        }
        if (request.getReminderTime() != null) {
            schedule.setReminderTime(request.getReminderTime());
        }
        if (request.getStatus() != null) {
            schedule.setStatus(request.getStatus());
        }

        // 验证时间
        if (schedule.getStartTime() != null && schedule.getEndTime() != null) {
            if (schedule.getEndTime().isBefore(schedule.getStartTime())) {
                throw new RuntimeException("结束时间不能早于开始时间");
            }
        }

        schedule = dailyScheduleRepository.save(schedule);
        log.info("更新日常安排成功: scheduleId={}, userId={}", scheduleId, userId);
        
        return convertToResponse(schedule);
    }

    /**
     * 删除日常安排
     */
    @Transactional
    public void deleteSchedule(Long scheduleId, Long userId) {
        DailySchedule schedule = dailyScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("日常安排不存在"));

        // 验证权限
        if (!schedule.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此安排");
        }

        dailyScheduleRepository.delete(schedule);
        log.info("删除日常安排成功: scheduleId={}, userId={}", scheduleId, userId);
    }

    /**
     * 根据ID查询日常安排
     */
    public DailyScheduleResponse getScheduleById(Long scheduleId, Long userId) {
        DailySchedule schedule = dailyScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("日常安排不存在"));

        // 验证权限
        if (!schedule.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此安排");
        }

        return convertToResponse(schedule);
    }

    /**
     * 查询用户今日的日常安排
     */
    public List<DailyScheduleResponse> getTodaySchedules(Long userId) {
        LocalDate today = LocalDate.now();
        List<DailySchedule> schedules = dailyScheduleRepository.findTodaySchedules(userId, today);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 查询用户指定日期的日常安排
     */
    public List<DailyScheduleResponse> getSchedulesByDate(Long userId, LocalDate date) {
        List<DailySchedule> schedules = dailyScheduleRepository.findByUserIdAndScheduleDateOrderByStartTimeAsc(userId, date);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据日期范围查询日常安排
     */
    public List<DailyScheduleResponse> getSchedulesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<DailySchedule> schedules = dailyScheduleRepository.findByUserIdAndDateRange(userId, startDate, endDate);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据状态查询日常安排
     */
    public List<DailyScheduleResponse> getSchedulesByStatus(Long userId, String status) {
        List<DailySchedule> schedules = dailyScheduleRepository.findByUserIdAndStatusOrderByScheduleDateDesc(userId, status);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据分类查询日常安排
     */
    public List<DailyScheduleResponse> getSchedulesByCategory(Long userId, String category) {
        List<DailySchedule> schedules = dailyScheduleRepository.findByUserIdAndCategoryOrderByScheduleDateDesc(userId, category);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据优先级查询日常安排
     */
    public List<DailyScheduleResponse> getSchedulesByPriority(Long userId, String priority) {
        List<DailySchedule> schedules = dailyScheduleRepository.findByUserIdAndPriorityOrderByScheduleDateDesc(userId, priority);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据标题搜索日常安排
     */
    public List<DailyScheduleResponse> searchSchedulesByTitle(Long userId, String title) {
        List<DailySchedule> schedules = dailyScheduleRepository.findByUserIdAndTitleContainingOrderByScheduleDateDesc(userId, title);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取需要提醒的日常安排
     */
    public List<DailyScheduleResponse> getUpcomingReminders(Long userId) {
        List<DailySchedule> schedules = dailyScheduleRepository.findUpcomingReminders(userId, LocalDateTime.now());
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 完成日常安排
     */
    @Transactional
    public DailyScheduleResponse completeSchedule(Long scheduleId, Long userId, Integer actualDuration) {
        DailySchedule schedule = dailyScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("日常安排不存在"));

        if (!schedule.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此安排");
        }

        schedule.setStatus("COMPLETED");
        if (actualDuration != null) {
            schedule.setActualDuration(actualDuration);
        }
        schedule.setCompletionTime(LocalDateTime.now());

        schedule = dailyScheduleRepository.save(schedule);
        log.info("完成日常安排: scheduleId={}, userId={}", scheduleId, userId);
        
        return convertToResponse(schedule);
    }

    /**
     * 获取工作统计信息
     */
    public WorkStatisticsResponse getWorkStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        WorkStatisticsResponse statistics = new WorkStatisticsResponse();
        statistics.setUserId(userId);
        statistics.setStartDate(startDate);
        statistics.setEndDate(endDate);

        // 查询所有安排
        List<DailySchedule> schedules = dailyScheduleRepository.findByUserIdAndDateRange(userId, startDate, endDate);
        
        statistics.setTotalSchedules((long) schedules.size());
        statistics.setCompletedSchedules(schedules.stream()
                .filter(s -> "COMPLETED".equals(s.getStatus()))
                .count());
        statistics.setPendingSchedules(schedules.stream()
                .filter(s -> "PENDING".equals(s.getStatus()))
                .count());
        statistics.setInProgressSchedules(schedules.stream()
                .filter(s -> "IN_PROGRESS".equals(s.getStatus()))
                .count());

        // 计算完成率
        if (statistics.getTotalSchedules() > 0) {
            double completionRate = (double) statistics.getCompletedSchedules() / statistics.getTotalSchedules() * 100;
            statistics.setCompletionRate(Math.round(completionRate * 100.0) / 100.0);
        } else {
            statistics.setCompletionRate(0.0);
        }

        // 计算总时长
        statistics.setTotalEstimatedMinutes(schedules.stream()
                .filter(s -> s.getEstimatedDuration() != null)
                .mapToInt(DailySchedule::getEstimatedDuration)
                .sum());
        statistics.setTotalActualMinutes(schedules.stream()
                .filter(s -> s.getActualDuration() != null)
                .mapToInt(DailySchedule::getActualDuration)
                .sum());

        // 分类统计
        Map<String, List<DailySchedule>> categoryMap = schedules.stream()
                .collect(Collectors.groupingBy(s -> s.getCategory() != null ? s.getCategory() : "其他"));
        
        List<WorkStatisticsResponse.CategoryStatistics> categoryStats = categoryMap.entrySet().stream()
                .map(entry -> {
                    WorkStatisticsResponse.CategoryStatistics stat = new WorkStatisticsResponse.CategoryStatistics();
                    stat.setCategory(entry.getKey());
                    stat.setCount((long) entry.getValue().size());
                    stat.setCompletedCount(entry.getValue().stream()
                            .filter(s -> "COMPLETED".equals(s.getStatus()))
                            .count());
                    if (stat.getCount() > 0) {
                        double rate = (double) stat.getCompletedCount() / stat.getCount() * 100;
                        stat.setCompletionRate(Math.round(rate * 100.0) / 100.0);
                    } else {
                        stat.setCompletionRate(0.0);
                    }
                    return stat;
                })
                .collect(Collectors.toList());
        
        statistics.setCategoryStatistics(categoryStats);

        return statistics;
    }

    /**
     * 获取本周工作统计
     */
    public WorkStatisticsResponse getThisWeekStatistics(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return getWorkStatistics(userId, startOfWeek, endOfWeek);
    }

    /**
     * 获取本月工作统计
     */
    public WorkStatisticsResponse getThisMonthStatistics(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        return getWorkStatistics(userId, startOfMonth, endOfMonth);
    }

    /**
     * 转换为响应DTO
     */
    private DailyScheduleResponse convertToResponse(DailySchedule schedule) {
        DailyScheduleResponse response = new DailyScheduleResponse();
        BeanUtils.copyProperties(schedule, response);
        return response;
    }
}

