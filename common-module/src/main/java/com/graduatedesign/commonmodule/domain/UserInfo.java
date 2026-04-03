package com.graduatedesign.commonmodule.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private LocalDateTime createTime;
}