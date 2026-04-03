package com.graduatedesign.todoservice.repository;

import com.graduatedesign.todoservice.domain.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    
    List<TodoItem> findByUserId(Long userId);
    
    List<TodoItem> findByUserIdAndStatus(Long userId, TodoItem.TodoStatus status);
    
    List<TodoItem> findByUserIdAndPriority(Long userId, TodoItem.TodoPriority priority);
    
    List<TodoItem> findByUserIdAndDueDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
    
    long countByUserIdAndStatus(Long userId, TodoItem.TodoStatus status);
}

