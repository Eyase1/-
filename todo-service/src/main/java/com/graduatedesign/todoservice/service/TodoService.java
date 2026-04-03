package com.graduatedesign.todoservice.service;

import com.graduatedesign.todoservice.domain.TodoItem;
import com.graduatedesign.todoservice.dto.CreateTodoRequest;
import com.graduatedesign.todoservice.dto.TodoItemDto;
import com.graduatedesign.todoservice.dto.UpdateTodoRequest;
import com.graduatedesign.todoservice.repository.TodoItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.graduatedesign.todoservice.dto.TodoStatisticsDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoItemRepository todoItemRepository;

    /**
     * 创建待办事项
     */
    @Transactional
    public TodoItemDto createTodo(CreateTodoRequest request) {
        TodoItem todoItem = new TodoItem();
        todoItem.setUserId(request.getUserId());
        todoItem.setTitle(request.getTitle());
        todoItem.setDescription(request.getDescription());
        todoItem.setPriority(request.getPriority() != null 
            ? TodoItem.TodoPriority.valueOf(request.getPriority().name()) 
            : TodoItem.TodoPriority.MEDIUM);
        todoItem.setStatus(TodoItem.TodoStatus.PENDING);
        todoItem.setDueDate(request.getDueDate());

        todoItem = todoItemRepository.save(todoItem);
        log.info("创建待办事项成功: id={}, userId={}, title={}", todoItem.getId(), todoItem.getUserId(), todoItem.getTitle());
        return TodoItemDto.fromEntity(todoItem);
    }

    /**
     * 更新待办事项
     */
    @Transactional
    public TodoItemDto updateTodo(Long id, UpdateTodoRequest request) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("待办事项不存在"));

        if (request.getTitle() != null) {
            todoItem.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            todoItem.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            TodoItem.TodoStatus newStatus = TodoItem.TodoStatus.valueOf(request.getStatus().name());
            todoItem.setStatus(newStatus);
            if (newStatus == TodoItem.TodoStatus.COMPLETED && todoItem.getCompletedAt() == null) {
                todoItem.setCompletedAt(LocalDateTime.now());
            } else if (newStatus != TodoItem.TodoStatus.COMPLETED) {
                todoItem.setCompletedAt(null);
            }
        }
        if (request.getPriority() != null) {
            todoItem.setPriority(TodoItem.TodoPriority.valueOf(request.getPriority().name()));
        }
        if (request.getDueDate() != null) {
            todoItem.setDueDate(request.getDueDate());
        }

        todoItem = todoItemRepository.save(todoItem);
        log.info("更新待办事项成功: id={}", id);
        return TodoItemDto.fromEntity(todoItem);
    }

    /**
     * 删除待办事项
     */
    @Transactional
    public void deleteTodo(Long id) {
        if (!todoItemRepository.existsById(id)) {
            throw new RuntimeException("待办事项不存在");
        }
        todoItemRepository.deleteById(id);
        log.info("删除待办事项成功: id={}", id);
    }

    /**
     * 根据ID获取待办事项
     */
    public TodoItemDto getTodoById(Long id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("待办事项不存在"));
        return TodoItemDto.fromEntity(todoItem);
    }

    /**
     * 获取用户的所有待办事项
     */
    public List<TodoItemDto> getTodosByUserId(Long userId) {
        List<TodoItem> todos = todoItemRepository.findByUserId(userId);
        return todos.stream()
                .map(TodoItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 根据状态获取待办事项
     */
    public List<TodoItemDto> getTodosByStatus(Long userId, TodoItem.TodoStatus status) {
        List<TodoItem> todos = todoItemRepository.findByUserIdAndStatus(userId, status);
        return todos.stream()
                .map(TodoItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 完成待办事项
     */
    @Transactional
    public TodoItemDto completeTodo(Long id) {
        TodoItem todoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("待办事项不存在"));
        
        todoItem.setStatus(TodoItem.TodoStatus.COMPLETED);
        todoItem.setCompletedAt(LocalDateTime.now());
        
        todoItem = todoItemRepository.save(todoItem);
        log.info("完成待办事项: id={}", id);
        return TodoItemDto.fromEntity(todoItem);
    }

    /**
     * 获取待办事项统计
     */
    public TodoStatisticsDto getStatistics(Long userId) {
        TodoStatisticsDto stats = new TodoStatisticsDto();
        List<TodoItem> allTodos = todoItemRepository.findByUserId(userId);
        stats.setTotal(allTodos.size());
        stats.setPending(todoItemRepository.countByUserIdAndStatus(userId, TodoItem.TodoStatus.PENDING));
        stats.setInProgress(todoItemRepository.countByUserIdAndStatus(userId, TodoItem.TodoStatus.IN_PROGRESS));
        stats.setCompleted(todoItemRepository.countByUserIdAndStatus(userId, TodoItem.TodoStatus.COMPLETED));
        return stats;
    }
}

