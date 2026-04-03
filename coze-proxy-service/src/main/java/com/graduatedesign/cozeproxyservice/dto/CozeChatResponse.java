package com.graduatedesign.cozeproxyservice.dto;

import lombok.Data;

@Data
public class CozeChatResponse {
    private String content;
    private Integer code;
    private String message;

    public boolean isSuccess() {
        return code != null && code == 0;
    }
}