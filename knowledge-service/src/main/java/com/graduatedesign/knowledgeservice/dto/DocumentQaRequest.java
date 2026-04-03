package com.graduatedesign.knowledgeservice.dto;

import lombok.Data;

@Data
public class DocumentQaRequest {
    private String fileName;
    private String question;
}