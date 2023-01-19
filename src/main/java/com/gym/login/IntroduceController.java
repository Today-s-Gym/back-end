package com.gym.login;


import com.gym.config.exception.BaseResponse;
import com.gym.config.exception.BaseResponseStatus;
import com.gym.user.User;
import com.gym.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class IntroduceController {

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/login/introduce")
    public @ResponseBody BaseResponse<String> updateIntroduce(@RequestBody User user, String nickname, String introduce)
    {

        if((introduce.length() >= 0) && (introduce.length() <= 30)){
            user.setNickName(nickname);
            user.setIntroduce(introduce);
            userRepository.save(user);
            return new BaseResponse<>("성공");

        }
        else{
            return new BaseResponse<>(BaseResponseStatus.INTRODUCE_ERROR);

        }
    }
}
