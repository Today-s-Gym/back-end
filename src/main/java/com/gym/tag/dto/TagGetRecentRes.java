package com.gym.tag.dto;

import lombok.Data;

@Data
public class TagGetRecentRes {
    String tag;

    public TagGetRecentRes(String tag) {
        this.tag = tag;
    }
}
