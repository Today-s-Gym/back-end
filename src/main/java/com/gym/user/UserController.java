package com.gym.user;

import com.gym.avatar.avatar.dto.MyAvatarDto;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.login.jwt.JwtService;
import com.gym.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.gym.config.exception.BaseResponseStatus.REQUEST_ERROR;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 사용자 공개 계정 전환
     */
    @PutMapping("/user/locked")
    public BaseResponse<Integer> changeAccountPrivacy(@RequestBody AccountPrivacyReq accountPrivacyReq) {
        try {
            Integer userId = jwtService.getUserIdx();

            Set<String> values = Set.of("true", "True", "TRUE", "false", "False", "FALSE");
            if (!values.contains(accountPrivacyReq.getLocked())) {
                return new BaseResponse<>(REQUEST_ERROR);
            }
            return new BaseResponse<>(userService.changeAccountPrivacy(userId,
                    Boolean.parseBoolean(accountPrivacyReq.getLocked())));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 사용자 이메일 조회
     */
    @GetMapping("/user/email")
    public BaseResponse<UserEmailRes> getUserEmail() {
        try {
            String userEmail = userService.findUserEmailByUserId(jwtService.getUserIdx());
            log.info("사용자 이메일 조회: " + userEmail);
            UserEmailRes userEmailRes = new UserEmailRes(userEmail);
            return new BaseResponse<>(userEmailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 마이페이지 수정
     */
    @PatchMapping("/user/mypage")
    public BaseResponse<Integer> editMyPage(@RequestBody EditMyPageReq editMyPageReq) {
        try {
            Integer userId = jwtService.getUserIdx();
            return userService.editMyPage(userId, editMyPageReq.getNewNickname(), editMyPageReq.getNewIntroduce());
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 아바타 조회
     */
    @GetMapping("/user/avatar-collection")
    public BaseResponse<List<MyAvatarDto>> getMyCollection() {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(userService.getMyCollection(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 마이 페이지 조회
     */
    @GetMapping("/user/mypage")
    public BaseResponse<GetMyPageRes> getMyPage() {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(userService.getMyPage(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상대방 프로필 조회
     */
    @GetMapping("/user/profile/{userId}")
    public BaseResponse<GetMyPageRes> getUserProfile(@PathVariable("userId") Integer userId) {
        try {
            return new BaseResponse<>(userService.getUserProfile(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 사용자 공개 계정 여부 조회
     */
    @GetMapping("/user/check-locked")
    public BaseResponse<AccountPrivacyRes> getUserAccountPrivacy() {
        try {
            Integer userId = jwtService.getUserIdx();
            return new BaseResponse<>(userService.getUserAccountPrivacy(userId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}