package com.gym.post.comment;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.post.comment.dto.GetCommentsRes;
import com.gym.post.comment.dto.PostCommentReq;
import com.gym.login.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JwtService jwtService;

    /**
     * 댓글 생성
     * [POST] comment
     */
    @PostMapping("/comment")
    public BaseResponse<String> createComment(@RequestBody PostCommentReq postCommentReq) throws BaseException {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse(commentService.createComment(userId, postCommentReq));
        }
        catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 POST 에 달린 댓글 조회
     * [GET] /comments/{postId}
     */
    @GetMapping("/comments/{postId}")
    public BaseResponse<GetCommentsRes> getAllComments(@PathVariable("postId") Integer postId) throws BaseException {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse(commentService.getCommentsByPostId(userId, postId));
        }
        catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * comment 삭제
     * [PATCH] /comment/delete
     */
    @PatchMapping ("/comment/delete")
    public BaseResponse<String> deleteComment(@Param("commentId") Integer commentId) throws BaseException {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse(commentService.deleteComment(userId, commentId));
        }
        catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
