package com.gym.user;

import com.gym.config.exception.BaseResponse;
import com.gym.user.dto.AccountPrivacyReq;
import com.gym.utils.JwtService;
import lombok.RequiredArgsConstructor;
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
}