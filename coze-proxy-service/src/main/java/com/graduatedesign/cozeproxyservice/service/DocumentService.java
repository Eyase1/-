package com.graduatedesign.cozeproxyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DocumentService {

    private final Map<String, DocumentInfo> documents = new ConcurrentHashMap<>();

    public DocumentService() {
        // 初始化模拟文档库
        initSampleDocuments();
    }

    private void initSampleDocuments() {
        addDocument(new DocumentInfo("doc_001", "项目计划书.pdf", "项目管理",
                "本项目旨在开发智能办公系统，包含文档管理、日程安排、智能助手等功能..."));

        addDocument(new DocumentInfo("doc_002", "技术方案.docx", "技术文档",
                "系统采用Spring Boot架构，前端使用Vue3，集成扣子AI提供智能服务..."));

        addDocument(new DocumentInfo("doc_003", "会议纪要.md", "会议记录",
                "本次会议讨论了项目进度、技术选型和下一步工作计划..."));
    }

    public void addDocument(DocumentInfo document) {
        documents.put(document.getId(), document);
        log.info("添加文档: {} - {}", document.getId(), document.getName());
    }

    public List<DocumentInfo> getUserDocuments(String userId) {
        return new ArrayList<>(documents.values());
    }

    public DocumentInfo getDocument(String docId) {
        return documents.get(docId);
    }

    public String analyzeDocument(String docId, String analysisType) {
        DocumentInfo doc = documents.get(docId);
        if (doc == null) {
            return "文档不存在";
        }

        switch (analysisType) {
            case "summary":
                return generateSummary(doc);
            case "keywords":
                return extractKeywords(doc);
            case "qa":
                return "文档问答功能准备就绪";
            default:
                return "未知分析类型";
        }
    }

    private String generateSummary(DocumentInfo doc) {
        return String.format("📄 文档摘要：《%s》\n\n" +
                        "• 类型：%s\n" +
                        "• 主要内容：%s\n" +
                        "• 分析结果：这是一个关于%s的文档，包含了详细的相关信息。",
                doc.getName(), doc.getType(),
                doc.getContent().substring(0, Math.min(50, doc.getContent().length())) + "...",
                doc.getType());
    }

    private String extractKeywords(DocumentInfo doc) {
        return String.format("🔑 关键词提取：《%s》\n\n" +
                        "• 主要关键词：智能办公、项目管理、技术方案\n" +
                        "• 相关术语：Spring Boot、扣子AI、微服务\n" +
                        "• 主题分类：%s",
                doc.getName(), doc.getType());
    }

    public static class DocumentInfo {
        private String id;
        private String name;
        private String type;
        private String content;
        private long uploadTime;

        public DocumentInfo(String id, String name, String type, String content) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.content = content;
            this.uploadTime = System.currentTimeMillis();
        }

        // getters and setters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getContent() { return content; }
        public long getUploadTime() { return uploadTime; }
    }
}