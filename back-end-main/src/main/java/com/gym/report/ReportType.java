package com.gym.report;

public enum ReportType {
    USER("user"),
    POST("post"),
    COMMENT("comment");

    private final String type;

    ReportType(String type) {
        this.type = type;
    }
}