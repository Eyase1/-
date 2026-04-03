package com.graduatedesign.cozeproxyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailAssistantService {

    private final Map<String, String> emailTemplates = new HashMap<>();

    public EmailAssistantService() {
        initEmailTemplates();
    }

    private void initEmailTemplates() {
        emailTemplates.put("meeting", getMeetingEmailTemplate());
        emailTemplates.put("report", getReportEmailTemplate());
        emailTemplates.put("reminder", getReminderEmailTemplate());
        emailTemplates.put("notification", getNotificationEmailTemplate());
    }

    public String generateEmail(String type, Map<String, String> parameters) {
        String template = emailTemplates.getOrDefault(type, getDefaultTemplate());

        // 替换模板变量
        String emailContent = template;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            emailContent = emailContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // 添加当前日期
        emailContent = emailContent.replace("${currentDate}",
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));

        log.info("生成邮件: type={}, parameters={}", type, parameters);
        return emailContent;
    }

    public String analyzeEmailRequest(String message) {
        if (message.contains("会议") && message.contains("邮件")) {
            return "📧 会议通知邮件模板已准备，请提供：\n• 会议主题\n• 时间地点\n• 参与人员\n• 会议议程";
        } else if (message.contains("报告") || message.contains("汇报")) {
            return "📊 工作报告邮件模板已准备，请提供：\n• 报告周期\n• 主要工作内容\n• 成果亮点\n• 下一步计划";
        } else if (message.contains("提醒")) {
            return "⏰ 提醒邮件模板已准备，请提供：\n• 提醒事项\n• 重要程度\n• 截止时间\n• 相关责任人";
        } else {
            return "📧 邮件助手可帮助您：\n• 撰写会议通知\n• 编写工作报告\n• 发送工作提醒\n• 日常沟通邮件\n\n请告诉我您要写什么类型的邮件？";
        }
    }

    private String getMeetingEmailTemplate() {
        return "主题：关于${meetingTopic}的会议通知\n\n" +
                "尊敬的各位同事：\n\n" +
                "我们将于${meetingTime}在${meetingLocation}召开${meetingTopic}会议。\n\n" +
                "会议议程：\n${agenda}\n\n" +
                "请各位准时参加，如有疑问请随时联系。\n\n" +
                "谢谢！\n${sender}\n${currentDate}";
    }

    private String getReportEmailTemplate() {
        return "主题：${period}工作汇报\n\n" +
                "尊敬的${leader}：\n\n" +
                "现将${period}工作情况汇报如下：\n\n" +
                "一、主要工作内容\n${workContent}\n\n" +
                "二、成果与亮点\n${achievements}\n\n" +
                "三、下一步计划\n${nextPlan}\n\n" +
                "请审阅。\n\n" +
                "${sender}\n${currentDate}";
    }

    private String getDefaultTemplate() {
        return "主题：${subject}\n\n" +
                "${content}\n\n" +
                "${sender}\n${currentDate}";
    }

    // 其他模板方法...
    private String getReminderEmailTemplate() { return ""; }
    private String getNotificationEmailTemplate() { return ""; }
}