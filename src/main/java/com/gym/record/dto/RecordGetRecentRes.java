package com.gym.record.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordGetRecentRes {
    private String content;
    private LocalDateTime createdAt;

    public RecordGetRecentRes(String content, LocalDateTime createdAt) {
        this.content = content;
        this.createdAt = createdAt;
    }
}
