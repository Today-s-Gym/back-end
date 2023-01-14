package com.gym.tag.dto;

import com.gym.tag.Tag;
import lombok.Data;

@Data
public class TagGetRes {
    private String name;

    public TagGetRes(Tag tag) {
        this.name = tag.getName();
    }
}
