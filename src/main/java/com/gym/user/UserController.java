package com.gym.user;

import com.gym.avatar.avatar.dto.MyAvatarDto;
import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.user.dto.AccountPrivacyReq;
import com.gym.user.dto.EditMyPageReq;
import com.gym.user.dto.GetMyPageRes;
import com.gym.user.dto.UserEmailRes;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UtilService utilService;

    /**
     * 사용자 공개 계정 전환
     */
    @PutMapping("/user/locked")
    public BaseResponse<Integer> changeAccountPrivacy(@RequestBody AccountPrivacyReq accountPrivacyReq) {
        Integer userId = JwtService.getUserId();
        return new BaseResponse<>(userService.changeAccountPrivacy(userId, accountPrivacyReq.isLocked()));
    }

    /**
     * 사용자 이메일 조회
     */
    @GetMapping("/user/email")
    public BaseResponse<UserEmailRes> getUserEmail() throws BaseException {
        String userEmail = userService.findUserEmailByUserId(JwtService.getUserId());
        UserEmailRes userEmailRes = new UserEmailRes(userEmail);
        return new BaseResponse<>(userEmailRes);
    }

    /**
     * 마이페이지 수정
     */
    @PatchMapping("/user/mypage")
    public BaseResponse<Integer> editMyPage(@RequestBody EditMyPageReq editMyPageReq) {
        try {
            return userService.editMyPage(JwtService.getUserId(), editMyPageReq.getNewNickname(), editMyPageReq.getNewIntroduce());
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 아바타 조회
     */
    @GetMapping("/user/avatar-collection")
    public BaseResponse<List<MyAvatarDto>> getMyCollection(){
        try {
            User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
            return new BaseResponse<>(userService.getMyCollection(user));
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
            User user = utilService.findByUserIdWithValidation(JwtService.getUserId());
            return new BaseResponse<>(userService.getMyPage(user));
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
            User user = utilService.findByUserIdWithValidation(userId);
            return new BaseResponse<>(userService.getUserProfile(user));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}