package com.gym.tag.dto;

import lombok.Data;

@Data
public class TagGetRecentRes {
    String imgUrl;

    public TagGetRecentRes(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
