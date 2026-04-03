package com.graduatedesign.todoservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateTodoRequest {
    private Long userId;
    private String title;
    private String description;
    private TodoItemDto.TodoPriority priority;
    private LocalDateTime dueDate;
}

