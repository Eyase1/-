package com.graduatedesign.cozeproxyservice.dto;

import lombok.Data;

@Data
public class CozeChatRequest {
    private String message;
    private String docId; // 可选，用于文档问答
    private String userId; // 可选，如果为空则自动生成
}