package com.graduatedesign.cozeproxyservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduatedesign.cozeproxyservice.client.CozeApiClient;
import com.graduatedesign.cozeproxyservice.config.CozeConfig;
import com.graduatedesign.cozeproxyservice.dto.CozeChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CozeService {

    @Autowired  // 移除 static
    private CozeApiClient cozeApiClient;

    @Autowired  // 移除 static
    private CozeConfig cozeConfig;

    // 使用CozeApiClient进行聊天
    public String chatWithOfficeAssistant(String message) {
        try {
            log.info("收到消息: {}", message);

            // 使用CozeApiClient进行调用
            String userId = "user_" + System.currentTimeMillis(); // 动态用户ID

            // 从配置获取botId - 使用实例方法
            String botId = cozeConfig.getApi().getBotId().getOfficeAssistant();

            log.info("使用Bot ID: {} 进行对话", botId);

            CozeChatResponse response = cozeApiClient.chatWithBot(botId, message, userId);

            if (response.isSuccess()) {
                log.info("AI回复: {}", response.getContent());
                return response.getContent();
            } else {
                String errorMessage = response.getMessage();
                // 处理空错误消息的情况
                if (errorMessage == null || errorMessage.trim().isEmpty()) {
                    errorMessage = "未提供具体错误信息";
                }
                log.warn("AI调用失败: {}", errorMessage);
                return "AI服务返回错误: " + errorMessage;
            }
        } catch (Exception e) {
            log.error("调用Coze API失败", e);
            return "抱歉，AI服务暂时不可用，请稍后重试。";
        }
    }

    public String documentQa(String docId, String question) {
        try {
            log.info("文档问答: docId={}, question={}", docId, question);

            // 构建文档问答的提示词
            String prompt = String.format("请根据文档%s回答以下问题：%s", docId, question);
            String userId = "user_" + System.currentTimeMillis(); // 动态用户ID

            // 从配置获取botId - 使用实例方法
            String botId = cozeConfig.getApi().getBotId().getOfficeAssistant();

            log.info("文档问答使用Bot ID: {}", botId);

            CozeChatResponse response = cozeApiClient.chatWithBot(botId, prompt, userId);

            if (response.isSuccess()) {
                log.info("文档问答回复: {}", response.getContent());
                return response.getContent();
            } else {
                String errorMessage = response.getMessage();
                // 处理空错误消息的情况
                if (errorMessage == null || errorMessage.trim().isEmpty()) {
                    errorMessage = "未提供具体错误信息";
                }
                log.warn("文档问答调用失败: {}", errorMessage);
                return "文档问答服务返回错误: " + errorMessage;
            }
        } catch (Exception e) {
            log.error("文档问答失败", e);
            return "文档问答服务暂时不可用。";
        }
    }

    /**
     * 任务管理功能
     */
    public String taskManagement(String taskRequest) {
        try {
            log.info("任务管理请求: {}", taskRequest);

            // 构建任务管理提示词
            String prompt = "作为任务管理助手，请帮我处理：" + taskRequest;
            String userId = "user_" + System.currentTimeMillis();

            // 从配置获取botId - 使用实例方法
            String botId = cozeConfig.getApi().getBotId().getOfficeAssistant();

            CozeChatResponse response = cozeApiClient.chatWithBot(botId, prompt, userId);

            if (response.isSuccess()) {
                log.info("任务管理回复: {}", response.getContent());
                return response.getContent();
            } else {
                String errorMessage = response.getMessage();
                if (errorMessage == null || errorMessage.trim().isEmpty()) {
                    errorMessage = "未提供具体错误信息";
                }
                log.warn("任务管理调用失败: {}", errorMessage);
                return "任务管理服务暂时不可用: " + errorMessage;
            }
        } catch (Exception e) {
            log.error("任务管理处理失败", e);
            return "任务管理服务暂时不可用。";
        }
    }


    /**
     * 文档问答（带文件内容）
     */
    public String documentQaWithContent(String docId, String question, String fileContent) {
        try {
            log.info("文档问答: docId={}, question={}, contentLength={}",
                    docId, question, fileContent.length());

            // 构建包含文件内容的提示词
            String prompt = String.format(
                    "请根据以下文档内容回答问题：\n\n" +
                            "文档ID：%s\n" +
                            "文档内容：\n%s\n\n" +
                            "问题：%s\n\n" +
                            "请基于文档内容回答：",
                    docId, fileContent, question
            );

            String userId = "user_" + System.currentTimeMillis();
            String botId = cozeConfig.getApi().getBotId().getOfficeAssistant();

            log.info("文档问答使用Bot ID: {}", botId);

            CozeChatResponse response = cozeApiClient.chatWithBot(botId, prompt, userId);

            if (response.isSuccess()) {
                log.info("文档问答回复: {}", response.getContent());
                return response.getContent();
            } else {
                String errorMessage = response.getMessage();
                if (errorMessage == null || errorMessage.trim().isEmpty()) {
                    errorMessage = "未提供具体错误信息";
                }
                log.warn("文档问答调用失败: {}", errorMessage);
                return "文档问答服务返回错误: " + errorMessage;
            }
        } catch (Exception e) {
            log.error("文档问答失败", e);
            return "文档问答服务暂时不可用。";
        }
    }


    // 在 CozeService.java 中添加以下方法

    /**
     * 生成日程建议
     */
    public Map<String, Object> generateScheduleSuggestions(String userId, String naturalText,
                                                           String scheduleDate, String workingHours,
                                                           Integer defaultDurationMinutes,
                                                           Boolean autoSave, Boolean overwriteConflicts) {
        try {
            log.info("生成日程建议 - 用户: {}, 日期: {}, 内容: {}", userId, scheduleDate, naturalText);

            // 构建智能体提示词
            String prompt = buildSchedulePrompt(naturalText, scheduleDate, workingHours, defaultDurationMinutes);

            // 调用智能体
            String agentResponse = chatWithOfficeAssistant(prompt);

            // 解析智能体回复
            return parseScheduleResponse(agentResponse, naturalText, scheduleDate);
        } catch (Exception e) {
            log.error("生成日程建议失败", e);
            return createErrorResponse("生成日程建议失败: " + e.getMessage());
        }
    }

    private String buildSchedulePrompt(String naturalText, String scheduleDate,
                                       String workingHours, Integer defaultDuration) {
        return String.format(
                "你是一个专业的日程安排助手。请根据用户需求生成合理的日程安排建议。\n\n" +
                        "用户需求：%s\n" +
                        "目标日期：%s\n" +
                        "工作时间段：%s\n" +
                        "默认时长：%d分钟\n\n" +
                        "请按照以下JSON格式返回：\n" +
                        "{\n" +
                        "  \"summary\": \"总体说明\",\n" +
                        "  \"suggestions\": [\n" +
                        "    {\n" +
                        "      \"title\": \"日程标题\",\n" +
                        "      \"description\": \"详细描述\",\n" +
                        "      \"startTime\": \"09:00\",\n" +
                        "      \"endTime\": \"10:00\",\n" +
                        "      \"category\": \"工作/会议/学习/休息/其他\",\n" +
                        "      \"priority\": \"HIGH/MEDIUM/LOW\",\n" +
                        "      \"estimatedDuration\": 60\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"warnings\": [\"注意事项1\", \"注意事项2\"]\n" +
                        "}\n\n" +
                        "要求：\n" +
                        "1. 时间安排要合理，避免冲突\n" +
                        "2. 优先级根据重要性设置\n" +
                        "3. 分类要准确\n" +
                        "4. 时长要合理\n" +
                        "5. 返回纯JSON格式，不要有其他文字",
                naturalText, scheduleDate, workingHours, defaultDuration
        );
    }

    private Map<String, Object> parseScheduleResponse(String agentResponse, String originalText, String scheduleDate) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 清理响应文本，提取JSON部分
            String jsonString = extractJsonFromResponse(agentResponse);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> parsedResponse = mapper.readValue(jsonString, Map.class);

            result.put("prompt", originalText);
            result.put("agentReply", agentResponse);
            result.put("summary", parsedResponse.getOrDefault("summary", "智能生成的日程安排"));
            result.put("suggestions", parsedResponse.getOrDefault("suggestions", new ArrayList<>()));
            result.put("warnings", parsedResponse.getOrDefault("warnings", new ArrayList<>()));
            result.put("createdSchedules", new ArrayList<>());
            result.put("existingSchedules", new ArrayList<>());

        } catch (Exception e) {
            log.warn("智能体响应JSON解析失败，使用默认解析: {}", e.getMessage());
            result = createDefaultSuggestions(originalText, scheduleDate);
        }

        return result;
    }

    private String extractJsonFromResponse(String response) {
        // 尝试提取JSON部分
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}") + 1;

        if (start >= 0 && end > start) {
            return response.substring(start, end);
        }
        return response; // 如果没找到，返回原响应
    }

    private Map<String, Object> createDefaultSuggestions(String naturalText, String scheduleDate) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> suggestions = new ArrayList<>();

        // 根据自然语言生成默认建议
        if (naturalText.contains("会议") || naturalText.contains("开会")) {
            suggestions.add(createSuggestion("团队会议", "项目进度同步会议", "09:00", "10:00", "会议", "HIGH", 60));
        }
        if (naturalText.contains("汇报") || naturalText.contains("报告")) {
            suggestions.add(createSuggestion("项目汇报", "准备并演示项目进展", "14:00", "15:00", "工作", "HIGH", 60));
        }
        if (naturalText.contains("学习") || naturalText.contains("培训")) {
            suggestions.add(createSuggestion("技能学习", "专业知识学习时间", "16:00", "17:00", "学习", "MEDIUM", 60));
        }
        if (naturalText.contains("代码") || naturalText.contains("开发")) {
            suggestions.add(createSuggestion("代码开发", "功能模块开发工作", "10:00", "12:00", "工作", "HIGH", 120));
        }

        // 如果没有匹配到特定类型，创建通用安排
        if (suggestions.isEmpty()) {
            suggestions.add(createSuggestion("工作安排", naturalText, "10:00", "11:00", "工作", "MEDIUM", 60));
            suggestions.add(createSuggestion("午休", "午餐和休息时间", "12:00", "13:00", "休息", "LOW", 60));
            suggestions.add(createSuggestion("下午工作", "继续完成工作任务", "13:00", "17:00", "工作", "MEDIUM", 240));
        }

        result.put("prompt", naturalText);
        result.put("agentReply", "默认生成的日程安排");
        result.put("summary", "基于您的需求生成的日程安排建议");
        result.put("suggestions", suggestions);
        result.put("warnings", Arrays.asList("建议时间可能需要根据实际情况调整"));
        result.put("createdSchedules", new ArrayList<>());
        result.put("existingSchedules", new ArrayList<>());

        return result;
    }

    private Map<String, Object> createSuggestion(String title, String description,
                                                 String startTime, String endTime,
                                                 String category, String priority, int duration) {
        Map<String, Object> suggestion = new HashMap<>();
        suggestion.put("title", title);
        suggestion.put("description", description);
        suggestion.put("startTime", startTime);
        suggestion.put("endTime", endTime);
        suggestion.put("category", category);
        suggestion.put("priority", priority);
        suggestion.put("estimatedDuration", duration);
        return suggestion;
    }

    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> result = new HashMap<>();
        result.put("prompt", "");
        result.put("agentReply", "");
        result.put("summary", "生成日程建议失败");
        result.put("suggestions", new ArrayList<>());
        result.put("warnings", Arrays.asList(errorMessage));
        result.put("createdSchedules", new ArrayList<>());
        result.put("existingSchedules", new ArrayList<>());
        return result;
    }
}