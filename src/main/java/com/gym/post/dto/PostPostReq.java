package com.gym.post.dto;

import com.gym.post.photo.PostPhoto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class PostPostReq {
    private Integer categoryId;
    private String title;
    private String content;

    private Integer recordId; // null 일 수도 있음

    private List<PostPhoto> postPhotos;
}
