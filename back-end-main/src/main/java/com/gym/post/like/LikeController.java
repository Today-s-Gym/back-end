package com.gym.post.like;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.post.like.dto.PushLikeRes;
import com.gym.login.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final JwtService jwtService;

    /**
     * 좋아요 누르기
     * [POST] /post/like
     */
    @PostMapping("/post/like")
    public BaseResponse<PushLikeRes> pushLike(@Param("postId") Integer postId) throws BaseException {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(likeService.pushLike(userId, postId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
