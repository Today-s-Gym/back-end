package com.gym.post.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetCommentsRes {
    private Integer commentId;
    private String content;
    private String writerName;
    private String writerAvatarImgUrl; //사용자의 프로필 이미지
    private boolean isMine; //본인 댓글인지 확인하기 위함
}
