package com.gym.user;

import com.gym.config.exception.BaseException;
import com.gym.config.exception.BaseResponse;
import com.gym.user.dto.AccountPrivacyReq;
import com.gym.user.dto.UserEmailRes;
import com.gym.utils.JwtService;
import com.gym.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}