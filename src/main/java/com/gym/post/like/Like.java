package com.gym.post.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;     // 좋아요를 누른 유저의 Id
    private Integer postId;     // 좋아요를 누른 게시글의 Id
    private boolean status;     // true = 좋아요, false = 취소
}
