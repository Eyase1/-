package com.graduatedesign.cozeproxyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class OfficeAssistantService {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private DocumentService documentService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public String processOfficeRequest(String message, String userId) {
        log.info("处理办公请求 - 用户: {}, 消息: {}", userId, message);

        // 更新用户会话
        sessionService.updateSessionActivity(userId);

        // 智能路由到不同功能
        if (isDocumentRelated(message)) {
            return handleDocumentRequest(message, userId);
        } else if (isScheduleRelated(message)) {
            return handleScheduleRequest(message, userId);
        } else if (isMeetingRelated(message)) {
            return handleMeetingRequest(message, userId);
        } else {
            return handleGeneralAssistant(message, userId);
        }
    }

    private boolean isDocumentRelated(String message) {
        String lowerMsg = message.toLowerCase();
        return lowerMsg.contains("文档") || lowerMsg.contains("文件") ||
                lowerMsg.contains("pdf") || lowerMsg.contains("word") ||
                lowerMsg.contains("总结") || lowerMsg.contains("摘要");
    }

    private boolean isScheduleRelated(String message) {
        String lowerMsg = message.toLowerCase();
        return lowerMsg.contains("日程") || lowerMsg.contains("安排") ||
                lowerMsg.contains("时间") || lowerMsg.contains("提醒");
    }

    private boolean isMeetingRelated(String message) {
        String lowerMsg = message.toLowerCase();
        return lowerMsg.contains("会议") || lowerMsg.contains("开会") ||
                lowerMsg.contains("讨论") || lowerMsg.contains("预约");
    }

    private String handleDocumentRequest(String message, String userId) {
        if (message.contains("列表") || message.contains("查看文档")) {
            return generateDocumentList(userId);
        } else if (message.contains("摘要") || message.contains("总结")) {
            return documentService.analyzeDocument("doc_001", "summary");
        } else if (message.contains("关键词")) {
            return documentService.analyzeDocument("doc_002", "keywords");
        } else {
            return "📄 文档管理助手\n\n" +
                    "我可以帮您：\n" +
                    "• 查看文档列表\n" +
                    "• 生成文档摘要\n" +
                    "• 提取关键词\n" +
                    "• 智能文档问答\n\n" +
                    "请告诉我您需要什么帮助？";
        }
    }

    // 修改OfficeAssistantService的handleScheduleRequest方法
    private String handleScheduleRequest(String message, String userId) {
        // 解析用户输入获取关键信息
        String event = extractEventFromMessage(message);
        LocalDateTime suggestedTime = extractTimeFromMessage(message);

        // 如果用户没指定时间，默认建议明天同一时间
        if (suggestedTime == null) {
            suggestedTime = LocalDateTime.now().plusDays(1);
        }

        // 检查该时间是否有冲突
        boolean hasConflict = checkScheduleConflict(userId, suggestedTime.toLocalDate(),
                suggestedTime.toLocalTime(),
                suggestedTime.plusMinutes(60).toLocalTime());

        // 构建带冲突信息的响应
        StringBuilder response = new StringBuilder();
        response.append("📅 智能日程安排\n\n已为您解析日程需求：\n")
                .append("• 事项：").append(event).append("\n")
                .append("• 建议时间：").append(suggestedTime.format(formatter)).append("\n")
                .append("• 提醒设置：提前30分钟\n")
                .append("• 状态：待确认\n");

        if (hasConflict) {
            response.append("\n⚠️ 注意：该时间段已有其他安排，是否需要调整时间？");
        }

        response.append("\n\n是否需要我为您正式创建这个日程？");

        return response.toString();
    }

    // 添加时间提取方法
    private LocalDateTime extractTimeFromMessage(String message) {
        // 实际项目中可使用更复杂的NLP解析
        // 这里简化处理，仅做示例
        if (message.contains("明天")) {
            return LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);
        }
        if (message.contains("今天")) {
            return LocalDateTime.now().withHour(14).withMinute(0);
        }
        return null;
    }

    // 添加冲突检查方法
    private boolean checkScheduleConflict(String userId, LocalDate date, LocalTime start, LocalTime end) {
        // 实际应调用CalendarService检查冲突
        return false; // 简化处理，默认无冲突
    }

    private String handleMeetingRequest(String message, String userId) {
        return "👥 会议安排助手\n\n" +
                "根据您的需求，建议：\n" +
                "• 会议主题：" + extractMeetingTopic(message) + "\n" +
                "• 建议时长：1小时\n" +
                "• 参与人员：相关团队成员\n" +
                "• 会议议程：待制定\n\n" +
                "我可以帮您：\n" +
                "• 发送会议邀请\n" +
                "• 准备会议议程\n" +
                "• 生成会议纪要模板";
    }

    private String handleGeneralAssistant(String message, String userId) {
        List<String> responses = Arrays.asList(
                "💼 作为您的智能办公助手，我可以帮助您处理：\n• 文档管理与分析\n• 日程安排与提醒\n• 会议组织与记录\n• 邮件起草与发送\n• 任务分配与跟踪",
                "🚀 办公效率提升建议：\n• 使用智能摘要快速了解文档内容\n• 通过自然语言创建日程安排\n• 利用模板快速生成会议纪要\n• 设置智能提醒避免遗漏重要事项",
                "🔧 当前可用功能：\n• 智能文档处理\n• 日程管理\n• 会议助手\n• 邮件辅助\n• 任务跟踪"
        );

        return responses.get(new Random().nextInt(responses.size()));
    }

    private String generateDocumentList(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("📚 您的文档库\n\n");

        var documents = documentService.getUserDocuments(userId);
        for (int i = 0; i < documents.size(); i++) {
            var doc = documents.get(i);
            sb.append(i + 1).append(". 《").append(doc.getName()).append("》")
                    .append(" [").append(doc.getType()).append("]\n");
        }

        sb.append("\n请告诉我您想对哪个文档进行操作？");
        return sb.toString();
    }

    private String extractEventFromMessage(String message) {
        if (message.contains("会议")) return "团队会议";
        if (message.contains("汇报")) return "项目汇报";
        if (message.contains("评审")) return "设计评审";
        return "工作安排";
    }

    private String extractMeetingTopic(String message) {
        if (message.contains("项目")) return "项目进度同步";
        if (message.contains("设计")) return "设计方案讨论";
        if (message.contains("需求")) return "需求分析会议";
        return "团队协作会议";
    }
}