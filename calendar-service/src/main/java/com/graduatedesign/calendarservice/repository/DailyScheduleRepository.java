package com.graduatedesign.calendarservice.repository;

import com.graduatedesign.calendarservice.domain.DailySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日常安排数据访问层
 */
@Repository
public interface DailyScheduleRepository extends JpaRepository<DailySchedule, Long> {
    
    /**
     * 根据用户ID和日期查询日常安排
     */
    List<DailySchedule> findByUserIdAndScheduleDateOrderByStartTimeAsc(Long userId, LocalDate date);
    
    /**
     * 根据用户ID和日期范围查询日常安排
     */
    @Query("SELECT s FROM DailySchedule s WHERE s.userId = :userId " +
           "AND s.scheduleDate >= :startDate AND s.scheduleDate <= :endDate " +
           "ORDER BY s.scheduleDate ASC, s.startTime ASC")
    List<DailySchedule> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    
    /**
     * 根据用户ID和状态查询日常安排
     */
    List<DailySchedule> findByUserIdAndStatusOrderByScheduleDateDesc(Long userId, String status);
    
    /**
     * 根据用户ID和分类查询日常安排
     */
    List<DailySchedule> findByUserIdAndCategoryOrderByScheduleDateDesc(Long userId, String category);
    
    /**
     * 根据用户ID和优先级查询日常安排
     */
    List<DailySchedule> findByUserIdAndPriorityOrderByScheduleDateDesc(Long userId, String priority);
    
    /**
     * 查询用户今日的日常安排
     */
    @Query("SELECT s FROM DailySchedule s WHERE s.userId = :userId " +
           "AND s.scheduleDate = :date " +
           "ORDER BY s.startTime ASC NULLS LAST, s.priority DESC")
    List<DailySchedule> findTodaySchedules(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    /**
     * 查询需要提醒的日常安排
     */
    @Query("SELECT s FROM DailySchedule s WHERE s.userId = :userId " +
           "AND s.reminderTime IS NOT NULL " +
           "AND s.reminderTime <= :now " +
           "AND s.status NOT IN ('COMPLETED', 'CANCELLED') " +
           "ORDER BY s.reminderTime ASC")
    List<DailySchedule> findUpcomingReminders(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );
    
    /**
     * 统计用户指定日期范围内的完成情况
     */
    @Query("SELECT COUNT(s) FROM DailySchedule s WHERE s.userId = :userId " +
           "AND s.scheduleDate >= :startDate AND s.scheduleDate <= :endDate " +
           "AND s.status = 'COMPLETED'")
    Long countCompletedByDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    
    /**
     * 统计用户指定日期范围内的总安排数
     */
    @Query("SELECT COUNT(s) FROM DailySchedule s WHERE s.userId = :userId " +
           "AND s.scheduleDate >= :startDate AND s.scheduleDate <= :endDate")
    Long countTotalByDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    
    /**
     * 根据用户ID和标题模糊查询
     */
    List<DailySchedule> findByUserIdAndTitleContainingOrderByScheduleDateDesc(Long userId, String title);
    
    /**
     * 查询重复安排的模板（用于生成日常安排）
     */
    List<DailySchedule> findByUserIdAndIsRecurringTrueOrderByScheduleDateDesc(Long userId);
}

