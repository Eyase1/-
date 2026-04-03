package com.graduatedesign.calendarservice.repository;

import com.graduatedesign.calendarservice.domain.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程事件数据访问层
 */
@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
    
    /**
     * 根据用户ID查询所有日程
     */
    List<CalendarEvent> findByUserIdOrderByStartTimeAsc(Long userId);
    
    /**
     * 根据用户ID和日期范围查询日程
     */
    @Query("SELECT e FROM CalendarEvent e WHERE e.userId = :userId " +
           "AND e.startTime >= :startDate AND e.startTime < :endDate " +
           "ORDER BY e.startTime ASC")
    List<CalendarEvent> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    /**
     * 根据用户ID和状态查询日程
     */
    List<CalendarEvent> findByUserIdAndStatusOrderByStartTimeAsc(Long userId, String status);
    
    /**
     * 根据用户ID和标题模糊查询
     */
    List<CalendarEvent> findByUserIdAndTitleContainingOrderByStartTimeAsc(Long userId, String title);
    
    /**
     * 查询需要提醒的日程（提醒时间在当前时间之后，且未完成）
     */
    @Query("SELECT e FROM CalendarEvent e WHERE e.userId = :userId " +
           "AND e.reminderTime IS NOT NULL " +
           "AND e.reminderTime <= :now " +
           "AND e.status NOT IN ('COMPLETED', 'CANCELLED') " +
           "ORDER BY e.reminderTime ASC")
    List<CalendarEvent> findUpcomingReminders(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );
    
    /**
     * 根据用户ID删除所有日程
     */
    void deleteByUserId(Long userId);
}


