package com.gym.user;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.user.dto.AccountPrivacyReq;
import com.gym.user.dto.EditMyPageReq;
import com.gym.user.dto.UserEmailRes;
import com.gym.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
}