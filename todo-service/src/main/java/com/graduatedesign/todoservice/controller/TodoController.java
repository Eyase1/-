package com.graduatedesign.todoservice.controller;

import com.graduatedesign.commonmodule.dto.ApiResponse;
import com.graduatedesign.todoservice.dto.*;
import com.graduatedesign.todoservice.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    /**
     * 创建待办事项
     */
    @PostMapping({"", "/"})
    public ApiResponse<TodoItemDto> createTodo(@RequestBody CreateTodoRequest request) {
        try {
            TodoItemDto todo = todoService.createTodo(request);
            return ApiResponse.success(todo);
        } catch (Exception e) {
            log.error("创建待办事项失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 更新待办事项
     */
    @PutMapping("/{id}")
    public ApiResponse<TodoItemDto> updateTodo(
            @PathVariable Long id,
            @RequestBody UpdateTodoRequest request) {
        try {
            TodoItemDto todo = todoService.updateTodo(id, request);
            return ApiResponse.success(todo);
        } catch (Exception e) {
            log.error("更新待办事项失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 删除待办事项
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTodo(@PathVariable Long id) {
        try {
            todoService.deleteTodo(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            log.error("删除待办事项失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 获取待办事项详情
     */
    @GetMapping("/{id}")
    public ApiResponse<TodoItemDto> getTodoById(@PathVariable Long id) {
        try {
            TodoItemDto todo = todoService.getTodoById(id);
            return ApiResponse.success(todo);
        } catch (Exception e) {
            log.error("获取待办事项失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 获取用户的所有待办事项
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<TodoItemDto>> getTodosByUserId(@PathVariable Long userId) {
        try {
            List<TodoItemDto> todos = todoService.getTodosByUserId(userId);
            return ApiResponse.success(todos);
        } catch (Exception e) {
            log.error("获取待办事项列表失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 根据状态获取待办事项
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ApiResponse<List<TodoItemDto>> getTodosByStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        try {
            com.graduatedesign.todoservice.domain.TodoItem.TodoStatus todoStatus =
                    com.graduatedesign.todoservice.domain.TodoItem.TodoStatus.valueOf(status.toUpperCase());
            List<TodoItemDto> todos = todoService.getTodosByStatus(userId, todoStatus);
            return ApiResponse.success(todos);
        } catch (Exception e) {
            log.error("获取待办事项列表失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 完成待办事项
     */
    @PostMapping("/{id}/complete")
    public ApiResponse<TodoItemDto> completeTodo(@PathVariable Long id) {
        try {
            TodoItemDto todo = todoService.completeTodo(id);
            return ApiResponse.success(todo);
        } catch (Exception e) {
            log.error("完成待办事项失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }

    /**
     * 获取待办事项统计
     */
    @GetMapping("/user/{userId}/statistics")
    public ApiResponse<TodoStatisticsDto> getStatistics(@PathVariable Long userId) {
        try {
            TodoStatisticsDto stats = todoService.getStatistics(userId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return ApiResponse.error(500, e.getMessage());
        }
    }
}