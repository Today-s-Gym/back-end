package com.gym.post.like.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class PushLikeRes {

    private Integer userId;
    private Integer postId;
    private boolean status;
    private String result;
}
