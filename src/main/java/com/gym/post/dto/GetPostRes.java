package com.gym.post.dto;

import com.gym.post.comment.dto.GetCommentsRes;
import com.gym.record.dto.RecordGetRecentRes;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPostRes {
    private GetPostsListRes getPostRes;

    private boolean isMine;
}
