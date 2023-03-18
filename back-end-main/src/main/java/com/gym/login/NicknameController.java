package com.gym.login;

import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class NicknameController {

    private final UserRepository userRepository;

    @GetMapping("/login/nickname")
    @ResponseBody
    public BaseResponse<?> updateNickname(@RequestParam("nickname") String nickName)
    {
        Optional<User> findUser = userRepository.findByNickName(nickName);
        if(findUser.isEmpty()){
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }
        else{
            return new BaseResponse<>(BaseResponseStatus.NICKNAME_ERROR);
        }
    }
}
