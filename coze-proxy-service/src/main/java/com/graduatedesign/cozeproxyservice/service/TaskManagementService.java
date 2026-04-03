package com.graduatedesign.cozeproxyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskManagementService {

    @Autowired
    private CacheService cacheService;

    private final Map<String, List<Task>> userTasks = new ConcurrentHashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TaskManagementService() {
        initSampleTasks();
    }

    private void initSampleTasks() {
        // 为用户初始化示例任务
        List<Task> user1Tasks = Arrays.asList(
                new Task("task_001", "完成项目需求文档", "pending", "2024-01-15", "high", "撰写详细的项目需求规格说明书"),
                new Task("task_002", "准备团队会议材料", "completed", "2024-01-10", "medium", "整理会议议程和演示文稿"),
                new Task("task_003", "代码评审", "in_progress", "2024-01-12", "high", "评审同事提交的代码修改")
        );
        userTasks.put("user_001", user1Tasks);

        List<Task> user2Tasks = Arrays.asList(
                new Task("task_004", "学习Spring Boot", "in_progress", "2024-01-20", "medium", "掌握Spring Boot核心概念"),
                new Task("task_005", "准备毕业答辩", "pending", "2024-01-25", "high", "准备答辩PPT和演示材料")
        );
        userTasks.put("user_002", user2Tasks);
    }

    public String handleTaskRequest(String message, String userId) {
        log.info("任务管理请求: userId={}, message={}", userId, message);

        // 确保用户有任务列表
        userTasks.putIfAbsent(userId, new ArrayList<>());

        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("查看") || lowerMessage.contains("列表") || lowerMessage.contains("任务")) {
            return getTaskList(userId);
        } else if (lowerMessage.contains("添加") || lowerMessage.contains("创建") || lowerMessage.contains("新建")) {
            return handleCreateTask(message, userId);
        } else if (lowerMessage.contains("完成") || lowerMessage.contains("完结")) {
            return handleCompleteTask(message, userId);
        } else if (lowerMessage.contains("删除") || lowerMessage.contains("移除")) {
            return handleDeleteTask(message, userId);
        } else if (lowerMessage.contains("进度") || lowerMessage.contains("进行中")) {
            return handleUpdateProgress(message, userId);
        } else if (lowerMessage.contains("详情") || lowerMessage.contains("详细")) {
            return handleTaskDetails(message, userId);
        } else if (lowerMessage.contains("统计") || lowerMessage.contains("报告")) {
            return getTaskStatistics(userId);
        } else {
            return getTaskManagementHelp();
        }
    }

    private String getTaskList(String userId) {
        List<Task> tasks = userTasks.get(userId);

        if (tasks.isEmpty()) {
            return "📋 任务列表\n\n" +
                    "您当前没有待办任务。\n\n" +
                    "💡 使用以下命令管理任务：\n" +
                    "• 添加任务 [任务名称]\n" +
                    "• 完成任务 [任务ID]\n" +
                    "• 删除任务 [任务ID]\n" +
                    "• 任务详情 [任务ID]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📋 您的任务列表\n\n");

        // 按状态分组
        Map<String, List<Task>> tasksByStatus = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus));

        // 待办任务
        if (tasksByStatus.containsKey("pending")) {
            sb.append("🟡 待办任务：\n");
            tasksByStatus.get("pending").forEach(task -> {
                sb.append("• ").append(task.getName())
                        .append(" [ID: ").append(task.getId()).append("]")
                        .append(" 🔺").append(task.getPriority().toUpperCase())
                        .append(" 📅").append(task.getDueDate())
                        .append("\n");
            });
            sb.append("\n");
        }

        // 进行中任务
        if (tasksByStatus.containsKey("in_progress")) {
            sb.append("🔵 进行中：\n");
            tasksByStatus.get("in_progress").forEach(task -> {
                sb.append("• ").append(task.getName())
                        .append(" [ID: ").append(task.getId()).append("]")
                        .append(" 🔺").append(task.getPriority().toUpperCase())
                        .append(" 📅").append(task.getDueDate())
                        .append("\n");
            });
            sb.append("\n");
        }

        // 已完成任务
        if (tasksByStatus.containsKey("completed")) {
            sb.append("✅ 已完成：\n");
            tasksByStatus.get("completed").forEach(task -> {
                sb.append("• ").append(task.getName())
                        .append(" [ID: ").append(task.getId()).append("]")
                        .append(" 🕒").append(task.getUpdatedAt())
                        .append("\n");
            });
        }

        sb.append("\n💡 提示：\n");
        sb.append("• 使用「完成任务 [ID]」来标记任务完成\n");
        sb.append("• 使用「任务详情 [ID]」查看任务详细信息\n");
        sb.append("• 使用「任务统计」查看完成情况分析");

        return sb.toString();
    }

    private String handleCreateTask(String message, String userId) {
        // 简单的任务创建逻辑
        String taskName = extractTaskName(message);
        if (taskName.isEmpty()) {
            return "❌ 创建任务失败\n\n" +
                    "请提供任务名称，例如：\n" +
                    "• 添加任务：完成周报\n" +
                    "• 创建任务：准备会议材料\n" +
                    "• 新建学习Spring Boot的任务";
        }

        String taskId = "task_" + System.currentTimeMillis();
        Task newTask = new Task(
                taskId,
                taskName,
                "pending",
                getDefaultDueDate(),
                "medium",
                "这是一个新创建的任务"
        );

        userTasks.get(userId).add(newTask);

        return "✅ 任务创建成功\n\n" +
                "📝 任务名称: " + taskName + "\n" +
                "🆔 任务ID: " + taskId + "\n" +
                "📅 截止时间: " + newTask.getDueDate() + "\n" +
                "🔺 优先级: " + getPriorityDisplayName(newTask.getPriority()) + "\n" +
                "📊 状态: " + getStatusDisplayName(newTask.getStatus()) + "\n\n" +
                "💡 使用以下命令管理任务：\n" +
                "• 完成任务 " + taskId + "\n" +
                "• 删除任务 " + taskId + "\n" +
                "• 任务详情 " + taskId;
    }

    private String handleCompleteTask(String message, String userId) {
        List<Task> tasks = userTasks.get(userId);
        if (tasks.isEmpty()) {
            return "❌ 没有找到可完成的任务";
        }

        // 简单逻辑：完成第一个待办或进行中的任务
        Optional<Task> taskToComplete = tasks.stream()
                .filter(task -> !"completed".equals(task.getStatus()))
                .findFirst();

        if (taskToComplete.isPresent()) {
            Task task = taskToComplete.get();
            task.setStatus("completed");
            task.setUpdatedAt(LocalDateTime.now().format(formatter));

            return "🎉 任务完成！\n\n" +
                    "✅ 已完成任务: " + task.getName() + "\n" +
                    "🆔 任务ID: " + task.getId() + "\n" +
                    "🕒 完成时间: " + task.getUpdatedAt() + "\n\n" +
                    "继续保持高效工作！💪";
        } else {
            return "✅ 所有任务都已经完成了！\n\n" +
                    "您目前没有待办任务，可以休息一下或创建新任务。";
        }
    }

    private String handleDeleteTask(String message, String userId) {
        List<Task> tasks = userTasks.get(userId);
        if (tasks.isEmpty()) {
            return "❌ 没有找到可删除的任务";
        }

        // 简单逻辑：删除最后一个任务
        if (!tasks.isEmpty()) {
            Task removedTask = tasks.remove(tasks.size() - 1);
            return "🗑️ 任务已删除\n\n" +
                    "已删除任务: " + removedTask.getName() + "\n" +
                    "任务ID: " + removedTask.getId() + "\n\n" +
                    "当前剩余任务数: " + tasks.size() + " 个";
        }

        return "❌ 删除任务失败";
    }

    private String handleUpdateProgress(String message, String userId) {
        List<Task> tasks = userTasks.get(userId);
        if (tasks.isEmpty()) {
            return "❌ 没有找到可更新的任务";
        }

        // 简单逻辑：将第一个待办任务改为进行中
        Optional<Task> taskToUpdate = tasks.stream()
                .filter(task -> "pending".equals(task.getStatus()))
                .findFirst();

        if (taskToUpdate.isPresent()) {
            Task task = taskToUpdate.get();
            task.setStatus("in_progress");
            task.setUpdatedAt(LocalDateTime.now().format(formatter));

            return "🔄 任务进度已更新\n\n" +
                    "📝 任务: " + task.getName() + "\n" +
                    "🆔 ID: " + task.getId() + "\n" +
                    "📊 新状态: " + getStatusDisplayName(task.getStatus()) + "\n" +
                    "🕒 更新时间: " + task.getUpdatedAt() + "\n\n" +
                    "加油完成任务！🚀";
        } else {
            return "✅ 所有任务都已经在进行中或完成了！";
        }
    }

    private String handleTaskDetails(String message, String userId) {
        List<Task> tasks = userTasks.get(userId);
        if (tasks.isEmpty()) {
            return "❌ 没有找到任务详情";
        }

        // 显示第一个任务的详情
        Task task = tasks.get(0);

        return "📄 任务详情\n\n" +
                "📝 任务名称: " + task.getName() + "\n" +
                "🆔 任务ID: " + task.getId() + "\n" +
                "📊 状态: " + getStatusDisplayName(task.getStatus()) + "\n" +
                "📅 截止时间: " + task.getDueDate() + "\n" +
                "🔺 优先级: " + getPriorityDisplayName(task.getPriority()) + "\n" +
                "📋 描述: " + task.getDescription() + "\n" +
                "⏰ 创建时间: " + task.getCreatedAt() + "\n" +
                "🔄 更新时间: " + task.getUpdatedAt() + "\n\n" +
                "💡 可用操作：\n" +
                "• 完成任务 " + task.getId() + "\n" +
                "• 删除任务 " + task.getId();
    }

    private String getTaskStatistics(String userId) {
        List<Task> tasks = userTasks.get(userId);

        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(t -> "completed".equals(t.getStatus())).count();
        long pendingTasks = tasks.stream().filter(t -> "pending".equals(t.getStatus())).count();
        long inProgressTasks = tasks.stream().filter(t -> "in_progress".equals(t.getStatus())).count();

        double completionRate = totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0;

        return "📊 任务统计报告\n\n" +
                "📈 总体情况:\n" +
                "• 总任务数: " + totalTasks + " 个\n" +
                "• 已完成: " + completedTasks + " 个\n" +
                "• 进行中: " + inProgressTasks + " 个\n" +
                "• 待办: " + pendingTasks + " 个\n" +
                "• 完成率: " + String.format("%.1f", completionRate) + "%\n\n" +

                "🎯 效率分析:\n" +
                (completionRate >= 70 ? "• ✅ 您的任务完成率很高，继续保持！\n" :
                        completionRate >= 40 ? "• 🔄 任务进度良好，继续努力！\n" :
                                "• 💡 建议优先完成重要任务\n") +
                "• ⏰ 及时更新任务状态有助于跟踪进度\n" +
                "• 📅 合理安排截止时间提高效率\n\n" +

                "🚀 建议:\n" +
                (pendingTasks > 3 ? "• 建议集中处理待办任务\n" : "") +
                (inProgressTasks > 2 ? "• 当前进行中任务较多，建议专注完成\n" : "") +
                "• 使用「添加任务」创建新任务\n" +
                "• 使用「完成任务」标记进度";
    }

    private String getTaskManagementHelp() {
        return "📋 任务管理助手\n\n" +
                "我可以帮您管理个人任务，提高工作效率：\n\n" +
                "🔧 可用命令：\n" +
                "• 查看任务列表 - 显示所有任务\n" +
                "• 添加任务 [名称] - 创建新任务\n" +
                "• 完成任务 - 标记任务完成\n" +
                "• 删除任务 - 移除任务\n" +
                "• 任务进度 - 更新任务状态\n" +
                "• 任务详情 - 查看任务详细信息\n" +
                "• 任务统计 - 查看完成情况分析\n\n" +
                "💡 示例：\n" +
                "• \"查看我的任务\"\n" +
                "• \"添加任务：学习Spring Boot\"\n" +
                "• \"完成任务\"\n" +
                "• \"任务统计\"\n\n" +
                "请告诉我您要执行什么操作？";
    }

    // ========== 工具方法 ==========

    private String extractTaskName(String message) {
        // 简单的任务名称提取逻辑
        if (message.contains("：")) {
            return message.substring(message.indexOf("：") + 1).trim();
        } else if (message.contains(":")) {
            return message.substring(message.indexOf(":") + 1).trim();
        } else if (message.contains("任务")) {
            return message.replace("添加", "").replace("创建", "").replace("新建", "").replace("任务", "").trim();
        }
        return "新任务";
    }

    private String getDefaultDueDate() {
        return LocalDateTime.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String getStatusDisplayName(String status) {
        switch (status) {
            case "pending": return "待办";
            case "in_progress": return "进行中";
            case "completed": return "已完成";
            default: return status;
        }
    }

    private String getPriorityDisplayName(String priority) {
        switch (priority) {
            case "high": return "高";
            case "medium": return "中";
            case "low": return "低";
            default: return priority;
        }
    }

    // ========== 任务实体类 ==========

    public static class Task {
        private String id;
        private String name;
        private String status;
        private String dueDate;
        private String priority;
        private String description;
        private String createdAt;
        private String updatedAt;

        public Task(String id, String name, String status, String dueDate, String priority, String description) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.dueDate = dueDate;
            this.priority = priority;
            this.description = description;
            this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.updatedAt = this.createdAt;
        }

        // Getters and Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getStatus() { return status; }
        public String getDueDate() { return dueDate; }
        public String getPriority() { return priority; }
        public String getDescription() { return description; }
        public String getCreatedAt() { return createdAt; }
        public String getUpdatedAt() { return updatedAt; }

        public void setStatus(String status) {
            this.status = status;
        }
        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}