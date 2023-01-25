package com.gym.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetMyPostsListRes {
    private String categoryName;

    private Integer postId;
    private String title;
    private String content;
    private String createdAt;
    private boolean liked;
    private Integer commentCounts;
    private String postPhotoImgUrl;

}
