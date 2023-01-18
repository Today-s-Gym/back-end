package com.gym.post;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.post.dto.GetPostsListRes;
import com.gym.post.dto.PostPostReq;
import com.gym.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Integer userId = jwtService.getUserId();
        return new BaseResponse<>(postService.getPostsByCategoryId(userId, categoryId));
    }

    /**
     * 게시글 생성
     * [POST] /post
     */
    @PostMapping("/post")
    public BaseResponse<Integer> createPost(@RequestBody PostPostReq postPostReq) throws BaseException {
        Integer userId = jwtService.getUserId();
        return new BaseResponse<>(postService.createPost(userId, postPostReq));
    }

}
