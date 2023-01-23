package com.gym.post;

import com.gym.tag.config.exception.BaseException;
import com.gym.tag.config.exception.BaseResponse;
import com.gym.post.dto.GetPostsListRes;
import com.gym.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
