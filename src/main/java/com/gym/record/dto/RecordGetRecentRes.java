package com.gym.record.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RecordGetRecentRes {
    private String content;
    private LocalDateTime createdAt;
    private String imgUrl;

    public RecordGetRecentRes(String content, LocalDateTime createdAt, String imgUrl) {
        this.content = content;
        this.createdAt = createdAt;
        this.imgUrl = imgUrl;
    }
}
