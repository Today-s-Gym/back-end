package com.gym.post;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.post.dto.GetPostsListRes;
import com.gym.post.dto.PostPostReq;
import com.gym.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;

    /**
     * 특정 카테고리에 대한 게시글 목록 조회
     * [GET] /posts/{categoryId}
     */
    @GetMapping("/posts/{categoryId}")
    public BaseResponse<List<GetPostsListRes>> getPostsByCategoryId(@PathVariable("categoryId") Integer categoryId) throws BaseException {
        try {
            Integer userId = jwtService.getUserId();
            return new BaseResponse<>(postService.getPostsByCategoryId(userId, categoryId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 게시글 생성
     * [POST] /post
     */
    @PostMapping("/post")
    public BaseResponse<Integer> createPost(@RequestBody PostPostReq postPostReq) throws BaseException {
        try {
            Integer userId = jwtService.getUserId();
            return new BaseResponse<>(postService.createPost(userId, postPostReq));
        }
        catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 게시글 삭제
     * [PATCH] /post/delete
     */
    @PatchMapping("/post/delete")
    public BaseResponse<String> deletePost(@Param("postId") Integer postId) throws BaseException {
        try {
            Integer userId = jwtService.getUserId();
            return new BaseResponse<>(postService.deletePost(postId));
        }
        catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
