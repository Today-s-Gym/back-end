package com.gym.post;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.post.dto.GetMyPostsListRes;
import com.gym.post.dto.GetPostRes;
import com.gym.post.dto.GetPostsListRes;
import com.gym.post.dto.PostPostReq;
import com.gym.login.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;


     // 특정 카테고리에 대한 게시글 목록 조회
     // [GET] /posts/{categoryId}


    @GetMapping("/posts/{categoryId}")
    public BaseResponse<List<GetPostsListRes>> getPostsByCategoryId(@PathVariable("categoryId") Integer categoryId) throws BaseException {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(postService.getPostsByCategoryId(userId, categoryId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


     //게시글 상세 조회
     // [GET] /post/{postId}


    @GetMapping("/post/{postId}")
    public BaseResponse<GetPostRes> getPostByPostId(@PathVariable("postId") Integer postId) throws BaseException {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(postService.getPostByPostId(userId, postId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


     // 내 게시글 리스트 조회
     // [GET] /post/my


    @GetMapping("/post/my")
    public BaseResponse<List<GetMyPostsListRes>> getMyPosts() throws BaseException {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(postService.getMyPosts(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


     // 게시글 생성
     // [POST] /post


    @PostMapping("/post")
    public BaseResponse<String> createPost(@RequestPart(value = "image", required = false) List<MultipartFile> multipartFiles,
                                            @RequestPart(value = "postPostReq") PostPostReq postPostReq){
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(postService.createPost(userId, postPostReq, multipartFiles));
        }
        catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


     // 게시글 수정
     // [PATCH] /post


    @PatchMapping("/post")
    public BaseResponse<String> updatePost(@Param("postId") Integer postId,
                                           @RequestPart(value = "image", required = false) List<MultipartFile> multipartFiles,
                                           @RequestPart(value = "postPostReq") PostPostReq postPostReq) throws BaseException {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(postService.updatePost(userId, postId, postPostReq, multipartFiles));
        }
        catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


     //게시글 삭제
     // [PATCH] /post/delete


    @PatchMapping("/post/delete")
    public BaseResponse<String> deletePost(@Param("postId") Integer postId) throws BaseException {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(postService.deletePost(userId, postId));
        }
        catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
