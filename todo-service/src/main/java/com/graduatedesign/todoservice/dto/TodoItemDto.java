package com.graduatedesign.todoservice.dto;

import com.graduatedesign.todoservice.domain.TodoItem;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TodoItemDto {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private TodoStatus status;
    private TodoPriority priority;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public enum TodoStatus {
        PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public enum TodoPriority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public static TodoItemDto fromEntity(TodoItem entity) {
        TodoItemDto dto = new TodoItemDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStatus(TodoStatus.valueOf(entity.getStatus().name()));
        dto.setPriority(TodoPriority.valueOf(entity.getPriority().name()));
        dto.setDueDate(entity.getDueDate());
        dto.setCompletedAt(entity.getCompletedAt());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}

