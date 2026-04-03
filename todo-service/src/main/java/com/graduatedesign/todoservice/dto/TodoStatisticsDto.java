package com.graduatedesign.todoservice.dto;

import lombok.Data;

@Data
public class TodoStatisticsDto {
    private long total;
    private long pending;
    private long inProgress;
    private long completed;
}

