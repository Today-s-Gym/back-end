package com.gym.post.dto;

import com.gym.post.photo.PostPhoto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@RequiredArgsConstructor
public class PostPostReq {
    private Integer categoryId;

    private String title;
    private String content;

    private Integer recordId = null; // null 일 수도 있음
}
