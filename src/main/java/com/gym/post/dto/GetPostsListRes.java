package com.gym.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetPostsListRes {
    private String categoryName;

    //게시글 관련
    private Integer postId;             // 해당 게시글의 Id
    private String title;               // 게시글 제목
    private String content;             // 게시글 내용
    private String createdAt;           // 언제 만들어졌는지 -> 20초 전, 1분 전 이런 식으로
    private Integer likeCounts;         // 좋아요 개수
    private boolean liked;              // 이 글을 보는 유저가 좋아요를 눌렀는지
    private Integer commentCounts;      // 댓글 개수

    //작성자(유저) 관련
    //private String writerAvatarImgUrl;  // 글 작성자의 아바타 이미지
    private String writerNickName;      // 글 작성자의 닉네임

    //첨부된 기록 관련
    private Integer recordId;           // 첨부된 기록의 Id
    private String recordPhotoImgUrl;   // 첨부된 기록의 이미지 -> 없을 경우 대표 이미지
    private String recordCreatedAt;      // 기록 날짜 ex) 2022/12/25
    private String recordContent;       // 기록 내용 미리 보기
}
