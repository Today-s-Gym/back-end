package com.gym.post.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentReq {
    private Integer postId;
    private String content;
}
